(ns sql.schema
  (:require [re-frame.core :refer [subscribe reg-event-db dispatch-sync dispatch]]))

(defn display-schema []
  (fn []
    (let [schema @(subscribe [:schema])]
      [:div
       (for [[i table] (map-indexed vector schema)]
         [:table
          {:key (str "table-" i)
           :on-mouse-enter  (fn [event]
                              (dispatch [:suggest (str "select * from " (:name table) " limit 1")]))
           :on-mouse-leave  (fn [event]
                              (dispatch [:suggest nil]))}
          [:thead
           [:tr
            [:th {:colSpan 2} (:name table)]]
           [:tr
            [:th "Name"]
            [:th "Type"]]]
          [:tbody
           (for [[i column] (map-indexed vector (:columns table))]
             [:tr
              {:key (str "column-" i)}
              [:td (:name column)]
              [:td (:type column)]])]])]
      )))