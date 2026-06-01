(ns jmc-mcp.views.job-monitor
  (:require [re-com.core :as rc]
            [re-frame.core :as rf]))

(defn job-item [job-id job]
  [rc/h-box
   :align :center
   :style {:padding "8px 12px" :border-bottom "1px solid #f1f5f9" :gap "12px"}
   :children [[rc/v-box :size "1" :children
               [[rc/label :label (str "Job: " (name (:analysisType job))) :style {:font-weight 600}]
                [rc/label :label (str "Status: " (:status job)) :style {:font-size "12px"}]]]
              [rc/progress-bar
               :model (:progressPercent job)
               :width "100px"]]])

(defn job-monitor []
  (let [jobs (rf/subscribe [:jobs/all])]
    (fn []
      (when (seq @jobs)
        [rc/v-box
         :style {:position "fixed" :bottom "20px" :right "20px" :width "300px"
                 :background-color "white" :border "1px solid #e2e8f0" :border-radius "8px"
                 :box-shadow "0 4px 6px -1px rgb(0 0 0 / 0.1)" :z-index 100}
         :children [[rc/h-box :style {:padding "12px" :border-bottom "1px solid #e2e8f0" :background-color "#f8fafc"}
                     :children [[rc/label :label "Active Jobs" :style {:font-weight 700}]]]
                    [rc/v-box
                     :children (for [[id job] @jobs]
                                 ^{:key id} [job-item id job])]]]))))
