(ns jmc-mcp.views.components
  (:require [re-com.core :as rc]
            [re-frame.core :as rf]))

(defn notification-toast [notification]
  [rc/alert-box
   :id (:id notification)
   :alert-type (:type notification)
   :body (:message notification)
   :closeable? true
   :on-close #(rf/dispatch [:notification/remove (:id notification)])])

(defn notification-stack []
  (let [notifications (rf/subscribe [:notifications])]
    (fn []
      [rc/v-box
       :class "notification-stack"
       :style {:position "fixed" :top "20px" :right "20px" :z-index 1000 :gap "10px"}
       :children (for [n @notifications]
                   ^{:key (:id n)} [notification-toast n])])))

(defn spinner []
  [:i {:class "zmdi zmdi-refresh zmdi-hc-spin" :style {:font-size "24px"}}])
