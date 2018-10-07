(ns sql.health-check)

(def health-check (constantly   {:status 200
                                 :headers {"Content-Type" "text/plain"}
                                 :body "200 OK"}))
