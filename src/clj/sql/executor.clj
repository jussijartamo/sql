(ns sql.executor
  (:require [clojure.core.match :refer [match]]
            [clojure.pprint :as pp]
            [sql.schema :refer [metadata->schema]])
  (:import ;(org.postgresql.util PGobject)
           (java.sql DriverManager)))

(def url "jdbc:postgresql://localhost:60600/test?user=test&password=test")
(def last-query-result (atom nil))

(defn display-schema []
  (with-open [conn (DriverManager/getConnection url)]
    (doall (metadata->schema conn))))

(defn transform-value [v]
  (str v))

(defn transform-values [v]
  (map transform-value v))

(defn query [sql uuid]
  (with-open [conn (DriverManager/getConnection url)]
    (if-let [a (resultset-seq (-> conn .createStatement (.executeQuery sql)))]
      (let [only-values (doall (mapv #(vec (vals %)) a))]
        (reset! last-query-result {:uuid uuid :result only-values})
        (if a
          {:columns (keys (first a))
           :rows (doall (map transform-values only-values))}))
      {:columns []
       :rows []})))

(defn execute [command]
  (match command
    {:command "schema"} (display-schema)
    {:command "query" :sql sql :uuid uuid} (query sql uuid)
    ))

