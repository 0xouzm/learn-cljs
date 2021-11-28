(ns ^:figwheel-hooks learn-cljs.reagent-test
  (:require
   [goog.dom :as gdom]))

(println "This text is printed from src/learn_cljs/reagent_test.cljs. Go ahead and edit it and see reloading in action.")


;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
