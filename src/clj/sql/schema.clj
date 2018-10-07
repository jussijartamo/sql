(ns sql.schema
  (:require [clojure.core.match :refer [match]]
            [clojure.pprint :as pp]))

(defn result->column [result]
  {:name (:column_name result)
   :ordinal (:ordinal_position result)
   :type (:type_name result)})

(defn result->table [metadata result]
  (let [columns (.getColumns metadata nil (:table_schem result) (:table_name result) nil)]
    {:schema (:table_schem result)
     :name (:table_name result)
     :type (:table_type result)
     :columns (mapv result->column (resultset-seq columns))}))

(defn metadata->tables [type_kw metadata]
  (->> (.getTables metadata nil nil nil (into-array String ["TABLE" "VIEW"]))
       (resultset-seq)
       (mapv #(result->table metadata %))))

(defn metadata->schema [connection]
  (let [metadata (.getMetaData connection)
        tables (metadata->tables :table metadata)]
    tables))

