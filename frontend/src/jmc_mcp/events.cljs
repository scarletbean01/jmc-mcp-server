(ns jmc-mcp.events
  (:require [re-frame.core :as rf]
            [jmc-mcp.db :refer [default-db]]
            [jmc-mcp.api.client :as api]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [jmc-mcp.fx]))

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   default-db))

(rf/reg-event-db
 :route/changed
 (fn [db [_ new-match]]
   (assoc db :route {:current (get-in new-match [:data :name])
                     :params (:path-params new-match)})))

;; Notifications
(rf/reg-event-db
 :notification/add
 (fn [db [_ notification]]
   (let [id (random-uuid)
         new-notification (assoc notification :id id)]
     (update db :notifications conj new-notification))))

(rf/reg-event-fx
 :notification/remove
 (fn [{:keys [db]} [_ id]]
   {:db (update db :notifications (fn [ns] (remove #(= (:id %) id) ns)))}))

;; Library
(rf/reg-event-fx
 :library/load-recordings
 (fn [{:keys [db]} _]
   {:db (assoc-in db [:recordings :loading?] true)
    :http-xhrio {:method          :get
               :uri             (api/url "/recordings")
               :response-format (ajax/json-response-format {:keywords? true})
               :on-success      [:library/recordings-loaded]
               :on-failure      [:library/recordings-failed]}}))

(rf/reg-event-db
 :library/recordings-loaded
 (fn [db [_ response]]
   (-> db
       (assoc-in [:recordings :loading?] false)
       (assoc-in [:recordings :items] (:data response)))))

(rf/reg-event-fx
 :library/recordings-failed
 (fn [{:keys [db]} [_ response]]
   {:db (assoc-in db [:recordings :loading?] false)
    :notify {:type :error :message "Failed to load recordings"}}))

(rf/reg-event-fx
 :library/delete-recording
 (fn [{:keys [db]} [_ recording-id]]
   {:http-xhrio {:method          :delete
               :uri             (api/url "/recordings/" recording-id)
               :format          (ajax/json-request-format)
               :response-format (ajax/json-response-format {:keywords? true})
               :on-success      [:library/recording-deleted recording-id]
               :on-failure      [:notification/add {:type :error :message "Failed to delete recording"}]}}))

(rf/reg-event-fx
 :library/recording-deleted
 (fn [{:keys [db]} [_ recording-id]]
   {:db (update-in db [:recordings :items] (fn [items] (remove #(= (:id %) recording-id) items)))
    :notify {:type :success :message "Recording deleted"}}))

;; Upload
(rf/reg-event-fx
 :upload/submit
 (fn [{:keys [db]} [_ file]]
   (let [form-data (js/FormData.)]
     (.append form-data "file" file)
     {:db (assoc db :upload {:status :uploading :progress 0 :error nil})
      :http-xhrio {:method          :post
                 :uri             (api/url "/recordings/upload")
                 :body            form-data
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:upload/success]
                 :on-failure      [:upload/failure]}})))

(rf/reg-event-fx
 :upload/success
 (fn [{:keys [db]} [_ response]]
   {:db (assoc db :upload {:status :success :progress 100 :error nil})
    :dispatch [:library/load-recordings]
    :notify {:type :success :message "Upload successful"}}))

(rf/reg-event-db
 :upload/failure
 (fn [db [_ response]]
   (assoc db :upload {:status :error :progress 0 :error (:error response)})))

(rf/reg-event-fx
 :analysis/select-type
 (fn [{:keys [db]} [_ type]]
   (let [recording-id (get-in db [:route :params :id])
         result (get-in db [:recording-detail :results type])]
     (cond-> {:db (assoc-in db [:recording-detail :active-analysis] type)}
       (not result) (assoc :dispatch [:analysis/run recording-id type (get-in db [:recording-detail :analysis-params])])))))

;; Analysis
(rf/reg-event-fx
 :analysis/run
 (fn [{:keys [db]} [_ recording-id type params]]
   (when (and recording-id type)
     {:db (assoc-in db [:recording-detail :loading?] true)
      :http-xhrio {:method          :post
                 :uri             (api/url "/recordings/" recording-id "/analyze/" (name type))
                 :params          params
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:analysis/result type]
                 :on-failure      [:analysis/failed type]}})))

(rf/reg-event-db
 :analysis/result
 (fn [db [_ type response]]
   (-> db
       (assoc-in [:recording-detail :loading?] false)
       (assoc-in [:recording-detail :results type] {:status :done :data (:data response)}))))

(rf/reg-event-fx
 :analysis/failed
 (fn [{:keys [db]} [_ type response]]
   {:db (assoc-in db [:recording-detail :loading?] false)
    :notify {:type :error :message (str "Analysis failed for " (name type))}}))

;; Comparison
(rf/reg-event-fx
 :comparison/run
 (fn [{:keys [db]} [_ params]]
   {:db (assoc-in db [:comparison :loading?] true)
    :http-xhrio {:method          :post
               :uri             (api/url "/compare/structured")
               :params          params
               :format          (ajax/json-request-format)
               :response-format (ajax/json-response-format {:keywords? true})
               :on-success      [:comparison/result]
               :on-failure      [:comparison/failed]}}))

(rf/reg-event-db
 :comparison/result
 (fn [db [_ response]]
   (-> db
       (assoc-in [:comparison :loading?] false)
       (assoc-in [:comparison :result] (:data response)))))

(defn- find-path-to-method [nodes target-method]
  (some (fn [node]
          (if (clojure.string/includes? (:methodName node) target-method)
            [(:nodeId node)]
            (when-let [path (find-path-to-method (:children node) target-method)]
              (cons (:nodeId node) path))))
        nodes))

(rf/reg-event-db
 :comparison/set-tab
 (fn [db [_ tab]]
   (assoc-in db [:comparison :active-tab] tab)))

(rf/reg-event-fx
 :comparison/navigate-to-method
 (fn [{:keys [db]} [_ method-name]]
   (let [tree-id (get-in db [:comparison :diff-call-tree :data :treeId])
         nodes (get-in db [:comparison :diff-call-tree :data :nodes])]
     (if (and tree-id (seq nodes))
       ;; If tree is already loaded, try to find the path immediately
       (let [path (find-path-to-method nodes method-name)]
         (if path
           {:db (-> db
                    (assoc-in [:comparison :active-tab] :call-tree)
                    (update-in [:comparison :diff-call-tree :expanded] into path)
                    (assoc-in [:comparison :diff-call-tree :target-method] method-name))}
           {:db (-> db
                    (assoc-in [:comparison :active-tab] :call-tree)
                    (assoc-in [:comparison :diff-call-tree :target-method] method-name))
            :dispatch [:comparison/expand-all tree-id]})) ; Fallback to expand all if path not found locally
       ;; Tree not loaded yet, just switch tab and set target
       {:db (-> db
                (assoc-in [:comparison :active-tab] :call-tree)
                (assoc-in [:comparison :diff-call-tree :target-method] method-name))}))))

(rf/reg-event-db
 :comparison/toggle-section
 (fn [db [_ section-id]]
   (update-in db [:comparison :collapsed-sections]
              (fn [collapsed]
                (if (contains? collapsed section-id)
                  (disj collapsed section-id)
                  (conj collapsed section-id))))))

(rf/reg-event-db
 :comparison/open-trace-modal
 (fn [db [_ trace]]
   (assoc-in db [:comparison :trace-modal] trace)))

(rf/reg-event-db
 :comparison/close-trace-modal
 (fn [db _]
   (assoc-in db [:comparison :trace-modal] nil)))

(rf/reg-event-fx
 :comparison/run-call-tree
 (fn [{:keys [db]} [_ params]]
   {:db (assoc-in db [:comparison :loading?] true)
    :http-xhrio {:method          :post
               :uri             (api/url "/compare/call-tree")
               :params          params
               :format          (ajax/json-request-format)
               :response-format (ajax/json-response-format {:keywords? true})
               :on-success      [:comparison/call-tree-result]
               :on-failure      [:comparison/failed]}}))

(rf/reg-event-db
 :comparison/call-tree-result
 (fn [db [_ response]]
   (-> db
       (assoc-in [:comparison :loading?] false)
       (assoc-in [:comparison :diff-call-tree :data] (:data response))
       (assoc-in [:comparison :diff-call-tree :expanded] #{})
       (assoc-in [:comparison :diff-call-tree :loading-nodes] #{}))))

(rf/reg-event-fx
 :comparison/toggle-node
 (fn [{:keys [db]} [_ tree-id node-id]]
   (let [expanded (get-in db [:comparison :diff-call-tree :expanded])
         is-expanded? (contains? expanded node-id)]
     (if is-expanded?
       {:db (update-in db [:comparison :diff-call-tree :expanded] disj node-id)}
       (let [node (fn find-node [nodes target-id]
                    (some (fn [n]
                            (if (= (:nodeId n) target-id)
                              n
                              (when (:children n)
                                (find-node (:children n) target-id))))
                          nodes))
             current-node (node (get-in db [:comparison :diff-call-tree :data :nodes]) node-id)]
         (if (and current-node (empty? (:children current-node)))
           {:db (update-in db [:comparison :diff-call-tree :loading-nodes] conj node-id)
            :http-xhrio {:method          :post
                       :uri             (api/url "/compare/call-tree/" tree-id "/expand?nodeId=" node-id)
                       :format          (ajax/json-request-format)
                       :response-format (ajax/json-response-format {:keywords? true})
                       :on-success      [:comparison/node-expanded node-id]
                       :on-failure      [:comparison/failed]}}
           {:db (update-in db [:comparison :diff-call-tree :expanded] conj node-id)}))))))

;; Helper to update nested children in Clojure.
(defn- assoc-children [nodes target-id children]
  (reduce (fn [acc node]
            (conj acc
                  (if (= (:nodeId node) target-id)
                    (assoc node :children children)
                    (if-let [existing-children (:children node)]
                      (assoc node :children (assoc-children existing-children target-id children))
                      node))))
          []
          nodes))

(rf/reg-event-fx
 :comparison/node-expanded
 (fn [{:keys [db]} [_ node-id response]]
   (let [children (get-in response [:data :children])
         target-method (get-in db [:comparison :diff-call-tree :target-method])
         tree-id (get-in db [:comparison :diff-call-tree :data :treeId])
         new-db (-> db
                    (update-in [:comparison :diff-call-tree :loading-nodes] disj node-id)
                    (update-in [:comparison :diff-call-tree :expanded] conj node-id)
                    (update-in [:comparison :diff-call-tree :data :nodes] assoc-children node-id children))]
     (if (and target-method tree-id)
       ;; Still searching for target method, let's see if we found it in the new children
       (let [nodes (get-in new-db [:comparison :diff-call-tree :data :nodes])
             path (find-path-to-method nodes target-method)]
         (if path
           ;; Found it! Stop searching and just highlight/expand the path
           {:db (update-in new-db [:comparison :diff-call-tree :expanded] into path)}
           ;; Not found yet, keep expanding all available nodes
           {:db new-db
            :dispatch [:comparison/expand-all tree-id]}))
       {:db new-db}))))

(defn- find-expandable-ids [nodes expanded]
  (reduce (fn [acc node]
            (let [id (:nodeId node)
                  children (:children node)
                  expandable? (and (:hasChildren node) (not (contains? expanded id)))]
              (cond-> (if expandable? (conj acc id) acc)
                (seq children) (into (find-expandable-ids children expanded)))))
          []
          nodes))

(rf/reg-event-fx
 :comparison/expand-all
 (fn [{:keys [db]} [_ tree-id]]
   (let [nodes (get-in db [:comparison :diff-call-tree :data :nodes])
         expanded (get-in db [:comparison :diff-call-tree :expanded])
         expandable-ids (find-expandable-ids nodes expanded)]
     {:dispatch-n (map (fn [id] [:comparison/toggle-node tree-id id]) expandable-ids)})))

(rf/reg-event-db
 :comparison/collapse-all
 (fn [db _]
   (assoc-in db [:comparison :diff-call-tree :expanded] #{})))

(rf/reg-event-fx
 :comparison/expand-all-under
 (fn [{:keys [db]} [_ tree-id node-id]]
   (let [node-fn (fn find-node [nodes target-id]
                   (some (fn [n]
                           (if (= (:nodeId n) target-id)
                             n
                             (when (:children n)
                               (find-node (:children n) target-id))))
                         nodes))
         target-node (node-fn (get-in db [:comparison :diff-call-tree :data :nodes]) node-id)
         expanded (get-in db [:comparison :diff-call-tree :expanded])
         expandable-ids (find-expandable-ids [target-node] expanded)]
     {:dispatch-n (map (fn [id] [:comparison/toggle-node tree-id id]) expandable-ids)})))

(rf/reg-event-fx
 :comparison/run-stack-traces
 (fn [{:keys [db]} [_ params]]
   {:db (assoc-in db [:comparison :loading?] true)
    :http-xhrio {:method          :post
               :uri             (api/url "/compare/stack-traces")
               :params          params
               :format          (ajax/json-request-format)
               :response-format (ajax/json-response-format {:keywords? true})
               :on-success      [:comparison/stack-traces-result]
               :on-failure      [:comparison/failed]}}))

(rf/reg-event-db
 :comparison/stack-traces-result
 (fn [db [_ response]]
   (-> db
       (assoc-in [:comparison :loading?] false)
       (assoc-in [:comparison :diff-stack-traces] (:data response)))))

(rf/reg-event-fx
 :comparison/failed
 (fn [{:keys [db]} [_ response]]
   {:db (assoc-in db [:comparison :loading?] false)
    :notify {:type :error :message "Comparison failed"}}))

(rf/reg-event-fx
 :analysis/run-async
 (fn [{:keys [db]} [_ recording-id type params]]
   {:db (assoc-in db [:recording-detail :loading?] true)
    :http-xhrio {:method          :post
               :uri             (api/url "/recordings/" recording-id "/analyze/" (name type) "/async")
               :params          params
               :format          (ajax/json-request-format)
               :response-format (ajax/json-response-format {:keywords? true})
               :on-success      [:analysis/job-created recording-id type]
               :on-failure      [:analysis/failed type]}}))

(rf/reg-event-fx
 :analysis/job-created
 (fn [{:keys [db]} [_ recording-id type response]]
   (let [job-id (get-in response [:data :jobId])]
     {:db (assoc-in db [:jobs job-id] (assoc (:data response) :recording-id recording-id :analysisType type))
      :sse/connect {:url (api/url "/recordings/" recording-id "/analyze/jobs/" job-id "/stream")
                    :on-message [:job/update job-id]
                    :on-error [:job/sse-failed job-id]}})))

(rf/reg-event-fx
 :job/update
 (fn [{:keys [db]} [_ job-id response]]
   (let [status-data (:data response)
         status (:status status-data)
         job (get-in db [:jobs job-id])]
     (cond-> {:db (assoc-in db [:jobs job-id] (merge job status-data))}
       (= status "COMPLETED")
       (assoc :dispatch [:analysis/result (:analysisType job) response])

       (or (= status "COMPLETED") (= status "FAILED"))
       (assoc :notify {:type (if (= status "COMPLETED") :success :error)
                       :message (str "Job " (name (:analysisType job)) " " status)})))))

(rf/reg-event-fx
 :job/sse-failed
 (fn [_ [_ job-id]]
   {:notify {:type :error :message (str "SSE connection failed for job " job-id)}}))
