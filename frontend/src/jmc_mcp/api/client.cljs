(ns jmc-mcp.api.client
  (:require [jmc-mcp.config :as config]
            [ajax.core :as ajax]))

(defn url [& paths]
  (apply str config/api-url paths))

(defn GET [path options]
  (ajax/GET (url path)
            (merge {:response-format (ajax/json-response-format {:keywords? true})
                    :error-handler (fn [e] (println "API Error:" e))}
                   options)))

(defn POST [path options]
  (ajax/POST (url path)
             (merge {:format (ajax/json-request-format)
                     :response-format (ajax/json-response-format {:keywords? true})
                     :error-handler (fn [e] (println "API Error:" e))}
                    options)))

(defn DELETE [path options]
  (ajax/DELETE (url path)
               (merge {:response-format (ajax/json-response-format {:keywords? true})
                       :error-handler (fn [e] (println "API Error:" e))}
                      options)))
