(ns sql.core
  (:require [reagent.core :refer [render]]
            [sql.event]
            [sql.ws]
            [sql.schema :refer [display-schema]]
            [sql.result :refer [display-result]]
            [cljs.core.match :refer-macros [match]]
            [sql.code-mirror :refer [code-mirror]]
            [re-frame.core :refer [clear-subscription-cache! subscribe reg-event-db dispatch-sync dispatch]]
            ;[reframe-websocket.core :as reframe-websocket]
            [clojure.string :as str]))

(enable-console-print!)

(defn ui
  []
  [:div
   [code-mirror]
   (match @(subscribe [:view])
     :schema [display-schema]
     :result [display-result])])

(defn ^:export run
  []
  (dispatch-sync [:initialize])
  (dispatch [:receive])
  (dispatch [:send {:command "schema"}])
  (dispatch [:send {:command "query" :sql "select * from basic_types_table"}])
  (clear-subscription-cache!)
  (render [ui]
          (js/document.getElementById "app")))
