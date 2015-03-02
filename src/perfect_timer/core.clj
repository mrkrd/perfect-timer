(ns perfect-timer.core
  (:use seesaw.core)
  (:gen-class))

(native!)

;(use 'seesaw.dev)
;(seesaw.dev/debug!)

(def input-field (text :text "1800"
                       :halign :center))
(def start-button (button :text "Start"))
(def reset-button (button :text "Reset"))
(def main-progress (progress-bar :max 1800000))


(def grid (grid-panel :columns 2
                      :items [input-field main-progress
                              start-button reset-button]))


(def state (atom {:starts []
                  :pauses []
                  :tmax 0}))


(defn start-pressed []
  ;; UI changes
  (config! start-button :text "Pause")
  (config! input-field :editable? false)

  ;; State changes
  (if (empty? (@state :starts))            ; First press?
    (do
      (swap! state assoc-in [:tmax] (* (read-string (config input-field :text)) 1000))
      (config! main-progress :max (@state :tmax))))
  (swap! state update-in [:starts] conj (System/currentTimeMillis)))



(defn pause-pressed []
  ;; UI changes
  (config! start-button :text "Start")

  ;; State changes
  (swap! state update-in [:pauses] conj (System/currentTimeMillis)))


(defn start-or-pause-pressed [e]
  (if (> (count (@state :starts)) (count (@state :pauses)))
    (pause-pressed)
    (start-pressed)))

(def start-listener (listen start-button :action start-or-pause-pressed))
; (start-listener)


(defn reset-pressed [e]
  ;; UI changes
  (config! input-field :text (/ (@state :tmax) 1000.))
  (config! input-field :editable? true)
  (config! start-button :text "Start")
  (config! main-progress :value 0)

  ;; State changes
  (swap! state assoc-in [:starts] [])
  (swap! state assoc-in [:pauses] []))


(def reset-listener (listen reset-button :action reset-pressed))
; (reset-listener)


(defn tick []
  (let [starts (@state :starts)
        tmax (@state :tmax)
        pauses (conj (@state :pauses) (System/currentTimeMillis))
        time-passed (reduce + (map - pauses starts))]

    (if (seq (@state :starts))
      (config! input-field :text (format "%.1f" (/ (- tmax time-passed) 1000.0))))

    (config! main-progress :value time-passed)

    (if (> time-passed (@state :tmax))
      (do
        (reset-pressed nil)
        (alert "Time's up!")))))



(def main-timer (timer (fn [e] (tick)) :delay 100))
; (.stop main-timer)



(defn -main [& args]
  (invoke-later
   (->
    (frame
     :title "Perfect Timer"
     :on-close :exit
     :content grid)
    pack!
    show!)))
