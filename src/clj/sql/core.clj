(ns sql.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [sql.ws.ws :as ws]
            [clojure.edn :as edn]
            [ring.middleware.params :refer [wrap-params]]
            [clojure.pprint :as pp]
            [clojure.data.json :as json]
            [clj-memory-meter.core :refer [measure]]
            [sql.executor :refer [execute last-query-result]]
            [sql.dev :refer [live-reload]]
            [sql.health-check :refer [health-check]])
  (:import (org.postgresql.util PGobject))
  (:gen-class))

(defn- convert-to-human-readable
  "Taken from http://programming.guide/java/formatting-byte-size-to-human-readable-format.html."
  [bytes]
  (let [unit 1024]
    (if (< bytes unit)
      (str bytes " B")
      (let [exp (int (/ (Math/log bytes) (Math/log unit)))
            pre (nth "KMGTPE" (dec exp))]
        (format "%.1f %sB" (/ bytes (Math/pow unit exp)) pre)))))

(defn memory->human-readable [memory]
  [memory (convert-to-human-readable memory)])

(defn ws-handler [request]
  {:status 101
   :ws-handlers {:on-connect (fn [ws]
                               (ws/connected? ws))
                 :on-close (fn [ws code reason]
                             (prn "Websocket closed" code reason))
                 :on-error (fn [ws err]
                             (prn err "Websocket connection error!" ws))
                 :on-text (fn [ws message]
                            (let [runtime (java.lang.Runtime/getRuntime)
                                  now (System/currentTimeMillis)
                                  command (edn/read-string message)
                                  data (execute command)
                                  result {:memory {:total (memory->human-readable (.totalMemory runtime))
                                                   :free (memory->human-readable (.freeMemory runtime))
                                                   :usage (memory->human-readable (- (.totalMemory runtime) (.freeMemory runtime)))
                                                   :data (memory->human-readable
                                                          (measure data :bytes true))}
                                          :uuid (:uuid command)
                                          :took (- (System/currentTimeMillis) now)
                                          :type (:command command)
                                          :data data}]

                              (ws/send! ws (pr-str result))))
                 :on-binary (fn [ws bytes offset len]
                              (prn "Websocket binary data recived" len))}})


(defn setup-websocket []
  (ws/configurator (live-reload #'ws-handler)))

(defn transform->value [value]
  (condp instance? value
    Long value
    Integer value
    Boolean value
    PGobject (json/read-str (str value))
    (str value)))

(defn to-json [result row cell]
  (cond
    (and row cell) (transform->value (get-in result [row cell]))
    (and cell) (mapv #(transform->value (get % cell)) result)))


(defn handler [{{requested-uuid "uuid" row "row" cell "cell"} :query-params}]
  (let [{:keys [uuid result]} @last-query-result]
    (cond
      (= uuid requested-uuid) {:status 200
                               :headers {"Content-Type" "application/json; charset=utf-8"}
                               :body (json/write-str (to-json result
                                                              (some-> row Integer/parseInt)
                                                              (some-> cell Integer/parseInt)))}
      :else  {:status 200
              :headers {"Content-Type" "text/plain"}
              :body "(str @last-query-result)"})))

(defn run-dev-server
  [port]
  (run-jetty (wrap-params (live-reload #'handler))
             {:port port
              :configurator (setup-websocket)}))

(defn -main [& args]
  (prn "Starting app!")
  (run-dev-server 5050))

