(ns perfect-timer.core
  (:use seesaw.core)
  (:use overtone.at-at)
  (:gen-class))

(native!)



(def status_label (label))
(def input_field (text))
(def start_button (button :text "Start"))
(def reset_button (button :text "Reset"))


(def grid (grid-panel :rows 2
                      :columns 2
                      :items [status_label input_field start_button reset_button]))




; XXX clojure timer needed: need to update the GUI every sec or so
(System/currentTimeMillis)

(defn tick []
  (config! status_label :text (System/currentTimeMillis) 1000))


(def my-pool (mk-pool))
;(every 1000 #(println "I am cool!") my-pool)
(every 1000 tick my-pool)
(stop-and-reset-pool! my-pool)


(defn -main [& args]
  (invoke-later
   (->
    (frame
     :title "Perfect Timer"
     ;; :on-close :exit
     :content grid)
    pack!
    show!)))
