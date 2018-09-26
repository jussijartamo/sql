(ns sql.event
  (:require [re-frame.core :refer [reg-event-db]]))

(reg-event-db
 :initialize
 (fn [_ _]
   {:sql ""}))