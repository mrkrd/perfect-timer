(ns perfect-timer.core
  (:use seesaw.core)
  (:use seesaw.font)
  (:gen-class))

(native!)

(def input-field (text :text "1800"
                       :halign :center
                       :font (font :size 40)))

(def start-button (button :text "Start"
                          :font (font :size 26)))

(def reset-button (button :text "Reset"
                          :font (font :size 26)))

(def main-progress (progress-bar :max 1800000))

(def grid (grid-panel :columns 2
                      :items [input-field main-progress
                              start-button reset-button]))


(def state (atom {:starts []
                  :pauses []
                  :tmax 0}))



(defn ms-to-time-str [ms]
  (let [tenths (quot ms 100)
        minutes (quot tenths 600)
        hours (quot minutes 60)]
    (format "%d:%02d:%04.1f" hours (rem minutes 60) (/ (rem tenths 600) 10.))))

;; (ms-to-time-str 3661200)



(defn start-pressed []
  ;; UI changes
  (config! start-button :text "Pause")
  (config! input-field :editable? false)
  (request-focus! start-button)

  ;; State changes
  (if (empty? (@state :starts))            ; First press?
    (do
      (swap! state assoc-in [:tmax] (int (* (read-string (config input-field :text)) 1000)))
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
  (config! input-field :text (/ (@state :tmax) 1000))
  (config! input-field :editable? true)
  (config! start-button :text "Start")
  (config! main-progress :value 0)

  ;; State changes
  (swap! state assoc-in [:starts] [])
  (swap! state assoc-in [:pauses] []))

(def reset-listener (listen reset-button :action reset-pressed))
; (reset-listener)



(defn key-input-pressed [e]
  (if (= \newline (.getKeyChar e))
    (start-pressed)))

(def key-input-listener (listen input-field :key-typed key-input-pressed))
; (key-input-listener)



(defn tick []
  (let [starts (@state :starts)
        tmax (@state :tmax)
        pauses (conj (@state :pauses) (System/currentTimeMillis))
        time-passed (reduce + (map - pauses starts))]

    (if (seq (@state :starts))
      (config! input-field :text (ms-to-time-str (- tmax time-passed))))

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
     :width 640
     :height 320
     :content grid)
    ;; pack!
    show!)))
