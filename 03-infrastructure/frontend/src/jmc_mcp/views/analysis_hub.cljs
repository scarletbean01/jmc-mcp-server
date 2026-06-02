(ns jmc-mcp.views.analysis-hub
  (:require [clojure.string :as str]
            [re-com.core :as rc]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [reitit.frontend.easy :as rfe]
            [jmc-mcp.views.components :as components]
            ["react-chartjs-2" :refer [Line]]
            ["chart.js/auto"]))

;; ------------------------------------------------------------------
;; Copy to Clipboard Utilities
;; ------------------------------------------------------------------

(defn copy-button [data-rows columns]
  (let [copied? (r/atom false)]
    (fn [data-rows columns]
      [:div {:class "flex gap-2 items-center"}
       [:button {:class "text-slate-400 hover:text-blue-600 transition-colors text-xs font-bold flex items-center gap-1"
                 :title "Copy as CSV"
                 :on-click (fn []
                             (let [headers (map :label columns)
                                   csv-rows (map (fn [row] (str/join "," (map #(str (get row (:key %))) columns))) data-rows)
                                   content (str/join "\n" (cons (str/join "," headers) csv-rows))]
                               (.writeText js/navigator.clipboard content)
                               (reset! copied? true)
                               (js/setTimeout #(reset! copied? false) 2000)))}
        [:i {:class (str "zmdi text-sm " (if @copied? "zmdi-check text-emerald-500" "zmdi-copy"))}]
        "CSV"]
       [:span {:class "text-slate-350"} "|"]
       [:button {:class "text-slate-400 hover:text-blue-600 transition-colors text-xs font-bold flex items-center gap-1"
                 :title "Copy as Markdown"
                 :on-click (fn []
                             (let [headers (map :label columns)
                                   header-line (str "| " (str/join " | " headers) " |")
                                   separator-line (str "| " (str/join " | " (map (constantly "---") columns)) " |")
                                   md-rows (map (fn [row] (str "| " (str/join " | " (map #(str (get row (:key %))) columns)) " |")) data-rows)
                                   content (str/join "\n" (list* header-line separator-line md-rows))]
                               (.writeText js/navigator.clipboard content)
                               (reset! copied? true)
                               (js/setTimeout #(reset! copied? false) 2000)))}
        [:i {:class (str "zmdi text-sm " (if @copied? "zmdi-check text-emerald-500" "zmdi-copy"))}]
        "MD"]])))

;; ------------------------------------------------------------------
;; Helper Renderers
;; ------------------------------------------------------------------

(defn table-renderer [data columns]
  (let [rows (if (and (map? data) (:entries data)) (:entries data) data)]
    (if (empty? rows)
      [:div {:class "p-6 text-center text-slate-400 text-sm italic"} "No records found"]
      [:div {:class "bg-white border border-slate-200 rounded-xl shadow-sm overflow-hidden flex flex-col"}
       [:div {:class "px-6 py-3 border-b border-slate-100 flex justify-end bg-slate-50/50"}
        [copy-button rows columns]]
       [:div {:class "overflow-x-auto"}
        [:table {:class "w-full text-left border-collapse"}
         [:thead {:class "bg-slate-50 border-b border-slate-200"}
          [:tr
           (for [col columns]
             ^{:key (:key col)}
             [:th {:class "px-6 py-3.5 text-xs font-bold text-slate-500 uppercase tracking-wider"} (:label col)])]]
         [:tbody
          (for [[idx row] (map-indexed vector rows)]
            ^{:key (str idx)}
            [:tr {:class "border-t border-slate-100 hover:bg-slate-50/50 transition-colors"}
             (for [col columns]
               ^{:key (:key col)}
               [:td {:class "px-6 py-3.5 text-sm text-slate-700 font-medium truncate max-w-2xl"} (str (get row (:key col)))])])]]]])))

(defn timeline-renderer [data x-key y-key]
  (let [chart-data {:labels (map #(get % x-key) data)
                    :datasets [{:label (name y-key)
                                :data (map #(get % y-key) data)
                                :borderColor "rgb(59, 130, 246)"
                                :backgroundColor "rgba(59, 130, 246, 0.1)"
                                :fill true
                                :tension 0.2}]}]
    [:div {:class "bg-white border border-slate-200 rounded-xl p-6 shadow-sm"}
     [:div {:class "h-64 relative"}
      [:> Line {:data (clj->js chart-data)
                :options {:responsive true
                          :maintainAspectRatio false}}]]]))

;; ------------------------------------------------------------------
;; Color-Coded Interactive Flame Graph
;; ------------------------------------------------------------------

(defn get-frame-type [method-name]
  (cond
    (clojure.string/includes? method-name "io.github") :application
    (or (clojure.string/includes? method-name "org.openjdk")
        (clojure.string/includes? method-name "java.lang")
        (clojure.string/includes? method-name "java.util")
        (clojure.string/includes? method-name "sun.misc")) :jvm
    (or (clojure.string/includes? method-name "native")
        (clojure.string/includes? method-name "socket")
        (clojure.string/includes? method-name "file")) :native
    :else :library))

(defn frame-color-class [frame-type]
  (case frame-type
    :application "bg-blue-600 hover:bg-blue-700 text-white border-blue-500"
    :jvm "bg-slate-400 hover:bg-slate-500 text-white border-slate-350"
    :native "bg-rose-600 hover:bg-rose-700 text-white border-rose-550"
    :library "bg-amber-500 hover:bg-amber-600 text-slate-900 border-amber-450"))

(defn flame-graph-renderer [data title]
  (let [entries (cond
                  (and (map? data) (:hotMethods data)) (:hotMethods data)
                  (and (map? data) (:entries data)) (:entries data)
                  :else data)
        max-samples (or (when (seq entries) (apply max (map #(or (:samples %) (:sampleCount %) 1) entries))) 1)]
    [:div {:class "bg-slate-900 border border-slate-800 rounded-2xl p-6 shadow-md flex flex-col gap-5 text-white"}
     ;; Title & Legend
     [:div {:class "flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-800 pb-4"}
      [:div
       [:h3 {:class "text-sm font-bold uppercase tracking-wider text-slate-350"} title]
       [:span {:class "text-xs text-slate-500"} "Colored by domain type (Click to copy trace)"]]
      ;; Legend indicators
      [:div {:class "flex items-center gap-3 text-[10px] font-bold"}
       [:span {:class "flex items-center gap-1.5"} [:span {:class "w-3 h-3 rounded bg-blue-600 border border-blue-500"}] "Application"]
       [:span {:class "flex items-center gap-1.5"} [:span {:class "w-3 h-3 rounded bg-amber-500 border border-amber-450"}] "Library/Framework"]
       [:span {:class "flex items-center gap-1.5"} [:span {:class "w-3 h-3 rounded bg-slate-400 border border-slate-350"}] "JVM Internals"]
       [:span {:class "flex items-center gap-1.5"} [:span {:class "w-3 h-3 rounded bg-rose-600 border border-rose-550"}] "Native/OS"]]]
     
     (if (empty? entries)
       [:div {:class "py-20 text-center text-slate-500 italic text-sm"} "No profiling samples available"]
       [:div {:class "flex flex-col gap-2 font-mono text-[10px] max-h-[400px] overflow-auto pr-2"}
        (for [[idx item] (map-indexed vector (take 15 (sort-by #(or (:samples %) (:sampleCount %) 0) > entries)))]
          (let [item-samples (or (:samples item) (:sampleCount item) 0)
                pct (* 100 (/ item-samples max-samples))
                raw-stack (or (:stackTrace item) (:methodName item) "")
                short-method (or (first (clojure.string/split-lines raw-stack)) "Unknown Method")
                frame-type (get-frame-type short-method)
                color-class (frame-color-class frame-type)]
            ^{:key idx}
            [:div {:class (str "relative h-8 flex items-center justify-between px-4 rounded border cursor-pointer overflow-hidden transition-all group " color-class)
                   :title "Click to copy full trace"
                   :on-click (fn []
                               (.writeText js/navigator.clipboard raw-stack)
                               (js/alert "Stack/Method copied to clipboard!"))}
             ;; Background percent bar
             [:div {:class "absolute inset-y-0 left-0 bg-white/10"
                    :style {:width (str pct "%")}}]
             [:span {:class "relative font-semibold truncate max-w-xl group-hover:underline"} short-method]
             [:span {:class "relative font-bold opacity-80"} (str item-samples " samples")]]))])]))

;; ------------------------------------------------------------------
;; Guided Investigation Wizard
;; ------------------------------------------------------------------

(defn investigation-wizard []
  (let [scenario (r/atom :cpu)]
    (fn []
      (let [steps (case @scenario
                    :cpu [{:id 1 :txt "Examine System CPU Load for anomalies (user vs system spikes)."}
                          {:id 2 :txt "Inspect thread-cpu distribution to isolate heavy worker threads."}
                          {:id 3 :txt "Cross-reference hot methods call-stack paths."}
                          {:id 4 :txt "Check thread-starvation warnings for scheduler lag."}]
                    :memory [{:id 1 :txt "Analyze GC Pause durations and generations frequency."}
                             {:id 2 :txt "Inspect heap-trends metrics slope for leak signatures."}
                             {:id 3 :txt "Review predictive leak analysis output recommendations."}]
                    :locks [{:id 1 :txt "Inspect lock contention lists to identify hot monitors."}
                            {:id 2 :txt "Verify park durations and biased lock revocations."}
                            {:id 3 :txt "Confirm whether deadlock loops exist in the threads map."}]
                    :io [{:id 1 :txt "Review SQL JDBC ORM N+1 loop warning reports."}
                         {:id 2 :txt "Locate slow network or database socket write endpoints."}
                         {:id 3 :txt "Examine file read-write percentiles for I/O bounds."}])]
        [:div {:class "bg-white p-5 border border-slate-200 rounded-2xl shadow-sm flex flex-col gap-4"}
         [:div {:class "flex justify-between items-center"}
          [:div {:class "flex items-center gap-2"}
           [:i {:class "zmdi zmdi-compass text-lg text-blue-600"}]
           [:span {:class "text-sm font-bold text-slate-700"} "Guided Investigation Wizard"]]
          [:select {:class "bg-slate-50 border border-slate-200 rounded px-2 py-1 text-xs font-semibold outline-none focus:border-blue-500"
                    :value @scenario
                    :on-change #(reset! scenario (keyword (.. % -target -value)))}
           [:option {:value "cpu"} "High CPU Cycles"]
           [:option {:value "memory"} "Memory Leaks"]
           [:option {:value "locks"} "Lock Contention"]
           [:option {:value "io"} "I/O Saturation"]]]
         [:div {:class "flex flex-col gap-3"}
          (for [s steps]
            ^{:key (:id s)}
            [:div {:class "flex items-start gap-3 text-xs text-slate-650 font-medium"}
             [:div {:class "w-5 h-5 bg-blue-50 text-blue-600 rounded-full flex items-center justify-center font-bold flex-shrink-0"} (:id s)]
             [:span {:class "mt-0.5 leading-normal"} (:txt s)]])]]))))

;; ------------------------------------------------------------------
;; Narrative Engine (Copilot Story Generator)
;; ------------------------------------------------------------------
(defn generate-copilot-narrative [rules-data contention-data nplusone-data]
  (let [errors (filter #(= "Error" (:severity %)) (:rules rules-data))
        warnings (filter #(= "Warning" (:severity %)) (:rules rules-data))
        top-contention (first (:topContentions contention-data))
        nplusone-loop (when (:hasPatterns nplusone-data)
                        (or (:triggeringMethod (first (:patterns nplusone-data)))
                            "detected SQL loop"))]
    (cond
      ;; Scenario 1: Deadlocks / Contention & SQL Loops
      (and top-contention nplusone-loop)
      (str "During this recording, we detected high synchronization latency. Thread contention on `"
           (:monitorClass top-contention) "` was recorded (contention duration: " (:totalDuration top-contention) "). "
           "This correlates with SQL queries loop patterns found around `" nplusone-loop "`. "
           "Recommendation: Review transaction boundary scopes and ORM loading strategy (e.g. EAGER vs LAZY fetch).")

      ;; Scenario 2: Severe Errors
      (seq errors)
      (str "Critical performance issues detected! The rules engine flagged " (count errors) " P0 error(s). "
           "Specifically: \"" (:summary (first errors)) "\". "
           "Recommendation: Immediately inspect the Diagnostic view's CPU & Threads focus to trace the execution bottleneck.")

      ;; Scenario 3: Warnings
      (seq warnings)
      (str "Performance warnings identified. " (count warnings) " rules reported minor regressions. "
           "Rule Alert: \"" (:summary (first warnings)) "\". "
           "Recommendation: Follow the Guided Investigation Wizard to optimize young-generation garbage collection or reduce monitor waits.")

      ;; Scenario 4: Standard Clean Baseline
      :else
      "No critical CPU or memory anomalies detected. The system appears stable within standard baseline parameters. Recommended next steps: Monitor latency distributions and I/O percentiles for tail-end regressions.")))

(defn copilot-narrative-panel [rules contention nplusone]
  (let [narrative (generate-copilot-narrative (:data @rules) (:data @contention) (:data @nplusone))]
    [:div {:class "bg-gradient-to-r from-blue-600 to-indigo-700 text-white rounded-2xl p-6 shadow-md flex items-start gap-4 mb-4"}
     [:div {:class "w-12 h-12 rounded-xl bg-white/10 flex items-center justify-center flex-shrink-0 text-white"}
      [:i {:class "zmdi zmdi-face text-3xl"}]]
     [:div {:class "flex flex-col gap-1.5"}
      [:span {:class "text-xs font-black uppercase tracking-wider text-white/70"} "Performance Copilot Auto-Insight"]
      [:p {:class "text-sm leading-relaxed font-semibold font-sans"} narrative]]]))

;; ------------------------------------------------------------------
;; Time filter & header components
;; ------------------------------------------------------------------

(defn time-scrubber [duration]
  (let [params (rf/subscribe [:recording-detail/analysis-params])
        start (r/atom 0)
        end (r/atom (int duration))]
    (fn [duration]
      (let [p @params
            current-start (or (:start-time p) 0)
            current-end (or (:end-time p) duration)]
        [:div {:class "bg-white p-5 border border-slate-200 rounded-2xl shadow-sm flex flex-col lg:flex-row items-center gap-6 justify-between mb-8"}
         [:div {:class "flex items-center gap-3"}
          [:div {:class "w-10 h-10 rounded-full bg-blue-50 text-blue-600 flex items-center justify-center"}
           [:i {:class "zmdi zmdi-time text-xl"}]]
          [:div
           [:div {:class "text-xs font-bold text-slate-400 uppercase tracking-widest"} "Time Range Filter"]
           [:div {:class "text-sm font-semibold text-slate-700"}
            (str "Selected Segment: " current-start "s - " current-end "s / " (js/Math.round duration) "s total")]]]
         [:div {:class "flex items-center gap-3 w-full lg:w-auto"}
          [:input {:type "number" :min 0 :max duration :class "bg-slate-50 border border-slate-200 rounded-lg px-3 py-1.5 w-24 text-sm font-semibold outline-none focus:border-blue-500"
                   :value @start
                   :on-change #(reset! start (js/parseInt (.. % -target -value)))}]
          [:span {:class "text-slate-400 font-bold"} "to"]
          [:input {:type "number" :min 0 :max duration :class "bg-slate-50 border border-slate-200 rounded-lg px-3 py-1.5 w-24 text-sm font-semibold outline-none focus:border-blue-500"
                   :value @end
                   :on-change #(reset! end (js/parseInt (.. % -target -value)))}]
          [:button {:class "btn-primary !py-2 !text-xs uppercase tracking-wider"
                    :on-click #(rf/dispatch [:analysis/apply-time-filter @start @end])} "Apply"]
          [:button {:class "btn-outline !py-2 !text-xs uppercase tracking-wider"
                    :on-click #(do
                                 (reset! start 0)
                                 (reset! end (int duration))
                                 (rf/dispatch [:analysis/apply-time-filter nil nil]))} "Reset"]]]))))

;; ------------------------------------------------------------------
;; Tab 1: Copilot Dashboard View
;; ------------------------------------------------------------------

(defn health-card [title value subtitle icon status]
  (let [color-class (case status
                      :error "border-red-200 bg-red-50/20 text-red-700"
                      :warning "border-amber-200 bg-amber-50/20 text-amber-700"
                      :success "border-emerald-200 bg-emerald-50/20 text-emerald-700"
                      "border-slate-200 bg-slate-50/20 text-slate-700")]
    [:div {:class (str "bg-white p-5 rounded-2xl border border-slate-200 shadow-sm flex items-center justify-between transition-all hover:shadow-md hover:border-blue-100")}
     [:div {:class "flex flex-col gap-1"}
      [:span {:class "text-xs font-bold text-slate-400 uppercase tracking-widest"} title]
      [:span {:class "text-2xl font-black text-slate-800 tracking-tight"} value]
      [:span {:class "text-xs text-slate-500 font-medium"} subtitle]]
     [:div {:class (str "w-12 h-12 rounded-xl flex items-center justify-center border " color-class)}
      [:i {:class (str "zmdi text-2xl " icon)}]]]))

(defn copilot-health-panel [health-data]
  (let [cpu-load (some-> health-data :cpuLoad)
        mem-info (some-> health-data :physicalMemory)
        cpu-info (some-> health-data :cpuInfo)]
    [:div {:class "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"}
     [health-card "CPU Consumption" 
      (if-let [avg (:avgMachineTotal cpu-load)] (str avg "%") "N/A")
      (str "Max Total Peak: " (or (:maxMachineTotal cpu-load) "N/A") "%")
      "zmdi-cpu" (if (:highCpuDetected health-data) :warning :success)]
     [health-card "Physical Memory" 
      (if-let [max-mem (:maxUsed mem-info)] (str max-mem) "N/A")
      (str "Total Hardware: " (or (:totalSize mem-info) "N/A"))
      "zmdi-storage" :info]
     [health-card "JVM Hardware" 
      (if-let [cores (:cores cpu-info)] (str cores " Cores") "N/A")
      (str "Model: " (or (:cpu cpu-info) "N/A"))
      "zmdi-info-outline" :info]]))

(defn rule-alert-card [rule]
  (let [expanded? (r/atom false)
        severity (or (:severity rule) "Info")
        color-class (case (str/lower-case severity)
                      "error" {:border "border-red-200 hover:border-red-300"
                               :bg "bg-red-50/30"
                               :text "text-red-700"
                               :icon "zmdi-alert-circle text-red-500"}
                      "warning" {:border "border-amber-200 hover:border-amber-300"
                                 :bg "bg-amber-50/30"
                                 :text "text-amber-700"
                                 :icon "zmdi-alert-triangle text-amber-500"}
                      {:border "border-slate-200 hover:border-slate-300"
                       :bg "bg-slate-50/30"
                       :text "text-slate-700"
                       :icon "zmdi-info text-slate-500"})]
    (fn [rule]
      [:div {:class (str "rounded-2xl border bg-white shadow-sm overflow-hidden transition-all " (:border color-class))}
       [:div {:class "px-6 py-4 flex items-center justify-between cursor-pointer"
              :on-click #(swap! expanded? not)}
        [:div {:class "flex items-center gap-3.5"}
         [:i {:class (str "zmdi text-2xl " (:icon color-class))}]
         [:div
          [:span {:class "text-sm font-bold text-slate-800 leading-tight"} (:name rule)]
          [:div {:class "flex items-center gap-2 mt-0.5"}
           [:span {:class (str "px-2 py-0.5 rounded text-[10px] font-black uppercase tracking-wider " (:bg color-class) " " (:text color-class))}
            severity]]]]
        [:div {:class "flex items-center gap-2"}
         [:span {:class "text-xs font-bold text-slate-400"} (if @expanded? "Collapse" "Show Details")]
         [:i {:class (str "zmdi text-lg text-slate-400 transition-transform " (if @expanded? "zmdi-chevron-up" "zmdi-chevron-down"))}]]]
       (when @expanded?
         [:div {:class "px-6 pb-6 pt-2 border-t border-slate-100 flex flex-col gap-4 bg-slate-50/50"}
          [:div {:class "flex flex-col gap-1"}
           [:span {:class "text-xs font-bold text-slate-400 uppercase tracking-widest"} "Finding Summary"]
           [:p {:class "text-sm text-slate-700 leading-relaxed font-medium"} (:summary rule)]]
          (when-not (str/blank? (:explanation rule))
            [:div {:class "flex flex-col gap-1"}
             [:span {:class "text-xs font-bold text-slate-400 uppercase tracking-widest"} "Diagnostic Explanation"]
             [:p {:class "text-sm text-slate-650 leading-relaxed font-mono whitespace-pre-wrap bg-white border border-slate-100 rounded-xl p-4"} (:explanation rule)]])])])))

(defn copilot-dashboard-tab [recording-id]
  (let [health (rf/subscribe [:recording-detail/result :system-health])
        rules (rf/subscribe [:recording-detail/result :jfr-rules])
        contention (rf/subscribe [:recording-detail/result :thread-contention])
        nplusone (rf/subscribe [:recording-detail/result :jdbc-nplusone])
        loading? (rf/subscribe [:recording-detail/loading?])]
    (fn [recording-id]
      [:div {:class "flex flex-col gap-8"}
       (if @loading?
         [:div {:class "py-24 text-center text-slate-400 flex flex-col items-center gap-4"}
          [components/spinner]
          [:span "Aggregating copilot insights..."]]
         [:<>
          ;; Narrative Auto-Insight
          [copilot-narrative-panel rules contention nplusone]

          ;; Telemetry Stats
          [:div {:class "flex flex-col gap-4"}
           [:h2 {:class "text-lg font-bold text-slate-800 flex items-center gap-2"}
            [:i {:class "zmdi zmdi-graphic-eq text-blue-500"}] "Overall System Health Telemetry"]
           (if-let [health-data (:data @health)]
             [copilot-health-panel health-data]
             [:div {:class "p-6 bg-slate-50 border border-slate-200 rounded-xl text-center text-slate-400 text-sm"}
              "Health telemetry not available"])]

          ;; Dual column grid
          [:div {:class "grid grid-cols-1 lg:grid-cols-3 gap-8"}
           ;; Left (2/3 size): Rules & Alerts
           [:div {:class "lg:col-span-2 flex flex-col gap-4"}
            [:h2 {:class "text-lg font-bold text-slate-800 flex items-center gap-2"}
             [:i {:class "zmdi zmdi-shield-check text-blue-500"}] "Severity-Classified Diagnostic Findings"]
            (if-let [rules-data (:data @rules)]
              (let [all-rules (:rules rules-data)]
                (if (empty? all-rules)
                  [:div {:class "p-8 border border-dashed border-slate-200 bg-white rounded-2xl text-center text-slate-400"}
                   [:i {:class "zmdi zmdi-mood text-3xl mb-1 text-emerald-400"}]
                   [:div {:class "font-bold text-slate-600"} "All clear!"]
                   [:p {:class "text-xs mt-0.5"} "The JMC Rules engine did not identify any P0/P1 bottlenecks."]]
                  [:div {:class "flex flex-col gap-3"}
                   (for [[idx r] (map-indexed vector all-rules)]
                     ^{:key idx} [rule-alert-card r])]))
              [:div {:class "p-6 bg-slate-50 border border-slate-200 rounded-xl text-center text-slate-400 text-sm"}
               "JMC Rules findings not available"])]

           ;; Right (1/3 size): Guided Mode Checklist
           [:div {:class "flex flex-col gap-6"}
            [investigation-wizard]]]])])))

;; ------------------------------------------------------------------
;; Tab 2: Diagnostic Pre-Correlations View
;; ------------------------------------------------------------------

(defn diagnostic-focus-btn [id label icon active?]
  [:button {:class (str "flex items-center gap-2.5 px-6 py-3 border-b-2 font-semibold text-sm transition-all focus:outline-none "
                        (if active?
                          "border-blue-600 text-blue-600 bg-blue-50/40"
                          "border-transparent text-slate-500 hover:text-slate-800 hover:bg-slate-50"))
            :on-click #(rf/dispatch [:analysis/select-diagnostic-focus id])}
   [:i {:class (str "zmdi text-lg " icon)}]
   label])

(defn render-cpu-diagnostics []
  (let [thread-cpu @(rf/subscribe [:recording-detail/result :thread-cpu])
        hot-methods @(rf/subscribe [:recording-detail/result :hot-methods])
        thread-starve @(rf/subscribe [:recording-detail/result :thread-starvation])
        thread-cpu-data (:data thread-cpu)
        hot-methods-data (:data hot-methods)
        thread-starve-data (:data thread-starve)]
    [:div {:class "flex flex-col gap-8"}
     (when thread-starve-data
       [:div {:class "bg-amber-50 border border-amber-200 rounded-2xl p-6"}
        [:h3 {:class "text-amber-800 font-bold text-sm uppercase tracking-wider mb-2 flex items-center gap-2"}
         [:i {:class "zmdi zmdi-alert-triangle"}] "Thread Starvation Monitor"]
        [:p {:class "text-sm text-amber-700 leading-relaxed font-medium"}
         (str "Top starvation instances: " (or (:summary thread-starve-data) "No threads starving in this segment."))]])
     [:div {:class "grid grid-cols-1 lg:grid-cols-2 gap-8"}
      [:div {:class "flex flex-col gap-4"}
       [:h3 {:class "font-bold text-slate-700 text-sm uppercase tracking-wider"} "Top CPU Consuming Threads"]
       [table-renderer (:threads thread-cpu-data)
        [{:key :threadName :label "Thread Name"}
         {:key :samples :label "Samples"}
         {:key :cpuPercent :label "CPU %"}]]]
      [:div {:class "flex flex-col gap-4"}
       [:h3 {:class "font-bold text-slate-700 text-sm uppercase tracking-wider"} "Hot Methods Execution"]
       [table-renderer (:entries hot-methods-data)
        [{:key :sampleCount :label "Samples"}
         {:key :stackTrace :label "Trace Frame"}]]]]]))

(defn render-memory-diagnostics []
  (let [gc-detail-res @(rf/subscribe [:recording-detail/result :gc-detail])
        heap-trends-res @(rf/subscribe [:recording-detail/result :heap-trends])
        pred-leak-res @(rf/subscribe [:recording-detail/result :predictive-leak])
        gc-detail (:data gc-detail-res)
        heap-trends (:data heap-trends-res)
        pred-leak (:data pred-leak-res)]
    [:div {:class "flex flex-col gap-8"}
     (when pred-leak
       [:div {:class "bg-blue-50 border border-blue-100 rounded-2xl p-6 flex flex-col gap-2"}
        [:h3 {:class "text-blue-800 font-bold text-sm uppercase tracking-wider flex items-center gap-2"}
         [:i {:class "zmdi zmdi-help-outline"}] "Predictive Memory Leak Analysis"]
        [:p {:class "text-sm text-blue-700 font-medium leading-relaxed"}
         (str "Verdict: " (or (:verdict pred-leak) "Unknown")
              " | Heap Utilization: " (if (:heapUtilizationPct pred-leak) (str (js/Math.round (:heapUtilizationPct pred-leak)) "%") "N/A")
              " | Growth Rate: " (if (:growthRateKBPerMin pred-leak) (str (js/Math.round (:growthRateKBPerMin pred-leak)) " KB/min") "N/A")
              " | Rsquared: " (or (:rSquared pred-leak) "N/A"))]
        (when (seq (:leakSuspects pred-leak))
          [:div {:class "mt-4"}
           [:span {:class "text-xs font-bold text-blue-800 uppercase tracking-wider"} "Leak Suspect Classes:"]
           [:ul {:class "list-disc pl-5 text-xs text-blue-705 font-medium mt-1"}
            (for [suspect (:leakSuspects pred-leak)]
              ^{:key (:className suspect)}
              [:li (str (:className suspect) " (" (:sampleCount suspect) " samples, " (js/Math.round (:percentage suspect)) "%)")])]])])
     [:div {:class "grid grid-cols-1 lg:grid-cols-2 gap-8"}
      [:div {:class "flex flex-col gap-4"}
       [:h3 {:class "font-bold text-slate-700 text-sm uppercase tracking-wider"} "Heap Usage Trend"]
       (if (seq (:heapBuckets heap-trends))
         [timeline-renderer (:heapBuckets heap-trends) :bucketStartMs :maxBytes]
         [:div {:class "py-10 text-center text-slate-400 italic bg-white border rounded-xl text-sm"} "No usage trends events"])
       [:h3 {:class "font-bold text-slate-700 text-sm uppercase tracking-wider mt-4"} "Thread Count Profile"]
       (if (seq (:threadBuckets heap-trends))
         [timeline-renderer (:threadBuckets heap-trends) :bucketStartMs :avgCount]
         [:div {:class "py-10 text-center text-slate-400 italic bg-white border rounded-xl text-sm"} "No thread counts available"])]
      [:div {:class "flex flex-col gap-4"}
       [:h3 {:class "font-bold text-slate-700 text-sm uppercase tracking-wider"} "GC Pause & Heap Metrics"]
       [:div {:class "grid grid-cols-2 gap-4"}
        [:div {:class "bg-white p-4 rounded-xl border border-slate-200 flex flex-col gap-0.5"}
         [:span {:class "text-xs font-semibold text-slate-400"} "Young Pause Total"]
         [:span {:class "text-lg font-black text-slate-700"} (or (get-in gc-detail [:generationalSummary :youngTotalDuration]) "N/A")]]
        [:div {:class "bg-white p-4 rounded-xl border border-slate-200 flex flex-col gap-0.5"}
         [:span {:class "text-xs font-semibold text-slate-400"} "Old Pause Total"]
         [:span {:class "text-lg font-black text-slate-700"} (or (get-in gc-detail [:generationalSummary :oldTotalDuration]) "N/A")]]
        [:div {:class "bg-white p-4 rounded-xl border border-slate-200 flex flex-col gap-0.5"}
         [:span {:class "text-xs font-semibold text-slate-400"} "Average Heap Used"]
         [:span {:class "text-lg font-black text-slate-700"} (or (get-in gc-detail [:heapTrendSummary :avgHeapUsed]) "N/A")]]
        [:div {:class "bg-white p-4 rounded-xl border border-slate-200 flex flex-col gap-0.5"}
         [:span {:class "text-xs font-semibold text-slate-400"} "Maximum Heap Used"]
         [:span {:class "text-lg font-black text-slate-700"} (or (get-in gc-detail [:heapTrendSummary :maxHeapUsed]) "N/A")]]]
       [:h3 {:class "font-bold text-slate-700 text-sm uppercase tracking-wider mt-4"} "Young vs Old Generation Cycles"]
       [:div {:class "bg-white border rounded-xl overflow-hidden p-6"}
        [:ul {:class "space-y-2"}
         [:li {:class "flex justify-between font-medium text-sm text-slate-600"}
          [:span "Young GCs Run"] [:span (or (get-in gc-detail [:generationalSummary :youngCount]) 0)]]
         [:li {:class "flex justify-between font-medium text-sm text-slate-600"}
          [:span "Old GCs Run"] [:span (or (get-in gc-detail [:generationalSummary :oldCount]) 0)]]]]]]]))

(defn render-lock-diagnostics []
  (let [contention-res @(rf/subscribe [:recording-detail/result :thread-contention])
        locks-res @(rf/subscribe [:recording-detail/result :lock-analysis])
        deadlock-res @(rf/subscribe [:recording-detail/result :deadlock-detection])
        contention (:data contention-res)
        locks (:data locks-res)
        deadlock (:data deadlock-res)]
    [:div {:class "flex flex-col gap-8"}
     (when (seq (:deadlocks deadlock))
       (let [threads (mapcat :threads (:deadlocks deadlock))]
         [:div {:class "bg-red-50 border border-red-200 rounded-2xl p-6"}
          [:h3 {:class "text-red-800 font-bold text-sm uppercase tracking-wider mb-2 flex items-center gap-2"}
           [:i {:class "zmdi zmdi-flash"}] "CRITICAL DEADLOCK DETECTED"]
          [:p {:class "text-sm text-red-700 leading-relaxed font-bold"}
           (str "Deadlocked threads: " (clojure.string/join ", " threads))]]))
     [:div {:class "grid grid-cols-1 lg:grid-cols-2 gap-8"}
      [:div {:class "flex flex-col gap-4"}
       [:h3 {:class "font-bold text-slate-700 text-sm uppercase tracking-wider"} "Contended Locks Ranking"]
       [table-renderer (:topContentions contention)
        [{:key :totalDuration :label "Blocked Duration"}
         {:key :monitorClass :label "Monitor Type"}
         {:key :stackTrace :label "Block Location"}]]]
      [:div {:class "flex flex-col gap-4"}
       [:h3 {:class "font-bold text-slate-700 text-sm uppercase tracking-wider"} "Lock Park & Revocations Profile"]
       (if locks
         [:div {:class "flex flex-col gap-4"}
          [:div {:class "bg-white p-5 border rounded-xl flex flex-col gap-2"}
           [:span {:class "text-xs font-bold text-slate-400 uppercase tracking-widest"} "Revoked Lock Classes"]
           (if-let [top-classes (get-in locks [:biasedLockSummary :topClasses])]
             (for [c top-classes]
               ^{:key (:lockClass c)}
               [:div {:class "flex justify-between text-sm font-semibold text-slate-600 border-b border-slate-100 pb-1.5"}
                [:span {:class "truncate font-mono text-xs"} (:lockClass c)]
                [:span (:count c)]])
             [:span {:class "text-sm text-slate-400"} "No biased lock revocations"])]]
         [:div {:class "py-10 text-center text-slate-400 italic bg-white border rounded-xl text-sm"} "Lock stats not available"])]]]))

(defn render-io-diagnostics []
  (let [io-hotspots-res @(rf/subscribe [:recording-detail/result :io-hotspots])
        jdbc-nplusone-res @(rf/subscribe [:recording-detail/result :jdbc-nplusone])
        io-hotspots (:data io-hotspots-res)
        jdbc-nplusone (:data jdbc-nplusone-res)]
    [:div {:class "flex flex-col gap-8"}
     (if (:hasPatterns jdbc-nplusone)
       [:div {:class "bg-amber-50 border border-amber-200 rounded-2xl p-6 mb-6"}
        [:h3 {:class "text-amber-800 font-bold text-sm uppercase tracking-wider mb-2 flex items-center gap-2"}
         [:i {:class "zmdi zmdi-assignment-alert"}] "SQL N+1 Query Loops Detected"]
        [:p {:class "text-sm text-amber-700 leading-relaxed font-semibold mb-4"}
         (str (count (:patterns jdbc-nplusone)) " database N+1 loop patterns detected.")]
        [table-renderer (:patterns jdbc-nplusone)
         [{:key :triggeringMethod :label "Triggering Method"}
          {:key :totalReads :label "Total SQL Reads"}
          {:key :confidence :label "Confidence Score"}
          {:key :threadName :label "Execution Thread"}]]]
       [:div {:class "bg-emerald-50 border border-emerald-200 rounded-2xl p-6 mb-6"}
        [:h3 {:class "text-emerald-800 font-bold text-sm uppercase tracking-wider mb-2 flex items-center gap-2"}
         [:i {:class "zmdi zmdi-check-circle"}] "SQL N+1 Query Diagnostics"]
        [:p {:class "text-sm text-emerald-700 leading-relaxed font-medium"}
         "No database N+1 loop patterns detected. Your application's database access patterns look healthy."]])
     [:div {:class "grid grid-cols-1 lg:grid-cols-2 gap-8"}
      [:div {:class "flex flex-col gap-4"}
       [:h3 {:class "font-bold text-slate-700 text-sm uppercase tracking-wider"} "File I/O Bottlenecks"]
       [table-renderer (:fileEndpoints io-hotspots)
        [{:key :maxDuration :label "Duration (Max)"}
         {:key :count :label "Writes/Reads"}
         {:key :totalBytes :label "Bytes Count"}
         {:key :target :label "Filename"}]]
       [:h3 {:class "font-bold text-slate-700 text-sm uppercase tracking-wider mt-4"} "Socket Network Endpoints"]
       [table-renderer (:socketEndpoints io-hotspots)
        [{:key :maxDuration :label "Duration (Max)"}
         {:key :count :label "Transactions"}
         {:key :totalBytes :label "Data Sent"}
         {:key :target :label "IP/Port"}]]]
      [:div {:class "flex flex-col gap-4"}
       [:h3 {:class "font-bold text-slate-700 text-sm uppercase tracking-wider"} "I/O Percentiles distribution"]
       [table-renderer (:percentiles io-hotspots)
        [{:key :operation :label "Operation"}
         {:key :p50 :label "P50"}
         {:key :p95 :label "P95"}
         {:key :p99 :label "P99"}
         {:key :max :label "Max Latency"}]]]]]))

(defn diagnostics-panel []
  (let [active-focus (rf/subscribe [:recording-detail/diagnostic-focus])
        loading? (rf/subscribe [:recording-detail/loading?])]
    (fn []
      [:div {:class "flex flex-col gap-6"}
       ;; Diagnostic focus tabs
       [:div {:class "flex border-b border-slate-200 bg-white rounded-t-xl overflow-hidden"}
        [diagnostic-focus-btn :cpu "CPU & Threads" "zmdi-cpu" (= @active-focus :cpu)]
        [diagnostic-focus-btn :memory "Memory & GC" "zmdi-storage" (= @active-focus :memory)]
        [diagnostic-focus-btn :locks "Lock Contention" "zmdi-lock" (= @active-focus :locks)]
        [diagnostic-focus-btn :io "Database & I/O" "zmdi-globe" (= @active-focus :io)]]

       (if @loading?
         [:div {:class "py-24 text-center text-slate-400 flex flex-col items-center gap-4"}
          [components/spinner]
          [:span "Loading correlated diagnostics..."]]
         [:div {:class "p-6 bg-slate-50 border-x border-b border-slate-200 rounded-b-xl min-h-[400px]"}
          (case @active-focus
            :cpu [render-cpu-diagnostics]
            :memory [render-memory-diagnostics]
            :locks [render-lock-diagnostics]
            :io [render-io-diagnostics]
            [render-cpu-diagnostics])])])))

;; ------------------------------------------------------------------
;; Tab 3: Forensic Deep-Dive View
;; ------------------------------------------------------------------

(defn forensic-sidebar-item [focus label active?]
  [:div {:class (str "px-4 py-2 text-sm rounded-lg cursor-pointer transition-all duration-200 "
                     (if active? "bg-blue-50 text-blue-600 font-bold" "text-slate-600 hover:bg-slate-100"))
         :on-click #(rf/dispatch [:analysis/select-forensic-focus focus])}
   label])

(defn render-overview [data]
  [:div {:class "flex flex-col gap-6"}
   [:div {:class "grid grid-cols-2 md:grid-cols-4 gap-4"}
    [:div {:class "bg-white p-4 rounded-xl border border-slate-200 flex flex-col gap-0.5 shadow-sm"}
     [:span {:class "text-xs font-semibold text-slate-400"} "Total Events"]
     [:span {:class "text-lg font-black text-slate-700"} (:totalEvents data)]]
    [:div {:class "bg-white p-4 rounded-xl border border-slate-200 flex flex-col gap-0.5 shadow-sm"}
     [:span {:class "text-xs font-semibold text-slate-400"} "Filtered Events"]
     [:span {:class "text-lg font-black text-slate-700"} (or (:filteredEvents data) (:totalEvents data))]]
    [:div {:class "bg-white p-4 rounded-xl border border-slate-200 flex flex-col gap-0.5 shadow-sm"}
     [:span {:class "text-xs font-semibold text-slate-400"} "Duration"]
     [:span {:class "text-lg font-black text-slate-700"} (str (js/Math.round (:durationSeconds data)) "s")]]
    [:div {:class "bg-white p-4 rounded-xl border border-slate-200 flex flex-col gap-0.5 shadow-sm"}
     [:span {:class "text-xs font-semibold text-slate-400"} "File Name"]
     [:span {:class "text-lg font-black text-slate-700 truncate" :title (:filePath data)} 
      (if-let [path (:filePath data)]
        (last (str/split path #"/"))
        "N/A")]]]
   [:div {:class "grid grid-cols-1 lg:grid-cols-2 gap-8"}
    [:div {:class "flex flex-col gap-3"}
     [:h3 {:class "font-bold text-slate-700 text-sm uppercase tracking-wider"} "Event Counts"]
     (if (seq (:eventCounts data))
       [table-renderer (sort-by :count > (map (fn [[k v]] {:eventType (name k) :count v}) (:eventCounts data)))
        [{:key :eventType :label "Event Type"}
         {:key :count :label "Count"}]]
       [:p {:class "text-xs text-slate-400 italic"} "No event count statistics available"])]
    [:div {:class "flex flex-col gap-3"}
     [:h3 {:class "font-bold text-slate-700 text-sm uppercase tracking-wider"} "Event Availability"]
     (if (seq (:availability data))
       [table-renderer (sort-by :eventType (map (fn [[k v]] {:eventType (name k) :status v}) (:availability data)))
        [{:key :eventType :label "Event Type"}
         {:key :status :label "Status"}]]
       [:p {:class "text-xs text-slate-400 italic"} "No event availability data"])]]])

(defn forensic-panel [recording-id]
  (let [active-forensic (rf/subscribe [:recording-detail/forensic-focus])
        loading? (rf/subscribe [:recording-detail/loading?])
        forensic-categories
        [{:label "Diagnostics" :items [[:overview "Overview"]]}
         {:label "CPU & Performance" :items [[:hot-methods "Hot Methods"]
                                             [:cpu-flame "CPU Flame Graph"]
                                             [:call-tree "Call Tree"]]}
         {:label "Memory" :items [[:heap-trends "Heap Trends"]]}
         {:label "Exceptions" :items [[:exceptions "Exceptions"]]}]]
    (fn [recording-id]
      (let [focus @active-forensic
            result @(rf/subscribe [:recording-detail/result focus])
            data (:data result)]
        [:div {:class "flex-1 flex bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-sm min-h-[500px]"}
         ;; Sidebar selector
         [:div {:class "w-56 flex flex-col gap-6 p-6 border-r border-slate-100 bg-slate-50/50"}
          (for [cat forensic-categories]
            ^{:key (:label cat)}
            [:div {:class "flex flex-col gap-1.5"}
             [:div {:class "px-4 text-[10px] uppercase tracking-[0.15em] font-black text-slate-400 mb-1"} (:label cat)]
             (for [[focus-id label] (:items cat)]
               ^{:key focus-id} [forensic-sidebar-item focus-id label (= focus-id focus)])])]
         ;; Result panel
         [:div {:class "flex-1 p-8 overflow-auto"}
          (if @loading?
            [:div {:class "h-full flex flex-col items-center justify-center gap-4 text-slate-400"}
             [components/spinner]
             [:span "Gathering profiler metrics..."]]
            (if data
              (case focus
                :overview [render-overview data]
                :hot-methods [table-renderer (:entries data)
                              [{:key :sampleCount :label "Samples"}
                               {:key :stackTrace :label "Method frames"}]]
                :cpu-flame [flame-graph-renderer data "CPU Call Stacks Map"]
                :call-tree [:div {:class "text-center text-slate-500 italic p-10"} "Standard Call Tree. Refer to the Comparison A/B Call Tree or CPU Flame for visuals."]
                :heap-trends [timeline-renderer (:heapBuckets data) :bucketStartMs :maxBytes]
                :exceptions (if (:hasData data)
                              [table-renderer (:topExceptions data)
                               [{:key :className :label "Class"}
                                {:key :message :label "Message"}
                                {:key :count :label "Count"}]]
                              [:div {:class "text-center text-slate-400 italic p-10 bg-slate-50 border border-slate-200 border-dashed rounded-xl"} 
                               "No exceptions recorded in this interval"])
                [:div {:class "text-center text-slate-400 italic"} "No visualizer mapped"])
              [:div {:class "h-full flex flex-col items-center justify-center gap-2 text-slate-300"}
               [:i {:class "zmdi zmdi-search-for text-4xl"}]
               [:span {:class "text-lg font-medium"} "Select a profiling metric on the left to begin"]]))]]))))

;; ------------------------------------------------------------------
;; Main Page Controller
;; ------------------------------------------------------------------

(defn primary-tab-btn [id label icon active?]
  [:div {:class (str "flex items-center gap-2 px-6 py-3 cursor-pointer border-b-2 font-semibold text-sm transition-all "
                     (if active?
                       "border-blue-600 text-blue-600 bg-blue-50/50"
                       "border-transparent text-slate-500 hover:text-slate-800 hover:bg-slate-50"))
         :on-click #(rf/dispatch [:analysis/select-tab id])}
   [:i {:class (str "zmdi text-lg " icon)}]
   label])

(defn analysis-hub-page []
  (let [recording-id (rf/subscribe [:recording-detail/recording-id])
        info (rf/subscribe [:recording-detail/info])
        active-tab (rf/subscribe [:recording-detail/active-tab])]
    (fn []
      (let [rec-id @recording-id
            rec-info @info
            tab @active-tab]
        [:div {:class "flex flex-col gap-6 h-full"}
         ;; Detail page Header
         [:div {:class "flex items-center justify-between"}
          [:div {:class "flex items-center gap-4"}
           [:button {:class "btn-outline flex items-center justify-center w-10 h-10 !p-0"
                     :on-click #(rfe/push-state :library)}
            [:i {:class "zmdi zmdi-arrow-left text-xl"}]]
           [:div
            [:h1 {:class "text-3xl font-extrabold text-slate-900 tracking-tight flex items-center gap-2"}
             [:i {:class "zmdi zmdi-file-text text-blue-600"}]
             (or (:filename rec-info) "Recording Detail")]
            [:div {:class "flex items-center gap-3 text-xs text-slate-400 mt-1 font-semibold"}
             [:span {:class "flex items-center gap-1"}
              [:i {:class "zmdi zmdi-key"}] (str "ID: " rec-id)]
             (when rec-info
               [:<>
                [:span "•"]
                [:span {:class "flex items-center gap-1"}
                 [:i {:class "zmdi zmdi-storage"}] (str (js/Math.round (/ (:size rec-info) 1024 1024)) " MB")]
                [:span "•"]
                [:span {:class "flex items-center gap-1"}
                 [:i {:class "zmdi zmdi-time"}] (:uploadTime rec-info)]])]]]
          [:div {:class "flex gap-2"}
           [:button {:class "btn-outline flex items-center gap-2"}
            [:i {:class "zmdi zmdi-download"}] "Download JFR"]]]

         ;; Timeline scrubber filter
         (when rec-info
           [time-scrubber (:durationSeconds rec-info)])

         ;; Primary stratification tabs
         [:div {:class "flex border-b border-slate-200 mb-4 bg-white rounded-t-xl overflow-hidden border border-slate-200 border-b-0"}
          [primary-tab-btn :copilot "Copilot Dashboard" "zmdi-graduation-cap" (= tab :copilot)]
          [primary-tab-btn :diagnostics "Pre-Correlated Diagnostics" "zmdi-pulse" (= tab :diagnostics)]
          [primary-tab-btn :forensics "Forensic Profiler" "zmdi-search" (= tab :forensics)]]

         ;; Tab content panel
         [:div {:class "flex-1"}
          (case tab
            :copilot [copilot-dashboard-tab rec-id]
            :diagnostics [diagnostics-panel]
            :forensics [forensic-panel rec-id]
            [copilot-dashboard-tab rec-id])]]))))
