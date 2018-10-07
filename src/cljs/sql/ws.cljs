(ns sql.ws
  (:require [cljs.core.async :refer [<! >!]]
            [cljs.core.match :refer-macros [match]]
            [re-frame.core :refer [reg-event-fx dispatch]]
            [websocket-client.core :refer [ async-websocket]])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(def socket (atom nil))

(reg-event-fx
 :receive
 (fn [db _]
   (when @socket
     (prn "old socket!"))
   (reset! socket (async-websocket "ws://localhost:5050/api/"))
   (go-loop []
     (let [msg (<! @socket)
           result (cljs.reader/read-string msg)]
       (match result
         {:type "query"} (dispatch [:set-result result])
         {:type "schema"} (dispatch [:set-schema (:data result)]))
       (recur)))
   nil))


(reg-event-fx
 :send
 (fn [db [_ command]]
   (go (>! @socket (assoc command :uuid (str (random-uuid)))))
   nil))