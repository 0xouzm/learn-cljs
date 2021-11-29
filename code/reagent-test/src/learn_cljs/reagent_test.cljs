(ns ^:figwheel-hooks learn-cljs.reagent-test
  (:require
   [reagent.dom :as rdom]
   [goog.dom :as gdom]))


(defn hello []
  [:p "hello world"])

(rdom/render
 hello
 (gdom/getElement "app"))
