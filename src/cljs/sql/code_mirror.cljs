(ns sql.code-mirror
  (:require [reagent.core :refer [create-class props atom]]
            [re-frame.core :refer [reg-event-db]]))

(defn code-mirror []
  (let [cm (atom nil)
        update (fn [comp]
                 (prn "update"))]
    (create-class {:reagent-render (fn []
                                     [:div#code-mirror])
                   :component-did-mount (fn [comp]
                                          (let [element (.getElementById js/document "code-mirror")
                                                configuration (clj->js {:mode "text/x-mysql"
                                                                        :lineNumbers false
                                                                        :value "SELECT * FROM a LIMIT 4"})
                                                editor (js/CodeMirror. element configuration)]
                                            (reset! cm editor))
                                          (update comp))
                   :component-did-update update
                   :display-name "code-mirror"})))
