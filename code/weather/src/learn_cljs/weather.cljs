(ns ^:figwheel-hooks learn-cljs.weather
  (:require
   [goog.dom :as gdom]
   [reagent.dom :as rdom]
   [reagent.core :as r]
   [ajax.core :as ajax]
   ))

(defonce app-state (r/atom {:title "WhichWeather"
                            :postal-code "94016"
                            :temperatures {:today {:label "Today"
                                                   :value nil}
                                           :tomorrow {:label "Tomorrow"
                                                      :value nil}}}))

(def api-key "2f04478d774bc05af379c898cc3e3195")

(defn handle-response [resp]
  (let [today (get-in resp ["list" 0 "main" "temp"])       ;; <1>
        tomorrow (get-in resp ["list" 8 "main" "temp"])]
    (swap! app-state                                       ;; <2>
           update-in [:temperatures :today :value] (constantly today))
    (swap! app-state
           update-in [:temperatures :tomorrow :value] (constantly tomorrow))))


(defn get-forecast! []
  (let [postal-code (:postal-code @app-state)]             ;; <1>
    (ajax/GET "http://api.openweathermap.org/data/2.5/forecast"
      {:params {"q" postal-code
                "appid" api-key
                "units" "imperial"}
       :handler handle-response})))                     ;; <2>


(defn title []
  [:h1 {:title @app-state}])

(defn temperatures [temp]
  [:div {:class "temperature"}
   [:div {:class "value"}
    (:value temp)]
   [:h2 (:label temp)]])

(defn postal-code []
  [:div {:class "postal-code"}
   [:h3 "Enter your postal code"]
   [:input {:type "text"
            :placeholder "Postal Code"
            :value (:postal-code @app-state)
            :on-change #(swap! app-state assoc :postal-code (-> % .-target .-value))}]
   [:button {:on-click get-forecast!} "Go"]])

(defn app []
  [:div {:class "app"}
   [title]
   [:div {:class "temperatures"}
    (for [temp (vals (:temperatures @app-state))]
      [temperatures temp])]
   [postal-code]])

(defn mount-app-element []
  (rdom/render [app] (gdom/getElement "app")))
(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
)
