(ns jmc-mcp.views.library
  (:require [re-com.core :as rc]
            [re-frame.core :as rf]
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
           [:div {:class "text-lg font-bold text-slate-700"} "Drop JFR files here or click to upload"]
           [:div {:class "text-sm text-slate-400 mt-1"} "Max recording size: 100MB"]]
          [:button {:class "btn-primary mt-2"} "Select File"]])
       [:input {:type "file"
                :accept ".jfr"
                :class "absolute inset-0 opacity-0 cursor-pointer"
                :on-change (fn [e]
                             (let [file (-> e .-target .-files (aget 0))]
                               (when file
                                 (rf/dispatch [:upload/submit file]))))}]])))

(defn recording-row [recording]
  [:div {:class "bg-white border border-slate-200 rounded-xl overflow-hidden group hover:ring-2 hover:ring-blue-500/20 transition-all shadow-sm"}
   [:div {:class "p-6 flex items-center justify-between"}
    [:div {:class "flex items-center gap-5"}
     [:div {:class "w-12 h-12 bg-slate-100 rounded-lg flex items-center justify-center text-slate-400 group-hover:bg-blue-50 group-hover:text-blue-500 transition-colors"}
      [:i {:class "zmdi zmdi-file-text text-2xl"}]]
     [:div
      [:div {:class "text-lg font-bold text-slate-800 group-hover:text-blue-600 transition-colors"} (:filename recording)]
      [:div {:class "flex items-center gap-3 text-sm text-slate-500 mt-0.5"}
       [:span {:class "flex items-center gap-1"}
        [:i {:class "zmdi zmdi-storage"}] (str (js/Math.round (/ (:size recording) 1024 1024)) " MB")]
       [:span {:class "text-slate-300"} "•"]
       [:span {:class "flex items-center gap-1"}
        [:i {:class "zmdi zmdi-time"}] (:uploadTime recording)]]]]
    [:div {:class "flex items-center gap-2 opacity-0 group-hover:opacity-100 transition-opacity"}
     [:button {:class "btn-primary !py-1.5 !text-sm"
               :on-click #(rfe/push-state :recording-detail {:id (:id recording)})} "Analyze"]
     [:button {:class "btn-outline border-red-200 text-red-500 hover:bg-red-50 !py-1.5 !text-sm"
               :on-click (fn []
                           (when (js/confirm "Are you sure you want to delete this recording?")
                             (rf/dispatch [:library/delete-recording (:id recording)])))} "Delete"]]]])

(defn library-page []
  (let [recordings (rf/subscribe [:recordings/items])
        loading? (rf/subscribe [:recordings/loading?])]
    (rf/dispatch [:library/load-recordings])
    (fn []
      [:div {:class "flex flex-col gap-8"}
       [:div {:class "flex items-center justify-between"}
        [:div
         [:h1 {:class "text-3xl font-extrabold text-slate-900 tracking-tight"} "Recording Library"]
         [:p {:class "text-slate-500 mt-1 text-lg"} "Manage and analyze your JFR recordings"]]
        [:button {:class "btn-outline flex items-center gap-2"
                  :on-click #(rf/dispatch [:library/load-recordings])}
         [:i {:class "zmdi zmdi-refresh"}] "Refresh"]]

       [upload-zone]

       (if @loading?
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
              ^{:key (:id r)} [recording-row r])]))])))
