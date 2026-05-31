(ns jmc-mcp.routes
  (:require [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]))

(def routes
  ["/"
   [""
    {:name :library}]
   ["recordings/:id"
    {:name :recording-detail}]
   ["compare"
    {:name :compare}]])

(defn on-navigate [new-match]
  (when new-match
    (re-frame.core/dispatch [:route/changed new-match])))

(defn init-routes! []
  (rfe/start!
   (rf/router routes)
   on-navigate
   {:use-fragment true}))
