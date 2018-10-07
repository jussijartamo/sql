(ns generate-test-data
  (:import ;(org.postgresql.util PGobject)
   (java.sql DriverManager)))

(def url "jdbc:postgresql://localhost:60600/test?user=test&password=test")

(defn new-uuid []
  (str (java.util.UUID/randomUUID)))

(defn generate-new-basic-types-row []
  (str "INSERT INTO basic_types_table(
                        some_int, some_varchar, 
                        optional_text, uuid,
                        some_statement, jsonb) VALUES("
       100 ","
       "'some varchar'" ","
       "'some text'" ","
       "'" (new-uuid) "'" ","
       "TRUE" ","
       "'{}'"
       ");"))

(defn generate []
  (with-open [conn (DriverManager/getConnection url)]
    (prn (-> conn .createStatement (.execute (generate-new-basic-types-row))))))

(defn -main [& args]
  (generate)
  (prn "done"))
