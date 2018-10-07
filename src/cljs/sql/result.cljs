(ns sql.result
  (:require [re-frame.core :refer [subscribe reg-event-db dispatch-sync dispatch]]))

(defn display-result []
  (fn []
    (let [{:keys [uuid columns rows]} @(subscribe [:result])]
      [:div
       [:table
        [:thead
         [:tr
          (for [[cell-index column] (map-indexed vector columns)]
            [:th.data-cell {
                :on-click (fn [event]
                            (.open js/window
                                   (str "http://localhost:5050/?uuid=" uuid "&cell=" cell-index)))
                :key (str "column-" cell-index)} column])]]
        [:tbody
         (for [[row-index row] (map-indexed vector rows)]
           [:tr
            {:key (str "row-" row-index)}
            (for [[cell-index cell] (map-indexed vector row)]
              [:td.data-cell {
                  :on-click (fn [event]
                              (.open js/window
                                     (str "http://localhost:5050/?uuid=" uuid "&row=" row-index "&cell=" cell-index)))
                  :key (str "cell-" cell-index)} cell])])
              ]]])))
