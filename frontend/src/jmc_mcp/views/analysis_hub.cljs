(ns jmc-mcp.views.analysis-hub
  (:require [re-com.core :as rc]
            [re-frame.core :as rf]
            [jmc-mcp.views.components :as components]
            ["react-chartjs-2" :refer [Line]]
            ["chart.js/auto"]))

(defn timeline-renderer [data x-key y-key]
  (let [chart-data {:labels (map #(get % x-key) data)
                    :datasets [{:label (name y-key)
                                :data (map #(get % y-key) data)
                                :borderColor "rgb(75, 192, 192)"
                                :tension 0.1}]}]
    [:div {:style {:padding "20px" :background-color "white" :border "1px solid #e2e8f0" :border-radius "8px"}}
     [:> Line {:data (clj->js chart-data)}]]))

(defn flame-graph-renderer [data]
  [rc/v-box
   :align :center
   :style {:padding "40px" :background-color "white" :border "1px solid #e2e8f0" :border-radius "8px"}
   :children [[rc/label :label "Flame Graph (SVG Implementation)"]
              [:svg {:width "100%" :height "400"}
               [:rect {:x "0" :y "0" :width "100%" :height "400" :fill "#f8fafc"}]
               [rc/label :label "SVG Flame Graph goes here" :style {:margin-top "180px"}]]]])

(def analysis-config
  {:overview         {:label "Overview" :shape :metrics :sections [:jvm :cpu :memory :gc]}
   :hot-methods      {:label "Hot Methods" :shape :table :columns [{:key :methodName :label "Method"}
                                                                   {:key :sampleCount :label "Samples"}]}
   :cpu-flame        {:label "CPU Flame Graph" :shape :flame-graph :depth-key :stackDepth :value-key :sampleCount}
   :call-tree        {:label "Call Tree" :shape :tree :children-key :children :label-key :methodName}
   :heap-trends      {:label "Heap Trends" :shape :timeline :x-key :timestamp :y-key :heapUsed}
   :exceptions       {:label "Exceptions" :shape :table :columns [{:key :type :label "Type"}
                                                                 {:key :message :label "Message"}
                                                                 {:key :count :label "Count"}]}})

(def categories
  [{:label "Overview" :items [:overview]}
   {:label "CPU & Compilation" :items [:hot-methods :cpu-flame :call-tree]}
   {:label "Memory" :items [:heap-trends]}
   {:label "Diagnostics" :items [:exceptions]}])

(defn analysis-sidebar-item [type]
  (let [config (get analysis-config type)
        active-type (rf/subscribe [:recording-detail/active-analysis])]
    (fn []
      (let [active? (= @active-type type)]
        [:div {:class (str "px-4 py-2 text-sm rounded-lg cursor-pointer transition-all duration-200 "
                           (if active? "bg-blue-50 text-blue-600 font-bold" "text-slate-600 hover:bg-slate-100"))
               :on-click #(rf/dispatch [:analysis/select-type type])}
         (:label config)]))))

(defn analysis-sidebar []
  [:div {:class "w-56 flex flex-col gap-6 p-6 border-r border-slate-100 bg-slate-50/50"}
   (for [cat categories]
     ^{:key (:label cat)}
     [:div {:class "flex flex-col gap-1.5"}
      [:div {:class "px-4 text-[10px] uppercase tracking-[0.15em] font-black text-slate-400 mb-1"} (:label cat)]
      (for [item (:items cat)]
        ^{:key item} [analysis-sidebar-item item])])])

(defn metrics-renderer [data]
  [:div {:class "grid grid-cols-1 gap-8"}
   (for [[section metrics] data]
     ^{:key section}
     [:div {:class "flex flex-col gap-4"}
      [:div {:class "text-xs font-bold text-slate-500 uppercase tracking-widest px-1"} (name section)]
      (if (map? metrics)
        [:div {:class "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4"}
         (for [[k v] metrics]
           ^{:key k}
           [:div {:class "bg-white p-5 rounded-xl border border-slate-200 shadow-sm flex flex-col gap-1 hover:border-blue-200 transition-colors"}
            [:span {:class "text-xs font-semibold text-slate-400"} (name k)]
            [:span {:class "text-xl font-black text-slate-800 break-all"} (str v)]])]
        [:div {:class "bg-white p-5 rounded-xl border border-slate-200 shadow-sm"}
         [:span {:class "text-xl font-black text-slate-800"} (str metrics)]])])])

(defn table-renderer [data columns]
  [:div {:class "bg-white border border-slate-200 rounded-xl shadow-sm overflow-hidden"}
   [:div {:class "overflow-x-auto"}
    [:table {:class "w-full text-left border-collapse"}
     [:thead {:class "bg-slate-50 border-bottom border-slate-200"}
      [:tr
       (for [col columns]
         ^{:key (:key col)}
         [:th {:class "px-6 py-4 text-xs font-bold text-slate-500 uppercase tracking-wider"} (:label col)])]]
     [:tbody
      (for [row data]
        ^{:key (str row)}
        [:tr {:class "border-t border-slate-100 hover:bg-slate-50/50 transition-colors"}
         (for [col columns]
           ^{:key (:key col)}
           [:td {:class "px-6 py-4 text-sm text-slate-700 font-medium"} (str (get row (:key col)))])])]]]])

(defn result-panel []
  (let [active-type (rf/subscribe [:recording-detail/active-analysis])
        result (rf/subscribe [:recording-detail/result @active-type])
        loading? (rf/subscribe [:recording-detail/loading?])]
    (fn []
      [:div {:class "flex-1 p-8 overflow-auto min-h-[600px]"}
       (if @loading?
         [:div {:class "h-full flex flex-col items-center justify-center gap-4 text-slate-400"}
          [components/spinner]
          [:span "Running analysis..."]]
         (if-let [data (:data @result)]
           (let [config (get analysis-config @active-type)]
             [(case (:shape config)
                :metrics [metrics-renderer data]
                :table [table-renderer data (:columns config)]
                :timeline [timeline-renderer data (:x-key config) (:y-key config)]
                :flame-graph [flame-graph-renderer data]
                [:div {:class "p-12 text-center text-slate-400 italic"}
                 (str "Renderer for " (:shape config) " not implemented")])])
           [:div {:class "h-full flex flex-col items-center justify-center gap-2 text-slate-300"}
            [:i {:class "zmdi zmdi-search-for text-4xl"}]
            [:span {:class "text-lg font-medium"} "No data available for this analysis"]]))])))

(defn analysis-hub-page []
  (let [recording-id (:id @(rf/subscribe [:route/params]))]
    [:div {:class "flex flex-col gap-6 h-full"}
     [:div {:class "flex items-center justify-between"}
      [:div
       [:h1 {:class "text-3xl font-extrabold text-slate-900 tracking-tight"} "Analysis Hub"]
       [:p {:class "text-slate-500 mt-1 font-medium"} (str "Recording ID: " recording-id)]]
      [:div {:class "flex gap-2"}
       [:button {:class "btn-outline flex items-center gap-2"}
        [:i {:class "zmdi zmdi-download"}] "Export JFR"]
       [:button {:class "btn-primary flex items-center gap-2"}
        [:i {:class "zmdi zmdi-play"}] "Run All Rules"]]]

     [:div {:class "flex-1 flex bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-md"}
      [analysis-sidebar]
      [result-panel]]]))
