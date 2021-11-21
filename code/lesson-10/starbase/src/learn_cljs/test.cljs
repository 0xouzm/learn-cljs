(ns learn-cljs.test)

(def greeting "Hi")

(defn make-greeter [greeting]
  (fn [name]
    (str greeting ", " name)))

((make-greeter "hello") "ouzm")




(defn factorial
  ([n] (factorial n 1))
  ([n result]
   (if (< n 1)
     result
     (recur (dec n) (* result n)))))


(factorial 12)
