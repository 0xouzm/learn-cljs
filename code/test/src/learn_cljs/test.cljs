(ns ^:figwheel-hooks learn-cljs.test
  (:require
   [goog.dom :as gdom]
   [goog.events :as gevent]
   [cljs.core.async :refer [go-loop chan <! >! put! alts! go timeout]]))

(def query-input (gdom/getElement "query-input"))
(def results-display (gdom/getElement "query-results"))


(def keydown-ch (chan))                                    ;; <1>
(gevent/listen js/document "keydown"
               #(put! keydown-ch (.-key %)))

(def keyup-ch (chan))                                      ;; <2>
(gevent/listen js/document "keyup"
               #(put! keyup-ch (.-key %)))

(def is-modifier? #{"Control" "Meta" "Alt" "Shift"})

(def chord-ch (chan))
(go-loop [modifiers []                                     ;; <3>
          pressed nil]
  (when (and (seq modifiers) pressed)                      ;; <4>
    (>! chord-ch (conj modifiers pressed)))
  (let [[key ch] (alts! [keydown-ch keyup-ch])]            ;; <5>
    (println "key:" key)
    (condp = ch
      keydown-ch (if (is-modifier? key)                    ;; <6>
                   (recur (conj modifiers key) pressed)
                   (recur modifiers key))
      keyup-ch (if (is-modifier? key)
                 (recur (filterv #(not= % key) modifiers)
                        pressed)
                 (recur modifiers nil)))))


(defn mock-request [query] 
  (let [ch (chan)]
    (js/setTimeout
     #(put! ch (str "results for: " query))
     500)
    ch))


(go-loop []
  (let [chord (<! chord-ch)]
       (when (and (= chord ["Control" "r"])
               (= js/document.activeElement query-input))
      (set! (.-innerText results-display) "Loading...")
      (set! (.-innerText results-display)
            (<! (mock-request (.-value query-input)))))
    (recur)))