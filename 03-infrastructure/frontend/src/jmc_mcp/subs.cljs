(ns jmc-mcp.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :route/current
 (fn [db _]
   (get-in db [:route :current])))

(rf/reg-sub
 :route/params
 (fn [db _]
   (get-in db [:route :params])))

(rf/reg-sub
 :recordings/items
 (fn [db _]
   (get-in db [:recordings :items])))

(rf/reg-sub
 :recordings/loading?
 (fn [db _]
   (get-in db [:recordings :loading?])))

(rf/reg-sub
 :recording-detail/recording-id
 (fn [db _]
   (get-in db [:recording-detail :recording-id])))

(rf/reg-sub
 :recording-detail/results
 (fn [db _]
   (get-in db [:recording-detail :results])))

(rf/reg-sub
 :recording-detail/info
 (fn [db _]
   (let [id (get-in db [:recording-detail :recording-id])
         items (get-in db [:recordings :items])]
     (or (get-in db [:recording-detail :info])
         (some #(when (= (:id %) id) %) items)))))

(rf/reg-sub
 :recording-detail/active-tab
 (fn [db _]
   (get-in db [:recording-detail :active-tab])))

(rf/reg-sub
 :recording-detail/diagnostic-focus
 (fn [db _]
   (get-in db [:recording-detail :diagnostic-focus])))

(rf/reg-sub
 :recording-detail/forensic-focus
 (fn [db _]
   (get-in db [:recording-detail :forensic-focus])))

(rf/reg-sub
 :recording-detail/analysis-params
 (fn [db _]
   (get-in db [:recording-detail :analysis-params])))

(rf/reg-sub
 :recording-detail/result
 (fn [db [_ type]]
   (get-in db [:recording-detail :results type])))

(rf/reg-sub
 :recording-detail/loading?
 (fn [db _]
   (get-in db [:recording-detail :loading?])))

(rf/reg-sub
 :comparison/result
 (fn [db _]
   (get-in db [:comparison :result])))

(rf/reg-sub
 :comparison/active-tab
 (fn [db _]
   (get-in db [:comparison :active-tab])))

(rf/reg-sub
 :comparison/collapsed-sections
 (fn [db _]
   (get-in db [:comparison :collapsed-sections])))

(rf/reg-sub
 :comparison/trace-modal
 (fn [db _]
   (get-in db [:comparison :trace-modal])))

(rf/reg-sub
 :comparison/diff-call-tree
 (fn [db _]
   (get-in db [:comparison :diff-call-tree :data])))

(rf/reg-sub
 :comparison/expanded-nodes
 (fn [db _]
   (get-in db [:comparison :diff-call-tree :expanded])))

(rf/reg-sub
 :comparison/loading-nodes
 (fn [db _]
   (get-in db [:comparison :diff-call-tree :loading-nodes])))

(rf/reg-sub
 :comparison/diff-call-tree-target
 (fn [db _]
   (get-in db [:comparison :diff-call-tree :target-method])))

(rf/reg-sub
 :comparison/diff-stack-traces
 (fn [db _]
   (get-in db [:comparison :diff-stack-traces])))

(rf/reg-sub
 :comparison/loading?
 (fn [db _]
   (get-in db [:comparison :loading?])))

(rf/reg-sub
 :upload/status
 (fn [db _]
   (get-in db [:upload :status])))

(rf/reg-sub
 :heapdumps/items
 (fn [db _]
   (get-in db [:heapdumps :items])))

(rf/reg-sub
 :heapdumps/loading?
 (fn [db _]
   (get-in db [:heapdumps :loading?])))

(rf/reg-sub
 :notifications
 (fn [db _]
   (:notifications db)))

(rf/reg-sub
 :jobs/all
 (fn [db _]
   (:jobs db)))

(rf/reg-sub
 :sse/sources
 (fn [db _]
   (:sse-sources db)))
