(ns example-datasource
  (:require [clojure.core.async :refer [<! go-loop go timeout]]
            [generate-test-data :refer [generate]])
  (:import (ru.yandex.qatools.embed.postgresql EmbeddedPostgres)
           (java.sql DriverManager)
           (java.io File)
           (java.util Date)
           (java.nio.file OpenOption Files Path Paths)))

(defn- wait-forever
  []
  @(promise))

(defn directory [& args]
  (Paths/get (System/getProperty "user.dir") (into-array String args)))

(defn resource-to-string [resource]
  (slurp (Files/newInputStream (directory "example-datasource" "resources" resource)
                               (into-array OpenOption []))))

(defn run-sql-to-database [sql url]
  (with-open [conn (DriverManager/getConnection url)]
    (-> conn .createStatement (.execute sql))))

(defn reload-sql-to-database [url]
  (try
    (run-sql-to-database (resource-to-string "purge.sql") url)
    (run-sql-to-database (resource-to-string "example-data.sql") url)
    (doall (for [x (range) :while (< x 100)]
      (generate)))
    ))

(defn sql-last-modified []
  (-> (directory "example-datasource" "resources" "example-data.sql")
      .toFile
      .lastModified))

(defn reinitialize-if-changed [url]
  (go-loop [last-modified (sql-last-modified)]
    (<! (timeout 500))
    (let [modified (sql-last-modified)]
      (if-not (= modified last-modified)
        (do
          (prn "changes were made!")
          (reload-sql-to-database url)))
      (recur modified))))

(defn -main [& args]
  (prn "Starting example datasource!")
  (let [cache-directory (directory "target-example-datasource")
        runtime-config (EmbeddedPostgres/cachedRuntimeConfig cache-directory)
        postgres (EmbeddedPostgres.)]
    (let [url (.start postgres runtime-config "localhost" 60600 "test" "test" "test"
                      (list "-E" "SQL_ASCII"
                            "--locale=C"
                            "--lc-collate=C"
                            "--lc-ctype=C"))]
      (prn "JDBC URL is " url)
      (reload-sql-to-database url)
      (reinitialize-if-changed url)
      (wait-forever))))
