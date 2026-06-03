(ns jmc-mcp.views.layout
  (:require [re-com.core :as rc]
            [re-frame.core :as rf]
            [reitit.frontend.easy :as rfe]
            [jmc-mcp.views.components :as components]
            [jmc-mcp.views.library :as library]
            [jmc-mcp.views.analysis-hub :as analysis-hub]
            [jmc-mcp.views.heapdump-detail :as heapdump-detail]
            [jmc-mcp.views.comparison :as comparison]
            [jmc-mcp.views.job-monitor :as job-monitor]))

(defn nav-item [label route-name icon]
  (let [current-route (rf/subscribe [:route/current])]
    (fn []
      (let [active? (= @current-route route-name)]
        [:div {:class (str "flex items-center gap-3 px-6 py-3 cursor-pointer transition-all duration-200 group "
                           (if active? "bg-blue-600/10 text-blue-600 border-r-4 border-blue-600" "text-slate-400 hover:bg-slate-800 hover:text-white"))
               :on-click #(rfe/push-state route-name)}
         [:i {:class (str "zmdi " icon) :style {:font-size "20px"}}]
         [:span {:class "text-sm font-semibold"} label]]))))

(defn sidebar []
  [:div {:class "w-64 bg-slate-900 border-r border-slate-800 h-full flex flex-col"}
   [:div {:class "p-8 mb-4"}
    [:div {:class "text-white text-xl font-bold tracking-tight"} "JMC MCP"]
    [:div {:class "text-slate-500 text-xs uppercase tracking-widest mt-1 font-semibold"} "Analytics Dashboard"]]
   [:div {:class "flex-1"}
    [nav-item "Library" :library "zmdi-collection-item"]
    [nav-item "Comparison" :compare "zmdi-compare"]]])

(defn main-content []
  (let [current-route (rf/subscribe [:route/current])]
    (fn []
      [:div {:class "flex-1 overflow-auto p-10 bg-slate-50"}
       [:div {:class "max-w-6xl mx-auto"}
        (case @current-route
          :library [library/library-page]
          :recording-detail [analysis-hub/analysis-hub-page]
          :heapdump-detail [heapdump-detail/heapdump-detail-page]
          :compare [comparison/comparison-page]
          [:div {:class "text-slate-400 italic"} "Page not found"])]])))

(defn main-layout []
  [:div {:class "flex h-screen w-screen overflow-hidden"}
   [sidebar]
   [main-content]
   [components/notification-stack]
   [job-monitor/job-monitor]])
