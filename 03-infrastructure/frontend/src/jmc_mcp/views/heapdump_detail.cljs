(ns jmc-mcp.views.heapdump-detail
  (:require [jmc-mcp.views.components :as components]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [reitit.frontend.easy :as rfe]))

(defn- tab-button [label active? on-click]
  [:button {:class    (str "px-4 py-2 text-sm font-semibold rounded-lg transition-colors "
                           (if active?
                             "bg-blue-600 text-white"
                             "text-slate-500 hover:bg-slate-100"))
            :on-click on-click}
   label])

(defn- info-card [title value icon]
  [:div {:class "bg-white border border-slate-200 rounded-xl p-5 shadow-sm"}
   [:div {:class "flex items-center gap-3 mb-2"}
    [:i {:class (str "zmdi " icon " text-slate-400 text-lg")}]
    [:span {:class "text-sm font-medium text-slate-500"} title]]
   [:div {:class "text-2xl font-bold text-slate-800"} value]])

(defn- overview-tab []
  (let [detail (rf/subscribe [:heapdump/detail])]
    (fn []
      (let [info (:info @detail)]
        [:div {:class "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4"}
         [info-card "File Name" (:fileName info) "zmdi-file"]
         [info-card "Format" (or (:format info) "HPROF") "zmdi-format"]
         [info-card "Size" (str (js/Math.round (/ (:fileSize info) 1024 1024)) " MB") "zmdi-storage"]
         [info-card "Objects" (if (pos? (:objectCount info)) (str (:objectCount info)) "—") "zmdi-layers"]
         [info-card "Classes" (if (pos? (:classCount info)) (str (:classCount info)) "—") "zmdi-code"]
         [info-card "Uploaded" (:uploadTime info) "zmdi-time"]]))))

(defn- class-histogram-tab []
  (let [result (rf/subscribe [:heapdump/analysis-result :class-histogram])
        loading? (rf/subscribe [:heapdump/detail-loading?])]
    (rf/dispatch [:heapdump/run-analysis :class-histogram {}])
    (fn []
      (cond
        @loading?
        [:div {:class "py-20 flex flex-col items-center gap-4 text-slate-400"}
         [components/spinner]
         [:span "Loading class histogram..."]]

        (nil? @result)
        [:div {:class "py-20 text-center text-slate-400"} "No data"]

        :else
        (let [entries (get @result :entries [])]
          [:div {:class "bg-white border border-slate-200 rounded-xl shadow-sm overflow-hidden"}
           [:div {:class "p-4 border-b border-slate-100 bg-slate-50"}
            [:h3 {:class "font-bold text-slate-700"} "Class Histogram"]
            [:p {:class "text-sm text-slate-500"}
             (str "Total instances: " (get @result :totalInstances 0)
                  " | Total bytes: " (get @result :totalBytes 0))]]
           [:div {:class "overflow-auto"}
            [:table {:class "w-full text-sm text-left"}
             [:thead {:class "bg-slate-50 text-slate-500 font-medium"}
              [:tr
               [:th {:class "px-4 py-3"} "Class"]
               [:th {:class "px-4 py-3 text-right"} "Instances"]
               [:th {:class "px-4 py-3 text-right"} "Shallow Size"]
               [:th {:class "px-4 py-3 text-right"} "Retained Size"]]]
             [:tbody
              (for [entry entries]
                ^{:key (:className entry)}
                [:tr {:class "border-t border-slate-100 hover:bg-slate-50"}
                 [:td {:class "px-4 py-3 font-mono text-xs"} (:className entry)]
                 [:td {:class "px-4 py-3 text-right"} (str (:instanceCount entry))]
                 [:td {:class "px-4 py-3 text-right"} (str (:totalShallowSize entry))]
                 [:td {:class "px-4 py-3 text-right font-semibold"} (str (:totalRetainedSize entry))]])]]]]))))

(defn- dominator-tree-node [node tree-id depth]
  (let [expanded (rf/subscribe [:heapdump/dominator-expanded])
        loading-nodes (rf/subscribe [:heapdump/dominator-loading-nodes])
        node-id (str (:objectId node))
        is-expanded? (contains? @expanded node-id)
        is-loading? (contains? @loading-nodes node-id)
        has-children? (:hasChildren node)]
    [:div {:class (str "border-l-2 pl-3 " (if (> depth 0) "ml-4 " "") "border-slate-200")}
     [:div {:class    "flex items-center gap-2 py-1.5 cursor-pointer hover:bg-slate-50 rounded px-2"
            :on-click #(when has-children?
                         (rf/dispatch [:heapdump/expand-dominator tree-id node-id]))}
      (cond
        is-loading? [:i {:class "zmdi zmdi-spinner zmdi-hc-spin text-slate-400"}]
        (and has-children? is-expanded?) [:i {:class "zmdi zmdi-minis zmdi-chevron-down text-slate-400"}]
        has-children? [:i {:class "zmdi zmdi-chevron-right text-slate-400"}]
        :else [:span {:class "w-4"}])
      [:span {:class "font-mono text-xs text-slate-500"} (str "#" (:objectId node))]
      [:span {:class "text-sm font-medium text-slate-700"} (:className node)]
      [:span {:class "text-xs text-slate-400 ml-2"}
       (str "retained " (:retainedSize node) " | shallow " (:shallowSize node))]]
     (when is-expanded?
       (if (seq (:children node))
         (for [child (:children node)]
           ^{:key (:objectId child)}
           [dominator-tree-node child tree-id (inc depth)])
         [:div {:class "text-sm text-slate-400 py-1 pl-6"} "No children"]))]))

(defn- dominator-tree-tab []
  (let [result (rf/subscribe [:heapdump/analysis-result :dominator-tree])
        loading? (rf/subscribe [:heapdump/detail-loading?])]
    (rf/dispatch [:heapdump/run-analysis :dominator-tree {}])
    (fn []
      (cond
        @loading?
        [:div {:class "py-20 flex flex-col items-center gap-4 text-slate-400"}
         [components/spinner]
         [:span "Loading dominator tree..."]]

        (nil? @result)
        [:div {:class "py-20 text-center text-slate-400"} "No data"]

        :else
        (let [top-dominators (get @result :topDominators [])
              tree-id (str (get @result :treeId "dominator-tree"))]
          [:div {:class "bg-white border border-slate-200 rounded-xl shadow-sm p-4"}
           [:div {:class "mb-4"}
            [:h3 {:class "font-bold text-slate-700"} "Dominator Tree"]
            [:p {:class "text-sm text-slate-500"}
             (str "Total objects: " (get @result :totalObjects 0)
                  " | Total heap: " (get @result :totalHeapBytes 0) " bytes")]]
           (for [node top-dominators]
             ^{:key (:objectId node)}
             [dominator-tree-node node tree-id 0])])))))

(defn- reference-graph-tab []
  (let [object-id (r/atom "")
        result (rf/subscribe [:heapdump/analysis-result :reference-graph])
        loading? (rf/subscribe [:heapdump/detail-loading?])]
    (fn []
      [:div {:class "bg-white border border-slate-200 rounded-xl shadow-sm p-6"}
       [:h3 {:class "font-bold text-slate-700 mb-4"} "Reference Graph to GC Roots"]
       [:div {:class "flex gap-3 mb-6"}
        [:input {:type        "text"
                 :placeholder "Object ID"
                 :class       "flex-1 border border-slate-200 rounded-lg px-4 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                 :value       @object-id
                 :on-change   #(reset! object-id (-> % .-target .-value))}]
        [:button {:class    "btn-primary"
                  :on-click #(when (seq @object-id)
                               (rf/dispatch [:heapdump/run-analysis :reference-graph {:objectId @object-id :maxPaths 5}]))}
         "Trace"]]
       (when @loading?
         [:div {:class "py-10 flex justify-center"} [components/spinner]])
       (when-let [data @result]
         (if (empty? (:pathsToGcRoots data))
           [:div {:class "text-slate-400 py-4"} "No paths to GC roots found."]
           [:div {:class "space-y-4"}
            [:div {:class "text-sm text-slate-500"}
             "Target: " [:span {:class "font-mono font-semibold"} (:targetClassName data)]
             " #" [:span {:class "font-mono"} (:targetObjectId data)]]
            (for [[idx path] (map-indexed vector (:pathsToGcRoots data))]
              ^{:key idx}
              [:div {:class "border border-slate-200 rounded-lg p-4 bg-slate-50"}
               [:div {:class "font-semibold text-slate-700 mb-2"} (str "Path " (inc idx))]
               [:div {:class "space-y-1"}
                (for [[link-idx link] (map-indexed vector (tree-seq :children :children path))]
                  ^{:key link-idx}
                  [:div {:class "text-sm"}
                   [:span {:class "text-slate-400 mr-2"} (str (repeat (* 2 (:depth link 0)) "\u00A0"))]
                   [:span {:class "font-medium text-slate-600"} (:referenceType link)]
                   (when (seq (:fieldName link))
                     [:span {:class "text-slate-400 mx-1"} (str "(" (:fieldName link) ")")])
                   [:span {:class "font-mono text-xs text-slate-500 ml-1"} (:className link)]
                   [:span {:class "text-slate-400 text-xs ml-2"} (str "#" (:objectId link))]])]])]))])))

(defn- cross-analysis-tab []
  (let [cross (rf/subscribe [:heapdump/cross-analysis])
        loading? (rf/subscribe [:heapdump/detail-loading?])]
    (rf/dispatch [:heapdump/run-cross-analysis])
    (fn []
      (cond
        @loading?
        [:div {:class "py-20 flex flex-col items-center gap-4 text-slate-400"}
         [components/spinner]
         [:span "Running cross-analysis..."]]

        (nil? @cross)
        [:div {:class "py-20 text-center text-slate-400"} "No cross-analysis data available."]

        :else
        [:div {:class "space-y-6"}
         [:div {:class "bg-white border border-slate-200 rounded-xl p-6 shadow-sm"}
          [:h3 {:class "font-bold text-slate-700 mb-4"} "Summary"]
          [:div {:class "grid grid-cols-2 md:grid-cols-4 gap-4"}
           [:div {:class "text-center p-4 bg-red-50 rounded-lg border border-red-100"}
            [:div {:class "text-2xl font-bold text-red-600"} (get-in @cross [:summary :highSeverityCount])]
            [:div {:class "text-sm text-red-500 font-medium"} "High Severity"]]
           [:div {:class "text-center p-4 bg-amber-50 rounded-lg border border-amber-100"}
            [:div {:class "text-2xl font-bold text-amber-600"} (get-in @cross [:summary :mediumSeverityCount])]
            [:div {:class "text-sm text-amber-500 font-medium"} "Medium Severity"]]
           [:div {:class "text-center p-4 bg-blue-50 rounded-lg border border-blue-100"}
            [:div {:class "text-2xl font-bold text-blue-600"} (get-in @cross [:summary :lowSeverityCount])]
            [:div {:class "text-sm text-blue-500 font-medium"} "Low Severity"]]
           [:div {:class "text-center p-4 bg-slate-50 rounded-lg border border-slate-100"}
            [:div {:class "text-2xl font-bold text-slate-600"} (get-in @cross [:summary :totalClasses])]
            [:div {:class "text-sm text-slate-500 font-medium"} "Total Classes"]]]]

         [:div {:class "bg-white border border-slate-200 rounded-xl p-6 shadow-sm"}
          [:h3 {:class "font-bold text-slate-700 mb-2"} "Recommendation"]
          [:p {:class "text-slate-600"} (:recommendation @cross)]]

         [:div {:class "bg-white border border-slate-200 rounded-xl shadow-sm overflow-hidden"}
          [:div {:class "p-4 border-b border-slate-100 bg-slate-50"}
           [:h3 {:class "font-bold text-slate-700"} "Unified Class View"]]
          [:div {:class "overflow-auto"}
           [:table {:class "w-full text-sm text-left"}
            [:thead {:class "bg-slate-50 text-slate-500 font-medium"}
             [:tr
              [:th {:class "px-4 py-3"} "Severity"]
              [:th {:class "px-4 py-3"} "Class"]
              [:th {:class "px-4 py-3 text-right"} "JFR Samples"]
              [:th {:class "px-4 py-3 text-right"} "Heap Instances"]
              [:th {:class "px-4 py-3 text-right"} "Heap Retained"]]]
            [:tbody
             (for [entry (:classes @cross)]
               ^{:key (:className entry)}
               [:tr {:class "border-t border-slate-100 hover:bg-slate-50"}
                [:td {:class "px-4 py-3"}
                 [:span {:class (str "px-2 py-0.5 rounded-full text-xs font-bold "
                                     (case (:severity entry)
                                       "HIGH" "bg-red-50 text-red-600 border border-red-200"
                                       "MEDIUM" "bg-amber-50 text-amber-600 border border-amber-200"
                                       "bg-blue-50 text-blue-600 border border-blue-200"))}
                  (:severity entry)]]
                [:td {:class "px-4 py-3 font-mono text-xs"} (:className entry)]
                [:td {:class "px-4 py-3 text-right"} (str (:jfrSampleCount entry))]
                [:td {:class "px-4 py-3 text-right"} (str (:heapInstanceCount entry))]
                [:td {:class "px-4 py-3 text-right font-semibold"} (str (:heapRetainedSize entry))]])]]]]]))))

(defn heapdump-detail-page []
  (let [detail (rf/subscribe [:heapdump/detail])
        active-tab (rf/subscribe [:heapdump/active-tab])
        linked-recording-id (rf/subscribe [:heapdump/detail])]
    (fn []
      (let [info (:info @detail)
            linked-id (:linked-recording-id @detail)]
        [:div {:class "flex flex-col gap-6"}
         ;; Header
         [:div {:class "flex items-center justify-between"}
          [:div
           [:h1 {:class "text-3xl font-extrabold text-slate-900 tracking-tight"} (:fileName info)]
           [:div {:class "flex items-center gap-3 mt-2"}
            [:span {:class "px-2 py-0.5 rounded-full bg-emerald-50 text-emerald-600 text-xs font-bold border border-emerald-200"}
             (or (:format info) "HPROF")]
            [:span {:class "text-sm text-slate-500"}
             (str (js/Math.round (/ (:fileSize info) 1024 1024)) " MB")]
            (when linked-id
              [:button {:class    "px-2 py-0.5 rounded-full bg-blue-50 text-blue-600 text-xs font-bold border border-blue-200 hover:bg-blue-100 transition-colors"
                        :on-click #(rfe/push-state :recording-detail {:id linked-id})}
               [:i {:class "zmdi zmdi-link mr-1"}] "Linked Recording"])]]
          [:button {:class    "btn-outline"
                    :on-click #(rfe/push-state :library)}
           [:i {:class "zmdi zmdi-arrow-left mr-1"}] "Back to Library"]]

         ;; Tabs
         [:div {:class "flex gap-2 border-b border-slate-200 pb-1"}
          [tab-button "Overview" (= @active-tab :overview) #(rf/dispatch [:heapdump/select-tab :overview])]
          [tab-button "Class Histogram" (= @active-tab :class-histogram) #(rf/dispatch [:heapdump/select-tab :class-histogram])]
          [tab-button "Dominator Tree" (= @active-tab :dominator-tree) #(rf/dispatch [:heapdump/select-tab :dominator-tree])]
          [tab-button "Reference Graph" (= @active-tab :reference-graph) #(rf/dispatch [:heapdump/select-tab :reference-graph])]
          (when linked-id
            [tab-button "Cross Analysis" (= @active-tab :cross-analysis) #(rf/dispatch [:heapdump/select-tab :cross-analysis])])]

         ;; Tab Content
         (case @active-tab
           :overview [overview-tab]
           :class-histogram [class-histogram-tab]
           :dominator-tree [dominator-tree-tab]
           :reference-graph [reference-graph-tab]
           :cross-analysis [cross-analysis-tab]
           [overview-tab])]))))
)