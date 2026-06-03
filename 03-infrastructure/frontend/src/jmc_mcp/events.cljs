(ns jmc-mcp.events
  (:require [re-frame.core :as rf]
            [jmc-mcp.db :refer [default-db]]
            [jmc-mcp.api.client :as api]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [jmc-mcp.fx]))

(declare assoc-children)

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   default-db))

(rf/reg-event-fx
 :route/changed
 (fn [{:keys [db]} [_ new-match]]
   (let [current-route (get-in new-match [:data :name])
         params (:path-params new-match)
         recording-id (:id params)
         heapdump-id (:id params)
         old-recording-id (get-in db [:recording-detail :recording-id])
         old-heapdump-id (get-in db [:heapdump-detail :heapdump-id])
         new-db (-> db
                    (assoc :route {:current current-route
                                   :params params})
                    (cond-> (= current-route :recording-detail)
                      (assoc-in [:recording-detail :recording-id] recording-id))
                    (cond-> (= current-route :heapdump-detail)
                      (assoc-in [:heapdump-detail :heapdump-id] heapdump-id)))]
     (cond-> {:db new-db}
       (and (= current-route :recording-detail)
            (or (not= recording-id old-recording-id)
                (empty? (get-in db [:recording-detail :results]))))
       (assoc :dispatch [:analysis/load-copilot-data recording-id])

       (and (= current-route :heapdump-detail)
            (or (not= heapdump-id old-heapdump-id)
                (nil? (get-in db [:heapdump-detail :info]))))
       (assoc :dispatch [:heapdump/load-detail heapdump-id])))))

;; Copilot, Diagnostics, and Forensic view event handlers
(rf/reg-event-fx
 :analysis/load-copilot-data
 (fn [{:keys [db]} [_ recording-id]]
   (let [params (get-in db [:recording-detail :analysis-params])]
     {:dispatch-n [[:analysis/run recording-id :system-health params]
                   [:analysis/run recording-id :jfr-rules params]
                   [:analysis/run recording-id :hot-methods params]
                   [:analysis/run recording-id :thread-contention params]
                   [:analysis/run recording-id :io-hotspots params]
                   [:analysis/run recording-id :jdbc-nplusone params]]})))

(rf/reg-event-fx
 :analysis/select-tab
 (fn [{:keys [db]} [_ tab]]
   (let [recording-id (get-in db [:route :params :id])
         new-db (assoc-in db [:recording-detail :active-tab] tab)]
     (cond-> {:db new-db}
       (and (= tab :diagnostics) (not (get-in db [:recording-detail :results :thread-cpu])))
       (assoc :dispatch [:analysis/select-diagnostic-focus (get-in db [:recording-detail :diagnostic-focus] :cpu)])
       
       (and (= tab :forensics) (not (get-in db [:recording-detail :results :overview])))
       (assoc :dispatch [:analysis/select-forensic-focus (get-in db [:recording-detail :forensic-focus] :overview)])))))

(rf/reg-event-fx
 :analysis/select-diagnostic-focus
 (fn [{:keys [db]} [_ focus]]
   (let [recording-id (get-in db [:route :params :id])
         params (get-in db [:recording-detail :analysis-params])
         new-db (assoc-in db [:recording-detail :diagnostic-focus] focus)
         dispatches (case focus
                      :cpu [[:analysis/run recording-id :thread-cpu params]
                            [:analysis/run recording-id :hot-methods params]
                            [:analysis/run recording-id :thread-starvation params]]
                      :memory [[:analysis/run recording-id :gc-detail params]
                               [:analysis/run recording-id :heap-trends params]
                               [:analysis/run recording-id :predictive-leak params]]
                      :locks [[:analysis/run recording-id :thread-contention params]
                              [:analysis/run recording-id :lock-analysis params]
                              [:analysis/run recording-id :deadlock-detection params]
                              [:analysis/run recording-id :lock-resolver params]]
                      :io [[:analysis/run recording-id :io-analysis params]
                           [:analysis/run recording-id :io-hotspots params]
                           [:analysis/run recording-id :jdbc-nplusone params]]
                      [])]
     {:db new-db
      :dispatch-n dispatches})))

(rf/reg-event-fx
 :analysis/select-forensic-focus
 (fn [{:keys [db]} [_ focus]]
   (let [recording-id (get-in db [:route :params :id])
         params (get-in db [:recording-detail :analysis-params])
         new-db (assoc-in db [:recording-detail :forensic-focus] focus)
         result (get-in db [:recording-detail :results focus])]
     (cond-> {:db new-db}
       (not result)
       (assoc :dispatch [:analysis/run recording-id focus params])))))

(rf/reg-event-fx
 :analysis/apply-time-filter
 (fn [{:keys [db]} [_ start-time end-time]]
   (let [recording-id (get-in db [:route :params :id])
         new-params (assoc (get-in db [:recording-detail :analysis-params])
                           :start-time start-time
                           :end-time end-time)
         new-db (-> db
                    (assoc-in [:recording-detail :analysis-params] new-params)
                    (assoc-in [:recording-detail :results] {})) ; Clear cache to force reload
         active-tab (get-in db [:recording-detail :active-tab] :copilot)
         active-focus (get-in db [:recording-detail :diagnostic-focus] :cpu)
         active-forensic (get-in db [:recording-detail :forensic-focus] :overview)]
     {:db new-db
      :dispatch-n (case active-tab
                    :copilot [[:analysis/load-copilot-data recording-id]]
                    :diagnostics [[:analysis/select-diagnostic-focus active-focus]]
                    :forensics [[:analysis/select-forensic-focus active-forensic]]
                    [])})))

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

;; Heap Dump Management
(rf/reg-event-fx
 :heapdump/upload
 (fn [{:keys [db]} [_ file]]
   (let [form-data (js/FormData.)]
     (.append form-data "file" file)
     {:db (assoc db :upload {:status :uploading :progress 0 :error nil})
      :http-xhrio {:method          :post
                 :uri             (api/url "/heap-dumps/upload")
                 :body            form-data
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:heapdump/upload-success]
                 :on-failure      [:heapdump/upload-failure]}})))

(rf/reg-event-fx
 :heapdump/upload-success
 (fn [{:keys [db]} [_ response]]
   {:db (assoc db :upload {:status :success :progress 100 :error nil})
    :dispatch [:heapdump/load-list]
    :notify {:type :success :message "Heap dump uploaded successfully"}}))

(rf/reg-event-db
 :heapdump/upload-failure
 (fn [db [_ response]]
   (assoc db :upload {:status :error :progress 0 :error (:error response)})))

(rf/reg-event-fx
 :heapdump/load-list
 (fn [{:keys [db]} _]
   {:db (assoc-in db [:heapdumps :loading?] true)
    :http-xhrio {:method          :get
               :uri             (api/url "/heap-dumps")
               :response-format (ajax/json-response-format {:keywords? true})
               :on-success      [:heapdump/list-loaded]
               :on-failure      [:heapdump/list-failed]}}))

(rf/reg-event-db
 :heapdump/list-loaded
 (fn [db [_ response]]
   (-> db
       (assoc-in [:heapdumps :loading?] false)
       (assoc-in [:heapdumps :items] (:data response)))))

(rf/reg-event-fx
 :heapdump/list-failed
 (fn [{:keys [db]} [_ response]]
   {:db (assoc-in db [:heapdumps :loading?] false)
    :notify {:type :error :message "Failed to load heap dumps"}}))

(rf/reg-event-fx
 :heapdump/delete
 (fn [{:keys [db]} [_ heap-dump-id]]
   {:http-xhrio {:method          :delete
               :uri             (api/url "/heap-dumps/" heap-dump-id)
               :format          (ajax/json-request-format)
               :response-format (ajax/json-response-format {:keywords? true})
               :on-success      [:heapdump/deleted heap-dump-id]
               :on-failure      [:notification/add {:type :error :message "Failed to delete heap dump"}]}}))

(rf/reg-event-fx
 :heapdump/deleted
 (fn [{:keys [db]} [_ heap-dump-id]]
   {:db (update-in db [:heapdumps :items] (fn [items] (remove #(= (:heapDumpId %) heap-dump-id) items)))
    :notify {:type :success :message "Heap dump deleted"}}))

(rf/reg-event-fx
 :heapdump/link-recording
 (fn [{:keys [db]} [_ heap-dump-id recording-id]]
   {:http-xhrio {:method          :post
               :uri             (api/url "/heap-dumps/" heap-dump-id "/link/" recording-id)
               :format          (ajax/json-request-format)
               :response-format (ajax/json-response-format {:keywords? true})
               :on-success      [:heapdump/linked heap-dump-id recording-id]
               :on-failure      [:notification/add {:type :error :message "Failed to link heap dump"}]}}))

(rf/reg-event-fx
 :heapdump/linked
 (fn [{:keys [db]} [_ heap-dump-id recording-id]]
   {:dispatch [:library/load-recordings]
    :notify {:type :success :message "Heap dump linked to recording"}}))

;; Heap Dump Detail
(rf/reg-event-fx
 :heapdump/load-detail
 (fn [{:keys [db]} [_ heapdump-id]]
   {:db (assoc-in db [:heapdump-detail :loading?] true)
    :http-xhrio {:method          :get
                 :uri             (api/url "/heap-dumps/" heapdump-id)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:heapdump/detail-loaded heapdump-id]
                 :on-failure      [:heapdump/detail-failed]}}))

(rf/reg-event-fx
 :heapdump/detail-loaded
 (fn [{:keys [db]} [_ heapdump-id response]]
   (let [info (:data response)
         new-db (-> db
                    (assoc-in [:heapdump-detail :loading?] false)
                    (assoc-in [:heapdump-detail :info] info)
                    (assoc-in [:heapdump-detail :heapdump-id] heapdump-id)
                    (assoc-in [:heapdump-detail :analysis-results] {})
                    (assoc-in [:heapdump-detail :cross-analysis] nil))]
     {:db new-db
      :http-xhrio {:method          :get
                   :uri             (api/url "/heap-dumps/" heapdump-id "/linked-recording")
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success      [:heapdump/linked-recording-loaded]
                   :on-failure      [:heapdump/linked-recording-failed]}})))

(rf/reg-event-db
 :heapdump/detail-failed
 (fn [db _]
   (assoc-in db [:heapdump-detail :loading?] false)))

(rf/reg-event-db
 :heapdump/linked-recording-loaded
 (fn [db [_ response]]
   (assoc-in db [:heapdump-detail :linked-recording-id] (get-in response [:data :recordingId]))))

(rf/reg-event-db
 :heapdump/linked-recording-failed
 (fn [db _]
   (assoc-in db [:heapdump-detail :linked-recording-id] nil)))

(rf/reg-event-db
 :heapdump/select-tab
 (fn [db [_ tab]]
   (assoc-in db [:heapdump-detail :active-tab] tab)))

(rf/reg-event-fx
 :heapdump/run-analysis
 (fn [{:keys [db]} [_ type params]]
   (let [heapdump-id (get-in db [:heapdump-detail :heapdump-id])]
     {:db (assoc-in db [:heapdump-detail :loading?] true)
      :http-xhrio {:method          :post
                   :uri             (api/url "/heap-dumps/" heapdump-id "/analyze/" (name type))
                   :params          (or params {})
                   :format          (ajax/json-request-format)
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success      [:heapdump/analysis-result type]
                   :on-failure      [:heapdump/analysis-failed type]}})))

(rf/reg-event-db
 :heapdump/analysis-result
 (fn [db [_ type response]]
   (-> db
       (assoc-in [:heapdump-detail :loading?] false)
       (assoc-in [:heapdump-detail :analysis-results type] (:data response)))))

(rf/reg-event-db
 :heapdump/analysis-failed
 (fn [db [_ type]]
   (assoc-in db [:heapdump-detail :loading?] false)))

;; Dominator tree expansion
(rf/reg-event-fx
 :heapdump/expand-dominator
 (fn [{:keys [db]} [_ tree-id node-id]]
   (let [heapdump-id (get-in db [:heapdump-detail :heapdump-id])
         expanded (get-in db [:heapdump-detail :dominator-tree :expanded])
         is-expanded? (contains? expanded node-id)]
     (if is-expanded?
       {:db (update-in db [:heapdump-detail :dominator-tree :expanded] disj node-id)}
       {:db (update-in db [:heapdump-detail :dominator-tree :loading-nodes] conj node-id)
        :http-xhrio {:method          :post
                     :uri             (api/url "/heap-dumps/" heapdump-id "/analyze/dominator-tree/" tree-id "/expand?nodeId=" node-id)
                     :format          (ajax/json-request-format)
                     :response-format (ajax/json-response-format {:keywords? true})
                     :on-success      [:heapdump/dominator-expanded node-id]
                     :on-failure      [:heapdump/analysis-failed :dominator-tree]}}))))

(rf/reg-event-db
 :heapdump/dominator-expanded
 (fn [db [_ node-id response]]
   (let [children (:data response)]
     (-> db
         (update-in [:heapdump-detail :dominator-tree :loading-nodes] disj node-id)
         (update-in [:heapdump-detail :dominator-tree :expanded] conj node-id)
         (update-in [:heapdump-detail :analysis-results :dominator-tree :topDominators]
                    (fn [top-dominators]
                      (assoc-children top-dominators node-id children)))))))

;; Cross analysis
(rf/reg-event-fx
 :heapdump/run-cross-analysis
 (fn [{:keys [db]} _]
   (let [heapdump-id (get-in db [:heapdump-detail :heapdump-id])
         recording-id (get-in db [:heapdump-detail :linked-recording-id])]
     (if recording-id
       {:db (assoc-in db [:heapdump-detail :loading?] true)
        :http-xhrio {:method          :post
                     :uri             (api/url "/recordings/" recording-id "/analyze/cross")
                     :params          {}
                     :format          (ajax/json-request-format)
                     :response-format (ajax/json-response-format {:keywords? true})
                     :on-success      [:heapdump/cross-analysis-result]
                     :on-failure      [:heapdump/analysis-failed :cross]}}
       {:db db}))))

(rf/reg-event-db
 :heapdump/cross-analysis-result
 (fn [db [_ response]]
   (-> db
       (assoc-in [:heapdump-detail :loading?] false)
       (assoc-in [:heapdump-detail :cross-analysis] (:data response)))))

;; Reuse comparison helper for updating nested children
(defn- assoc-children [nodes target-id children]
  (reduce (fn [acc node]
            (conj acc
                  (if (= (:objectId node) target-id)
                    (assoc node :children children)
                    (if-let [existing-children (:children node)]
                      (assoc node :children (assoc-children existing-children target-id children))
                      node))))
          []
          nodes))

(rf/reg-event-fx
 :analysis/select-type
 (fn [{:keys [db]} [_ type]]
   (let [recording-id (get-in db [:route :params :id])
         result (get-in db [:recording-detail :results type])]
     (cond-> {:db (assoc-in db [:recording-detail :active-analysis] type)}
       (not result) (assoc :dispatch [:analysis/run recording-id type (get-in db [:recording-detail :analysis-params])])))))

(defn- resolve-time-params [db recording-id params]
  (let [info (or (get-in db [:recording-detail :info])
                 (some #(when (= (:id %) recording-id) %) (get-in db [:recordings :items])))
        start-sec (:start-time params)
        end-sec (:end-time params)]
    (cond-> params
      (and start-sec (:startTime info))
      (assoc :start-time (let [start-inst (js/Date. (:startTime info))
                               new-time (+ (.getTime start-inst) (* start-sec 1000))]
                           (.toISOString (js/Date. new-time))))
      (and end-sec (:startTime info))
      (assoc :end-time (let [start-inst (js/Date. (:startTime info))
                             new-time (+ (.getTime start-inst) (* end-sec 1000))]
                         (.toISOString (js/Date. new-time)))))))

;; Analysis
(rf/reg-event-fx
 :analysis/run
 (fn [{:keys [db]} [_ recording-id type params]]
   (when (and recording-id type)
     (let [resolved-params (resolve-time-params db recording-id params)]
       {:db (assoc-in db [:recording-detail :loading?] true)
        :http-xhrio {:method          :post
                   :uri             (api/url "/recordings/" recording-id "/analyze/" (name type))
                   :params          resolved-params
                   :format          (ajax/json-request-format)
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success      [:analysis/result type]
                   :on-failure      [:analysis/failed type]}}))))

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
   (let [resolved-params (resolve-time-params db recording-id params)]
     {:db (assoc-in db [:recording-detail :loading?] true)
      :http-xhrio {:method          :post
                 :uri             (api/url "/recordings/" recording-id "/analyze/" (name type) "/async")
                 :params          resolved-params
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:analysis/job-created recording-id type]
                 :on-failure      [:analysis/failed type]}})))

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
