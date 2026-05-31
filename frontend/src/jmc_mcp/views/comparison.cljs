(ns jmc-mcp.views.comparison
  (:require [clojure.string :as str]
            [jmc-mcp.views.components :as components]
            [re-frame.core :as rf]
            [reagent.core :as r]))

(defn comparison-header []
  (let [recordings (rf/subscribe [:recordings/items])
        baseline-id (r/atom nil)
        target-id (r/atom nil)
        loading? (rf/subscribe [:comparison/loading?])]
    (fn []
      [:div {:class "card p-8 flex flex-col md:flex-row items-end gap-6 mb-8"}
       [:div {:class "flex-1 flex flex-col gap-2"}
        [:label {:class "text-xs font-bold text-slate-400 uppercase tracking-widest"} "Baseline Recording"]
        [:select {:class "w-full bg-slate-50 border border-slate-200 rounded-lg px-4 py-2.5 focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all outline-none"
                  :value @baseline-id
                  :on-change #(reset! baseline-id (.. % -target -value))}
         [:option {:value ""} "Select baseline..."]
         (for [r @recordings]
           ^{:key (:id r)} [:option {:value (:id r)} (:filename r)])]]

       [:div {:class "flex items-center justify-center pb-2.5"}
        [:i {:class "zmdi zmdi-swap text-2xl text-slate-300"}]]

       [:div {:class "flex-1 flex flex-col gap-2"}
        [:label {:class "text-xs font-bold text-slate-400 uppercase tracking-widest"} "Target Recording"]
        [:select {:class "w-full bg-slate-50 border border-slate-200 rounded-lg px-4 py-2.5 focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all outline-none"
                  :value @target-id
                  :on-change #(reset! target-id (.. % -target -value))}
         [:option {:value ""} "Select target..."]
         (for [r @recordings]
           ^{:key (:id r)} [:option {:value (:id r)} (:filename r)])]]

       [:button {:class "btn-primary !py-2.5 px-8"
                 :disabled (or @loading? (not @baseline-id) (not @target-id))
                 :on-click (fn []
                             (rf/dispatch [:comparison/run {:baselineRecordingId @baseline-id
                                                            :comparisonRecordingId @target-id
                                                            :analysisType "overview"}])
                             (rf/dispatch [:comparison/run-call-tree {:baselineRecordingId @baseline-id
                                                                       :comparisonRecordingId @target-id
                                                                       :params {:subsystem "cpu"}}])
                             (rf/dispatch [:comparison/run-stack-traces {:baselineRecordingId @baseline-id
                                                                          :comparisonRecordingId @target-id
                                                                          :params {:topN 50}}]))}
        (if @loading? "Comparing..." "Run Full A/B Test")]])))

(defn recording-metadata [result]
  (let [b (:baseline result)
        t (:target result)]
    [:div {:class "grid grid-cols-1 md:grid-cols-2 gap-4 mb-8"}
     [:div {:class "bg-slate-100/50 rounded-xl p-4 border border-slate-200 flex items-center gap-4"}
      [:div {:class "w-10 h-10 bg-white rounded-lg flex items-center justify-center border border-slate-200 text-slate-400"}
       [:i {:class "zmdi zmdi-file-text text-xl"}]]
      [:div
       [:div {:class "text-[10px] uppercase font-bold text-slate-400 tracking-wider"} "Baseline"]
       [:div {:class "text-sm font-bold text-slate-700 truncate max-w-[300px]"} (:path b)]
       [:div {:class "text-xs text-slate-500"} (str "Duration: " (js/Math.round (:durationSeconds b)) "s")]]]
     [:div {:class "bg-blue-50 rounded-xl p-4 border border-blue-100 flex items-center gap-4"}
      [:div {:class "w-10 h-10 bg-white rounded-lg flex items-center justify-center border border-blue-100 text-blue-400"}
       [:i {:class "zmdi zmdi-file-text text-xl"}]]
      [:div
       [:div {:class "text-[10px] uppercase font-bold text-blue-400 tracking-wider"} "Target"]
       [:div {:class "text-sm font-bold text-blue-700 truncate max-w-[300px]"} (:path t)]
       [:div {:class "text-xs text-slate-500"} (str "Duration: " (js/Math.round (:durationSeconds t)) "s")]]]]))

(defn tab-button [id label icon active?]
  [:div {:class (str "flex items-center gap-2 px-6 py-3 cursor-pointer border-b-2 transition-all font-semibold text-sm "
                     (if active? "border-blue-600 text-blue-600 bg-blue-50/50" "border-transparent text-slate-500 hover:text-slate-800 hover:bg-slate-50"))
         :on-click #(rf/dispatch [:comparison/set-tab id])}
   [:i {:class (str "zmdi " icon)}]
   label])

(defn comparison-tabs []
  (let [active-tab (rf/subscribe [:comparison/active-tab])]
    [:div {:class "flex border-b border-slate-200 mb-8"}
     [tab-button :overview "Overview" "zmdi-view-dashboard" (= @active-tab :overview)]
     [tab-button :call-tree "Diff Call Tree" "zmdi-device-hub" (= @active-tab :call-tree)]
     [tab-button :stack-traces "Deep Hotspots" "zmdi-reorder-alt" (= @active-tab :stack-traces)]]))

(defn metric-row [metric]
  (let [delta (:deltaPercent metric)
        color-class (cond
                      (nil? delta) "text-slate-400"
                      (> delta 10) "text-red-500 font-bold"
                      (< delta -10) "text-emerald-500 font-bold"
                      :else "text-slate-600")]
    [:tr {:class "border-t border-slate-100 hover:bg-slate-50/50 transition-colors"}
     [:td {:class "px-6 py-4 text-sm text-slate-700 font-medium"} (:label metric)]
     [:td {:class "px-6 py-4 text-sm text-slate-600"} (:baselineDisplay metric)]
     [:td {:class "px-6 py-4 text-sm text-slate-600"} (:targetDisplay metric)]
     [:td {:class (str "px-6 py-4 text-sm " color-class)}
      (if delta (str delta "%") "-")]]))

(defn collapsible-card [id title icon header-extra content]
  (let [collapsed? (rf/subscribe [:comparison/collapsed-sections])]
    (fn []
      (let [is-collapsed? (contains? @collapsed? id)]
        [:div {:class (str "card flex flex-col transition-all " (if is-collapsed? "opacity-80" ""))}
         [:div {:class "px-6 py-4 bg-slate-50 border-b border-slate-100 flex items-center justify-between cursor-pointer group"
                :on-click #(rf/dispatch [:comparison/toggle-section id])}
          [:div {:class "flex items-center gap-3"}
           [:i {:class (str "zmdi text-blue-500 transition-transform duration-200 "
                             (if is-collapsed? "zmdi-chevron-right" "zmdi-chevron-down"))}]
           (when icon [:i {:class (str "zmdi " icon " text-slate-400 group-hover:text-blue-500 transition-colors")}])
           [:h3 {:class "font-bold text-slate-700 uppercase tracking-wider text-xs"} title]]
          [:div {:class "flex items-center gap-4"}
           header-extra
           [:span {:class "text-[10px] text-slate-400 font-bold group-hover:text-blue-500 transition-colors"}
            (if is-collapsed? "SHOW" "HIDE")]]]
         (when-not is-collapsed?
           [:div {:class "flex-1"} content])]))))

(defn trace-modal []
  (let [trace (rf/subscribe [:comparison/trace-modal])]
    (fn []
      (when @trace
        [:div {:class "fixed inset-0 z-50 flex items-center justify-center p-4 sm:p-6"}
         [:div {:class "absolute inset-0 bg-slate-900/60 backdrop-blur-sm"
                :on-click #(rf/dispatch [:comparison/close-trace-modal])}]
         [:div {:class "relative bg-white rounded-3xl shadow-2xl w-full max-w-4xl max-h-[85vh] flex flex-col overflow-hidden"}
          [:div {:class "px-8 py-6 border-b border-slate-100 flex items-center justify-between bg-slate-50/50"}
           [:div {:class "flex items-center gap-3"}
            [:div {:class "w-10 h-10 bg-blue-100 rounded-xl flex items-center justify-center text-blue-600"}
             [:i {:class "zmdi zmdi-format-align-left text-xl"}]]
            [:div
             [:h3 {:class "text-lg font-bold text-slate-900"} "Full Stack Trace"]
             [:p {:class "text-xs text-slate-500 font-medium uppercase tracking-wider"} "Detailed Execution Context"]]]
           [:button {:class "w-10 h-10 flex items-center justify-center rounded-full hover:bg-slate-200 transition-colors text-slate-400"
                     :on-click #(rf/dispatch [:comparison/close-trace-modal])}
            [:i {:class "zmdi zmdi-close text-2xl"}]]]
          [:div {:class "flex-1 overflow-auto p-8 bg-slate-900"}
           [:pre {:class "text-slate-300 font-mono text-sm leading-relaxed"}
            (for [[i line] (map-indexed vector (str/split-lines @trace))]
              ^{:key i}
              [:div {:class "hover:bg-slate-800 px-2 py-0.5 rounded transition-colors"}
               [:span {:class "text-slate-600 mr-4 select-none inline-block w-8 text-right"} (inc i)]
               [:span line]])]]
          [:div {:class "px-8 py-4 border-t border-slate-100 bg-slate-50 flex justify-end"}
           [:button {:class "btn-primary" :on-click #(rf/dispatch [:comparison/close-trace-modal])} "Close"]]]]))))

(defn delta-table [id title icon deltas _ show-trace?]
  (when (seq deltas)
    [collapsible-card id title icon
     [:span {:class "text-[10px] bg-slate-200 text-slate-600 px-2 py-0.5 rounded-full font-black"} "TOP DELTAS"]
     [:div {:class "overflow-x-auto"}
      [:table {:class "w-full text-left border-collapse"}
       [:thead
        [:tr
         [:th {:class "px-6 py-3 text-[10px] font-bold text-slate-400 uppercase tracking-widest"} "Key / Item"]
         [:th {:class "px-6 py-3 text-[10px] font-bold text-slate-400 uppercase tracking-widest"} "Baseline"]
         [:th {:class "px-6 py-3 text-[10px] font-bold text-slate-400 uppercase tracking-widest"} "Target"]
         [:th {:class "px-6 py-3 text-[10px] font-bold text-slate-400 uppercase tracking-widest"} "Delta"]
         (when show-trace? [:th {:class "px-6 py-3 text-[10px] font-bold text-slate-400 uppercase tracking-widest"} "Action"])]]
       [:tbody
        (for [d deltas]
          ^{:key (:key d)}
          [:tr {:class "border-t border-slate-100 hover:bg-slate-50/50 transition-colors"}
           [:td {:class "px-6 py-4 text-xs text-slate-700 font-mono font-medium leading-relaxed max-w-md break-words"}
            (let [lines (str/split-lines (:key d))]
              [:div
               (first lines)
               (when (> (count lines) 1)
                 [:div {:class "text-[10px] text-slate-400 italic mt-1"} (str "+ " (dec (count lines)) " more frames")])])]
           [:td {:class "px-6 py-4 text-sm text-slate-600"} (:baselineDisplay d)]
           [:td {:class "px-6 py-4 text-sm text-slate-600"} (:targetDisplay d)]
           [:td {:class (str "px-6 py-4 text-sm font-bold " (if (pos? (:delta d)) "text-red-500" "text-emerald-500"))}
            (:deltaDisplay d)]
           (when show-trace?
             [:td {:class "px-6 py-4 text-sm"}
              [:button {:class "text-blue-600 hover:text-blue-800 font-bold text-xs uppercase tracking-tight flex items-center gap-1 group/btn"
                        :on-click #(rf/dispatch [:comparison/open-trace-modal (:key d)])}
               [:i {:class "zmdi zmdi-eye text-sm transition-transform group-hover/btn:scale-110"}]
               "View Trace"]])])]]]]))

(defn rule-change-row [change is-regression?]
  [:tr {:class "border-t border-slate-100 hover:bg-slate-50/50 transition-colors"}
   [:td {:class "px-6 py-4 text-sm text-slate-700 font-medium"} (:rule change)]
   [:td {:class "px-6 py-4 text-sm"}
    [:span {:class (str "px-2 py-1 rounded text-xs font-bold "
                        (case (:baselineSeverity change)
                          "Error" "bg-red-100 text-red-700"
                          "Warning" "bg-amber-100 text-amber-700"
                          "bg-slate-100 text-slate-600"))}
     (:baselineSeverity change)]]
   [:td {:class "px-6 py-4 text-sm"}
    [:span {:class (str "px-2 py-1 rounded text-xs font-bold "
                        (case (:targetSeverity change)
                          "Error" "bg-red-100 text-red-700"
                          "Warning" "bg-amber-100 text-amber-700"
                          "bg-slate-100 text-slate-600"))}
     (:targetSeverity change)]]
   [:td {:class "px-6 py-4 text-sm"}
    [:i {:class (str "zmdi text-lg " (if is-regression? "zmdi-trending-up text-red-500" "zmdi-trending-down text-emerald-500"))}]]])

(defn rule-changes-panel [rules]
  (let [regressions (:regressions rules)
        improvements (:improvements rules)]
    (when (or (seq regressions) (seq improvements))
      [:div {:class "flex flex-col gap-6"}
       [:h2 {:class "text-xl font-bold text-slate-800 flex items-center gap-2"}
        [:i {:class "zmdi zmdi-shield-check text-blue-500"}] "Rule Analysis"]
       [:div {:class "grid grid-cols-1 lg:grid-cols-2 gap-6"}
        (when (seq regressions)
          [collapsible-card :regressions "Regressions" "zmdi-alert-triangle"
           [:span {:class "text-[10px] bg-red-100 text-red-600 px-2 py-0.5 rounded-full font-black"} "URGENT"]
           [:table {:class "w-full text-left"}
            [:thead
             [:tr
              [:th {:class "px-6 py-3 text-[10px] font-bold text-red-400 uppercase tracking-widest"} "Rule"]
              [:th {:class "px-6 py-3 text-[10px] font-bold text-red-400 uppercase tracking-widest"} "From"]
              [:th {:class "px-6 py-3 text-[10px] font-bold text-red-400 uppercase tracking-widest"} "To"]
              [:th {:class "px-6 py-3"}]]]
            [:tbody
             (for [r regressions]
               ^{:key (:rule r)} [rule-change-row r true])]]])

        (when (seq improvements)
          [collapsible-card :improvements "Improvements" "zmdi-mood"
           [:span {:class "text-[10px] bg-emerald-100 text-emerald-600 px-2 py-0.5 rounded-full font-black"} "GOOD"]
           [:table {:class "w-full text-left"}
            [:thead
             [:tr
              [:th {:class "px-6 py-3 text-[10px] font-bold text-emerald-400 uppercase tracking-widest"} "Rule"]
              [:th {:class "px-6 py-3 text-[10px] font-bold text-emerald-400 uppercase tracking-widest"} "From"]
              [:th {:class "px-6 py-3 text-[10px] font-bold text-emerald-400 uppercase tracking-widest"} "To"]
              [:th {:class "px-6 py-3"}]]]
            [:tbody
             (for [i improvements]
               ^{:key (:rule i)} [rule-change-row i false])]]])]])))

(defn overview-tab [result]
  [:div {:class "flex flex-col gap-10"}
   (when-let [warnings (seq (:warnings result))]
     [:div {:class "bg-amber-50 border border-amber-200 rounded-2xl p-6 flex flex-col gap-2"}
      [:h3 {:class "text-amber-800 font-bold text-sm uppercase tracking-wider flex items-center gap-2"}
       [:i {:class "zmdi zmdi-alert-circle"}] "Analysis Warnings"]
      [:ul {:class "space-y-1"}
       (for [w warnings]
         ^{:key w}
         [:li {:class "text-amber-700 text-sm"} (str "• " w)])]])

   (when-let [summary (:summary result)]
     [:div {:class "bg-blue-50 border border-blue-100 rounded-2xl p-6"}
      [:h3 {:class "text-blue-800 font-bold text-sm uppercase tracking-wider mb-3"} "Executive Summary"]
      [:ul {:class "space-y-2"}
       (for [s summary]
         ^{:key s}
         [:li {:class "flex items-start gap-2 text-blue-700 text-sm font-medium"}
          [:i {:class "zmdi zmdi-chevron-right mt-0.5"}]
          s])]])

   [rule-changes-panel (:ruleChanges result)]

   [:div {:class "flex flex-col gap-6"}
    [:h2 {:class "text-xl font-bold text-slate-800 flex items-center gap-2"}
     [:i {:class "zmdi zmdi-chart text-blue-500"}] "Key Performance Indicators"]
    [:div {:class "grid grid-cols-1 md:grid-cols-2 gap-6"}
     (for [[cat metrics] (:metrics result)]
       ^{:key cat}
       [collapsible-card (str "kpi-" (name cat)) (name cat) nil
        [:span {:class "text-[10px] bg-blue-100 text-blue-600 px-2 py-0.5 rounded-full font-black"} "METRICS"]
        [:div {:class "overflow-x-auto"}
         [:table {:class "w-full text-left border-collapse"}
          [:thead
           [:tr
            [:th {:class "px-6 py-3 text-[10px] font-bold text-slate-400 uppercase tracking-widest"} "Metric"]
            [:th {:class "px-6 py-3 text-[10px] font-bold text-slate-400 uppercase tracking-widest"} "Baseline"]
            [:th {:class "px-6 py-3 text-[10px] font-bold text-slate-400 uppercase tracking-widest"} "Target"]
            [:th {:class "px-6 py-3 text-[10px] font-bold text-slate-400 uppercase tracking-widest"} "Delta"]]]
          [:tbody
           (for [m metrics]
             ^{:key (:label m)} [metric-row m])]]]])]]

   [:div {:class "flex flex-col gap-6"}
    [:h2 {:class "text-xl font-bold text-slate-800 flex items-center gap-2"}
     [:i {:class "zmdi zmdi-flash text-blue-500"}] "Top Hotspots & Shifts"]
    [:div {:class "grid grid-cols-1 gap-8"}
     [delta-table :cpu-hotspots "CPU Hotspots" "zmdi-cpu" (:cpuDeltas result) "text-blue-500" true]
     [delta-table :allocation-hotspots "Allocations" "zmdi-memory" (:allocationDeltas result) "text-amber-500" false]
     [delta-table :contention-hotspots "Lock Contention" "zmdi-lock" (:contentionDeltas result) "text-purple-500" true]
     [delta-table :exception-hotspots "Exceptions" "zmdi-bug" (:exceptionDeltas result) "text-red-500" false]]]])

(defn diff-call-tree-node [tree-id n level expanded-nodes loading-nodes]
  (let [node-id (:nodeId n)
        is-expanded? (contains? expanded-nodes node-id)
        is-loading? (contains? loading-nodes node-id)]
    [:<>
     [:tr {:class (str "border-t border-slate-100 hover:bg-slate-50/50 transition-colors "
                       (when is-expanded? "bg-slate-50/30"))}
      [:td {:class "px-6 py-3.5 text-sm font-mono text-slate-700 flex items-center"
            :style {:padding-left (str (+ 24 (* level 20)) "px")}}
       (if (:hasChildren n)
         [:div {:class "flex items-center gap-1 mr-1"}
          [:div {:class "w-6 h-6 flex items-center justify-center cursor-pointer hover:bg-slate-200 rounded transition-colors"
                 :on-click #(rf/dispatch [:comparison/toggle-node tree-id node-id])}
           (if is-loading?
             [:i {:class "zmdi zmdi-spinner zmdi-hc-spin text-blue-400"}]
             [:i {:class (str "zmdi text-blue-500 " (if is-expanded? "zmdi-chevron-down" "zmdi-chevron-right"))}])]
          [:div {:class "w-6 h-6 flex items-center justify-center cursor-pointer hover:bg-slate-200 rounded transition-colors text-slate-400 hover:text-blue-500"
                 :title "Expand All Children"
                 :on-click #(rf/dispatch [:comparison/expand-all-under tree-id node-id])}
           [:i {:class "zmdi zmdi-unfold-more text-xs"}]]]
         [:div {:class "w-6 h-6 flex items-center justify-center mr-1"}
          [:i {:class "zmdi zmdi-circle text-[6px] text-slate-300"}]])
       [:span {:class (str "truncate " (if is-expanded? "font-bold text-slate-900" ""))} (:methodName n)]]
      [:td {:class "px-6 py-3.5 text-sm text-slate-500 font-medium"} (str (:baselinePct n) "%")]
      [:td {:class "px-6 py-3.5 text-sm text-slate-500 font-medium"} (str (:targetPct n) "%")]
      [:td {:class (str "px-6 py-3.5 text-sm font-black text-right "
                        (case (:changeType n)
                          "REGRESSION" "text-red-500"
                          "IMPROVEMENT" "text-emerald-500"
                          "text-slate-600"))}
       (str (if (pos? (:delta n)) "+" "") (js/Math.round (:delta n)) "%")]]

     (when is-expanded?
       (for [child (:children n)]
         ^{:key (:nodeId child)}
         [diff-call-tree-node tree-id child (inc level) expanded-nodes loading-nodes]))]))

(defn diff-call-tree-tab []
  (let [tree (rf/subscribe [:comparison/diff-call-tree])
        expanded (rf/subscribe [:comparison/expanded-nodes])
        loading (rf/subscribe [:comparison/loading-nodes])]
    (fn []
      (if-let [data @tree]
        [:div {:class "flex flex-col gap-6"}
         [:div {:class "flex items-center justify-between"}
          [:div
           [:h2 {:class "text-xl font-bold text-slate-800"} "Differential Call Tree"]
           [:p {:class "text-sm text-slate-500"} (str "Comparing execution time flow for " (:subsystem data) " events")]]
          [:div {:class "flex items-center gap-4"}
           [:div {:class "flex bg-slate-100 rounded-lg p-1"}
            [:button {:class "px-3 py-1.5 text-xs font-bold text-slate-600 hover:bg-white hover:shadow-sm rounded-md transition-all flex items-center gap-1.5"
                      :on-click #(rf/dispatch [:comparison/expand-all (:treeId data)])}
             [:i {:class "zmdi zmdi-unfold-more"}] "Expand All"]
            [:button {:class "px-3 py-1.5 text-xs font-bold text-slate-600 hover:bg-white hover:shadow-sm rounded-md transition-all flex items-center gap-1.5"
                      :on-click #(rf/dispatch [:comparison/collapse-all])}
             [:i {:class "zmdi zmdi-unfold-less"}] "Collapse All"]]
           [:div {:class "text-right"}
            [:div {:class "text-xs font-bold text-slate-400 uppercase tracking-widest"} "Package Filter"]
            [:div {:class "text-sm font-mono text-slate-700"} (or (:packageFilter data) "None")]]]]

         [:div {:class "card"}
          [:div {:class "overflow-x-auto"}
           [:table {:class "w-full text-left border-collapse"}
            [:thead {:class "bg-slate-50 border-b border-slate-200"}
             [:tr
              [:th {:class "px-6 py-3 text-[10px] font-bold text-slate-400 uppercase tracking-widest"} "Method"]
              [:th {:class "px-6 py-3 text-[10px] font-bold text-slate-400 uppercase tracking-widest"} "Baseline %"]
              [:th {:class "px-6 py-3 text-[10px] font-bold text-slate-400 uppercase tracking-widest"} "Target %"]
              [:th {:class "px-6 py-3 text-[10px] font-bold text-slate-400 uppercase tracking-widest text-right"} "Delta"]]]
            [:tbody
             (for [n (:nodes data)]
               ^{:key (:nodeId n)}
               [diff-call-tree-node (:treeId data) n 0 @expanded @loading])]]]]]
        [:div {:class "py-32 text-center text-slate-400 italic"} "Run comparison to see call tree data"]))))

(defn method-diff-table [id title icon description methods]
  (when (seq methods)
    [collapsible-card id title icon
     [:span {:class "text-[10px] bg-slate-200 text-slate-600 px-2 py-0.5 rounded-full font-black"} "DEEP-DIVE"]
     [:div {:class "flex flex-col"}
      [:div {:class "px-6 py-3 bg-slate-50/50 border-b border-slate-100 text-xs text-slate-500 italic"}
       description]
      [:div {:class "overflow-x-auto"}
       [:table {:class "w-full text-left"}
        [:thead {:class "bg-slate-50/30 border-b border-slate-100"}
         [:tr
          [:th {:class "px-6 py-3 text-[10px] font-bold text-slate-400 uppercase tracking-widest"} "Method"]
          [:th {:class "px-6 py-3 text-[10px] font-bold text-slate-400 uppercase tracking-widest text-right"} "Delta %"]]]
        [:tbody
         (for [m methods]
           ^{:key (:methodName m)}
           [:tr {:class "border-t border-slate-100 hover:bg-slate-50/50 transition-colors"}
            [:td {:class "px-6 py-3 text-xs font-mono text-slate-700 truncate max-w-xl"} (:methodName m)]
            [:td {:class (str "px-6 py-3 text-xs font-bold text-right "
                              (if (pos? (:pctChange m)) "text-red-500" "text-emerald-500"))}
             (str (if (pos? (:pctChange m)) "+" "") (js/Math.round (:pctChange m)) "%")]])]]]]]))

(defn diff-stack-traces-tab []
  (let [traces (rf/subscribe [:comparison/diff-stack-traces])]
    (fn []
      (if-let [data @traces]
        [:div {:class "flex flex-col gap-10"}
         [:div {:class "flex items-center justify-between"}
          [:div
           [:h2 {:class "text-xl font-bold text-slate-800"} "Hotspot Deep-Dive"]
           [:p {:class "text-sm text-slate-500"} "Flattened method-level analysis comparing execution frequency and performance shifts"]]
          [:div {:class "flex gap-8 text-right"}
           [:div
            [:div {:class "text-[10px] font-bold text-slate-400 uppercase tracking-widest"} "Baseline Samples"]
            [:div {:class "text-lg font-black text-slate-700"} (:baselineTotalSamples data)]]
           [:div
            [:div {:class "text-[10px] font-bold text-blue-400 uppercase tracking-widest"} "Target Samples"]
            [:div {:class "text-lg font-black text-blue-700"} (:targetTotalSamples data)]]]]

         [:div {:class "grid grid-cols-1 lg:grid-cols-2 gap-8"}
          [:div {:class "flex flex-col gap-8"}
           [method-diff-table :deep-regressions "Significant Regressions" "zmdi-trending-up text-red-500"
            "Methods present in both recordings that show a substantial increase in execution time."
            (:changedMethods data)]
           [method-diff-table :deep-new "New Hotspots" "zmdi-plus-circle text-red-500"
            "Performance bottlenecks detected in the target recording that were absent in the baseline."
            (:newMethods data)]]
          [:div {:class "flex flex-col gap-8"}
           [method-diff-table :deep-improvements "Significant Improvements" "zmdi-trending-down text-emerald-500"
            "Methods that show a substantial decrease in execution frequency, indicating potential optimizations."
            (sort-by :pctChange (filter #(neg? (:pctChange %)) (:changedMethods data)))]
           [method-diff-table :deep-eliminated "Eliminated Hotspots" "zmdi-minus-circle text-emerald-500"
            "Bottlenecks that were significant in the baseline but have been removed or reduced in the target."
            (:disappearedMethods data)]]]]
        [:div {:class "py-32 text-center text-slate-400 italic"} "Run comparison to see hotspot data"]))))

(defn comparison-page []
  (let [result (rf/subscribe [:comparison/result])
        active-tab (rf/subscribe [:comparison/active-tab])
        loading? (rf/subscribe [:comparison/loading?])]
    (fn []
      [:div {:class "flex flex-col gap-8"}
       [:div
        [:h1 {:class "text-3xl font-extrabold text-slate-900 tracking-tight"} "Comparison Tool"]
        [:p {:class "text-slate-500 mt-1 text-lg"} "Deep A/B analysis of regressions and performance shifts"]]

       [comparison-header]

       (if @loading?
         [:div {:class "py-32 flex flex-col items-center justify-center gap-4 text-slate-400"}
          [components/spinner]
          [:span "Running comprehensive comparison tests..."]]
         (if @result
           [:div {:class "flex flex-col"}
            [recording-metadata @result]
            [comparison-tabs]
            (case @active-tab
              :overview [overview-tab @result]
              :call-tree [diff-call-tree-tab]
              :stack-traces [diff-stack-traces-tab]
              [overview-tab @result])]
           [:div {:class "py-32 flex flex-col items-center justify-center gap-4 text-slate-300 border-2 border-dashed border-slate-200 rounded-3xl"}
            [:i {:class "zmdi zmdi-compare text-6xl text-slate-100"}]
            [:div {:class "text-center"}
             [:div {:class "text-xl font-bold text-slate-400"} "Ready to compare"]
             [:p {:class "text-sm text-slate-400 mt-1"} "Select two recordings from the library to perform a deep A/B test"]]]))
       [trace-modal]])))
