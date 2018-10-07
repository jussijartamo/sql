(ns sql.dev
  (:require [ring.middleware.reload :refer [wrap-reload]]))

;#'
(defn live-reload [handler]
  (wrap-reload handler))


