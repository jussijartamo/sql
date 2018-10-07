(ns sql.event
  (:require [cljs.core.match :refer-macros [match]]
            [re-frame.core :refer [reg-sub reg-event-db reg-event-fx]]))

(reg-event-db
 :initialize
 (fn [_ _]
   {:sql ""
    :view :schema ;:result
    :schema nil}))

(reg-event-db
 :set-view
 (fn [db [_ view]]
   (assoc db
          :view view)))

(reg-event-db
 :set-schema
 (fn [db [_ schema]]
   (assoc db
          :schema schema)))

(reg-event-db
 :set-result
 (fn [db [_ result]]
   (assoc db
          :result 
          (-> (:data result)
          (assoc :uuid (:uuid result))
          (assoc :memory (:memory result))
          (assoc :took (:took result))
          ))))

(reg-sub
 :view
 (fn [db]
   (:view db)))

(reg-sub
 :code-mirror
 (fn [db schema]
   {:suggest (:suggest db)}))

(reg-sub
 :schema
 (fn [db]
   (:schema db)))

(reg-sub
 :result
 (fn [db]
   (:result db)))
   
(reg-event-db
 :suggest
 (fn [db [_ suggest]]
   (assoc db
          :suggest suggest)))