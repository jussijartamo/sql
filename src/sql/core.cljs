(ns sql.core
  (:require [reagent.core :refer [render]]
            [sql.event]
            [sql.code-mirror :refer [code-mirror]]
            [re-frame.core :refer [reg-event-db dispatch-sync]]
            [clojure.string :as str]))

(defn ui
  []
  [:div
   [code-mirror]])

(defn ^:export run
  []
  (dispatch-sync [:initialize])
  (render [ui]
          (js/document.getElementById "app")))
