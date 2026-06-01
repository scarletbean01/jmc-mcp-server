(ns jmc-mcp.fx
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]))

(rf/reg-fx
 :sse/connect
 (fn [{:keys [url on-message on-error]}]
   (let [source (js/EventSource. url)]
     (set! (.-onmessage source)
           (fn [event]
             (let [data (js/JSON.parse (.-data event))]
               (rf/dispatch (conj on-message (js->clj data :keywordize-keys true))))))
     (set! (.-onerror source)
           (fn [event]
             (rf/dispatch (conj on-error event))))
     ;; Store source in a global atom if we need to close it later
     (swap! (rf/subscribe [:sse/sources]) assoc url source))))

(rf/reg-fx
 :notify
 (fn [{:keys [type message]}]
   (rf/dispatch [:notification/add {:type type :message message}])))
