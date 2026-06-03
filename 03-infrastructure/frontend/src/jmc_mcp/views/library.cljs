(ns jmc-mcp.views.library
  (:require [clojure.string :as str]
            [re-com.core :as rc]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [reitit.frontend.easy :as rfe]
            [jmc-mcp.views.components :as components]))

(defn upload-zone []
  (let [uploading? (rf/subscribe [:upload/status])]
    (fn []
      [:div {:class "relative group border-2 border-dashed border-slate-300 rounded-2xl p-12 bg-white flex flex-col items-center gap-4 transition-all duration-300 hover:border-blue-500 hover:bg-blue-50/50"}
       (if (:uploading? @uploading?)
         [:div {:class "flex flex-col items-center gap-4"}
          [components/spinner]
          [:span {:class "text-slate-500 font-medium"} (str "Uploading... " (:progress @uploading?) "%")]]
         [:<>
          [:div {:class "w-16 h-16 bg-slate-100 rounded-full flex items-center justify-center group-hover:bg-blue-100 transition-colors"}
           [:i {:class "zmdi zmdi-upload text-3xl text-slate-400 group-hover:text-blue-600"}]]
          [:div {:class "text-center"}
           [:div {:class "text-lg font-bold text-slate-700"} "Drop JFR or HPROF files here or click to upload"]
           [:div {:class "text-sm text-slate-400 mt-1"} "Supported: .jfr, .hprof, .phd"]]
          [:button {:class "btn-primary mt-2"} "Select File"]])
       [:input {:type "file"
                :accept ".jfr,.hprof,.phd"
                :class "absolute inset-0 opacity-0 cursor-pointer"
                :on-change (fn [e]
                             (let [file (-> e .-target .-files (aget 0))]
                               (when file
                                 (let [fname (.-name file)
                                       ext (str/lower-case (last (str/split fname #"\.")))]
                                   (cond
                                     (= ext "jfr") (rf/dispatch [:upload/submit file])
                                     (#{"hprof" "phd"} ext) (rf/dispatch [:heapdump/upload file])
                                     :else (js/alert "Unsupported file type. Please upload .jfr, .hprof, or .phd files."))))))}]])))

(defn recording-row [recording]
  (let [heap-dump-id (get-in recording [:availability :heapDumpId])
        heap-dumps (rf/subscribe [:heapdumps/items])
        show-link-menu (r/atom false)]
    (fn [recording]
      [:div {:class "bg-white border border-slate-200 rounded-xl overflow-hidden group hover:ring-2 hover:ring-blue-500/20 transition-all shadow-sm"}
       [:div {:class "p-6 flex items-center justify-between"}
        [:div {:class "flex items-center gap-5"}
         [:div {:class "w-12 h-12 bg-slate-100 rounded-lg flex items-center justify-center text-slate-400 group-hover:bg-blue-50 group-hover:text-blue-500 transition-colors"}
          [:i {:class "zmdi zmdi-file-text text-2xl"}]]
         [:div
          [:div {:class "text-lg font-bold text-slate-800 group-hover:text-blue-600 transition-colors flex items-center gap-2"}
           (:filename recording)
           (when heap-dump-id
             [:span {:class "px-2 py-0.5 rounded-full bg-emerald-50 text-emerald-600 text-xs font-bold border border-emerald-200"}
              "Heap"])]
          [:div {:class "flex items-center gap-3 text-sm text-slate-500 mt-0.5"}
           [:span {:class "flex items-center gap-1"}
            [:i {:class "zmdi zmdi-storage"}] (str (js/Math.round (/ (:size recording) 1024 1024)) " MB")]
           [:span {:class "text-slate-300"} "•"]
           [:span {:class "flex items-center gap-1"}
            [:i {:class "zmdi zmdi-time"}] (:uploadTime recording)]]]]
        [:div {:class "flex items-center gap-2 opacity-0 group-hover:opacity-100 transition-opacity"}
         (when-not heap-dump-id
           [:div {:class "relative"}
            [:button {:class "btn-outline !py-1.5 !text-sm flex items-center gap-1"
                      :on-click #(swap! show-link-menu not)}
             [:i {:class "zmdi zmdi-link"}] "Link Heap Dump"]
            (when @show-link-menu
              [:div {:class "absolute right-0 top-full mt-2 w-64 bg-white border border-slate-200 rounded-xl shadow-lg z-50 py-2 max-h-60 overflow-auto"}
               (if (empty? @heap-dumps)
                 [:div {:class "px-4 py-2 text-sm text-slate-400"} "No heap dumps available"]
                 (for [hd @heap-dumps]
                   ^{:key (:heapDumpId hd)}
                   [:button {:class "w-full text-left px-4 py-2 text-sm text-slate-700 hover:bg-blue-50 hover:text-blue-600 transition-colors"
                             :on-click (fn []
                                         (rf/dispatch [:heapdump/link-recording (:heapDumpId hd) (:id recording)])
                                         (reset! show-link-menu false))}
                    (:fileName hd)]))])])
         [:button {:class "btn-primary !py-1.5 !text-sm"
                   :on-click #(rfe/push-state :recording-detail {:id (:id recording)})} "Analyze"]
         [:button {:class "btn-outline border-red-200 text-red-500 hover:bg-red-50 !py-1.5 !text-sm"
                   :on-click (fn []
                               (when (js/confirm "Are you sure you want to delete this recording?")
                                 (rf/dispatch [:library/delete-recording (:id recording)])))} "Delete"]]]])))

(defn heapdump-row [heapdump]
  [:div {:class "bg-white border border-slate-200 rounded-xl overflow-hidden group hover:ring-2 hover:ring-emerald-500/20 transition-all shadow-sm"}
   [:div {:class "p-6 flex items-center justify-between"}
    [:div {:class "flex items-center gap-5"}
     [:div {:class "w-12 h-12 bg-slate-100 rounded-lg flex items-center justify-center text-slate-400 group-hover:bg-emerald-50 group-hover:text-emerald-500 transition-colors"}
      [:i {:class "zmdi zmdi-memory text-2xl"}]]
     [:div
      [:div {:class "text-lg font-bold text-slate-800 group-hover:text-emerald-600 transition-colors"} (:fileName heapdump)]
      [:div {:class "flex items-center gap-3 text-sm text-slate-500 mt-0.5"}
       [:span {:class "flex items-center gap-1"}
        [:i {:class "zmdi zmdi-storage"}] (str (js/Math.round (/ (:fileSize heapdump) 1024 1024)) " MB")]
       [:span {:class "text-slate-300"} "•"]
       [:span {:class "flex items-center gap-1"}
        [:i {:class "zmdi zmdi-time"}] (:uploadTime heapdump)]
       [:span {:class "text-slate-300"} "•"]
       [:span {:class "flex items-center gap-1"}
        [:i {:class "zmdi zmdi-format"}] (or (:format heapdump) "HPROF")]
       (when (pos? (:objectCount heapdump))
         [:<>
          [:span {:class "text-slate-300"} "•"]
          [:span {:class "flex items-center gap-1"}
           [:i {:class "zmdi zmdi-layers"}] (str (:objectCount heapdump) " objects")]])]]]
    [:div {:class "flex items-center gap-2 opacity-0 group-hover:opacity-100 transition-opacity"}
     [:button {:class "btn-outline border-red-200 text-red-500 hover:bg-red-50 !py-1.5 !text-sm"
               :on-click (fn []
                           (when (js/confirm "Are you sure you want to delete this heap dump?")
                             (rf/dispatch [:heapdump/delete (:heapDumpId heapdump)])))} "Delete"]]]])

(defn library-page []
  (let [recordings (rf/subscribe [:recordings/items])
        recordings-loading? (rf/subscribe [:recordings/loading?])
        heapdumps (rf/subscribe [:heapdumps/items])
        heapdumps-loading? (rf/subscribe [:heapdumps/loading?])]
    (rf/dispatch [:library/load-recordings])
    (rf/dispatch [:heapdump/load-list])
    (fn []
      [:div {:class "flex flex-col gap-8"}
       ;; Header
       [:div {:class "flex items-center justify-between"}
        [:div
         [:h1 {:class "text-3xl font-extrabold text-slate-900 tracking-tight"} "Recording Library"]
         [:p {:class "text-slate-500 mt-1 text-lg"} "Manage and analyze your JFR recordings and heap dumps"]]
        [:button {:class "btn-outline flex items-center gap-2"
                  :on-click #(do (rf/dispatch [:library/load-recordings])
                                 (rf/dispatch [:heapdump/load-list]))}
         [:i {:class "zmdi zmdi-refresh"}] "Refresh"]]

       [upload-zone]

       ;; Recordings Section
       [:div {:class "flex flex-col gap-4"}
        [:h2 {:class "text-xl font-bold text-slate-800"} "JFR Recordings"]
        (if @recordings-loading?
          [:div {:class "py-20 flex flex-col items-center gap-4 text-slate-400"}
           [components/spinner]
           [:span "Loading recordings..."]]
          (if (empty? @recordings)
            [:div {:class "py-20 flex flex-col items-center gap-3 text-slate-400 border-2 border-dashed border-slate-200 rounded-2xl bg-white"}
             [:i {:class "zmdi zmdi-inbox text-5xl text-slate-200"}]
             [:span {:class "text-lg font-medium"} "No recordings found"]
             [:p {:class "text-sm"} "Upload a JFR file to get started"]]
            [:div {:class "grid gap-4"}
             (for [r @recordings]
               ^{:key (:id r)} [recording-row r])]))]

       ;; Heap Dumps Section
       [:div {:class "flex flex-col gap-4"}
        [:h2 {:class "text-xl font-bold text-slate-800"} "Heap Dumps"]
        (if @heapdumps-loading?
          [:div {:class "py-20 flex flex-col items-center gap-4 text-slate-400"}
           [components/spinner]
           [:span "Loading heap dumps..."]]
          (if (empty? @heapdumps)
            [:div {:class "py-20 flex flex-col items-center gap-3 text-slate-400 border-2 border-dashed border-slate-200 rounded-2xl bg-white"}
             [:i {:class "zmdi zmdi-memory text-5xl text-slate-200"}]
             [:span {:class "text-lg font-medium"} "No heap dumps found"]
             [:p {:class "text-sm"} "Upload an HPROF file to get started"]]
            [:div {:class "grid gap-4"}
             (for [hd @heapdumps]
               ^{:key (:heapDumpId hd)} [heapdump-row hd])]))]])))
