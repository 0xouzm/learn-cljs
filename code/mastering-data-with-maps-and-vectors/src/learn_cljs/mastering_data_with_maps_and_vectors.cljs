(ns ^:figwheel-hooks learn-cljs.mastering-data-with-maps-and-vectors
  (:require
   [goog.dom :as gdom]))

(println "This text is printed from src/learn_cljs/mastering_data_with_maps_and_vectors.cljs. Go ahead and edit it and see reloading in action.")

(defn multiply [a b] (* a b))

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:text "Hello world!"}))

(defn get-app-element []
  (gdom/getElement "app"))


(defn event [type]
  {:type type
   :timestamp (.now js/Date)})

(defn click [location target]
  (merge (event :click)
         {:location location :target target}))
;; (click [644 831] "#somewhere")
;; specify reload hook with ^;after-load metadata


(defn page-view
  ([url] (page-view url (.now js/Date) []))
  ([url loaded] (page-view url loaded []))
  ([url loaded events]
   {:url url
    :loaded loaded
    :events events}))

(defn session
  ([start is-active? ip user-agent] (session start is-active? ip user-agent []))
  ([start is-active? ip user-agent page-views]
   {:start start
    :is-active? is-active?
    :ip ip
    :user-agent user-agent
    :page-views page-views}))

(defn user
  ([id name] (user id name []))
  ([id name sessions]
   {:id id
    :name name
    :sessions sessions}))

(defn with-duration [session end-time]
  (let [duration-in-ms (- end-time (:start session))
        duration-in-s (/ duration-in-ms 1000)]
    (assoc session :duration duration-in-s)))

(def my-session
  (session (.now js/Date) true "127.0.0.1" "my ua"))

 (defn untrack [session]
   (dissoc session :ip :user-agent))

(defn ^:after-load on-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
