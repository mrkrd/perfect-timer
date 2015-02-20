(ns perfect-timer.core
  (:use seesaw.core)
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





(defn -main
  (invoke-later
   (->
    (frame
     :title "Perfect Timer"
     ;; :on-close :exit
     :content grid)
    pack!
    show!)))
