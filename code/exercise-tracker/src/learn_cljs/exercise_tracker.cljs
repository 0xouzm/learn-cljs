(ns learn-cljs.exercise-tracker
  (:require [reagent.dom :as rdom]
            [goog.dom :as gdom]))

(defn hello []
  [:p "Hello World"])

(rdom/render
 hello                                                    ;; <1>
 (gdom/getElement "app"))