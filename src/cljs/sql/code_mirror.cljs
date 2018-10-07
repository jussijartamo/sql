(ns sql.code-mirror
  (:require [reagent.core :refer [create-class props atom]]
            [re-frame.core :refer [subscribe reg-event-db dispatch]]))

(defn code-mirror-inner []
  (let [cm (atom nil)
        value (atom nil)
        update (fn [comp]
                 (let [{:keys [suggest]} (props comp)]
                   (if suggest
                     (when (not @value)
                       (reset! value (.getValue @cm))
                       (.setValue @cm (str "\r\r" suggest "\r\r")))
                     (when @value
                       (.setValue @cm @value)
                       (reset! value nil)))
                   ;(prn "is suggest?" suggest)
                   ))]
    (create-class {:reagent-render (fn []
                                     [:div#code-mirror
                                      [:p "jee"]
                                      [:div#code-mirror-inner {:class (when @value "suggest")}]
                                      [:div.tabs
                                       [:a.tab {:class (when (= @(subscribe [:view]) :schema) "tab--selected")
                                                :on-click (fn [event]
                                                            (dispatch [:set-view :schema]))}
                                        "Schema"]
                                       [:a.tab {:class (when (= @(subscribe [:view]) :result) "tab--selected")
                                                :on-click (fn [event]
                                                            (dispatch [:set-view :result]))}
                                        "Result"]
                                       [:a.tab {:on-click (fn [event]
                                       (.print js/window))} "Print"]]])
                   :component-did-mount (fn [comp]
                                          (let [element (.getElementById js/document "code-mirror-inner")
                                                configuration (clj->js {:mode "text/x-mysql"
                                                                        :lineNumbers false
                                                                        :value "\r\rSELECT * FROM a LIMIT 4\r\r"})
                                                editor (js/CodeMirror. element configuration)]
                                            (reset! cm editor))
                                          (update comp))
                   :component-did-update update
                   :display-name "code-mirror"})))

(defn code-mirror []
  (let [state (subscribe [:code-mirror])]
    (fn []
      [code-mirror-inner @state])))
