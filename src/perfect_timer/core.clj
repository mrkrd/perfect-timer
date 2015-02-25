(ns perfect-timer.core
  (:use seesaw.core)
  (:gen-class))

(native!)



(def status-label (label))
(def input-field (text :text "3600"))
(def start-button (button :text "Start"))
(def reset-button (button :text "Reset"))


(def grid (grid-panel :rows 2
                      :columns 2
                      :items [status-label input-field start-button reset-button]))


(def state (atom {:mode "paused"
                  :start-time 0
                  :end-time 0
                  :passed-time 0}))


(defn start-pressed []
  (swap! state assoc-in [:mode] "paused")
  (config! start-button :text "Pause")
  (swap! state assoc-in [:end-time] (read-string (config input-field :text))))

(defn pause-pressed []
  (swap! state assoc-in [:mode] "started")
  (config! start-button :text "Start"))

(defn start-or-pause-pressed [e]
  (if (= (@state :mode) "started")
    (start-pressed)
    (pause-pressed)))

(def start-listener (listen start-button :action start-or-pause-pressed))
; (start-listener)


; XXX clojure timer needed: need to update the GUI every sec or so
(System/currentTimeMillis)

(defn tick []
  (config! status-label :text (str @state)) ;(System/currentTimeMillis))

)


(def t (timer (fn [e] (tick)) :delay 1000))

(.stop t)



(defn -main [& args]
  (invoke-later
   (->
    (frame
     :title "Perfect Timer"
     ;; :on-close :exit
     :content grid)
    pack!
    show!)))
