(ns jmc-mcp.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [jmc-mcp.config :as config]
            [jmc-mcp.events]
            [jmc-mcp.subs]
            [jmc-mcp.routes :as routes]
            [jmc-mcp.views.layout :as layout]))

(defn dev-setup []
  (when ^boolean goog.DEBUG
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (rf/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [layout/main-layout] root-el)))

(defn init []
  (rf/dispatch-sync [:initialize-db])
  (dev-setup)
  (routes/init-routes!)
  (mount-root))
