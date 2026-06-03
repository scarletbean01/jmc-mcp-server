(ns jmc-mcp.db)

(def default-db
  {:route {:current :library
           :params {}}

   :recordings {:items []
                :loading? false
                :error nil}

   :heapdumps {:items []
               :loading? false
               :error nil}

   :recording-detail {:recording-id nil
                      :info nil
                      :active-tab :copilot
                      :diagnostic-focus :cpu
                      :forensic-focus :overview
                      :analysis-params {:start-time nil
                                        :end-time nil
                                        :topN 20}
                      :results {}
                      :loading? false}

   :upload {:status :idle
            :progress 0
            :error nil}

   :jobs {}

   :comparison {:baseline-id nil
                :target-id nil
                :result nil
                :active-tab :overview
                :collapsed-sections #{}
                :trace-modal nil
                :diff-call-tree {:data nil :expanded #{} :loading-nodes #{} :target-method nil}
                :diff-stack-traces nil
                :loading? false
                :error nil}

   :notifications []

   :ui {:sidebar-collapsed? false}})
