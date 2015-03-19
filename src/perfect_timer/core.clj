(ns perfect-timer.core
  (:use [perfect-timer.util]
        [seesaw.core]
        [seesaw.font])
  (:gen-class))

(native!)

(def default-ms 1800000)


(def input-field (text :text (ms-to-time-str default-ms)
                       :halign :center
                       :font (font :size 40)))

(def start-button (button :text "Start"
                          :font (font :size 26)))

(def reset-button (button :text "Reset"
                          :font (font :size 26)))

(def main-progress (progress-bar :max default-ms))

(def grid (grid-panel :columns 2
                      :items [input-field main-progress
                              start-button reset-button]))


(def state (atom {:starts []
                  :pauses []
                  :tmax default-ms}))





(defn start-pressed []
  ;; UI changes
  (config! start-button :text "Pause")
  (config! input-field :editable? false)
  (config! input-field :background "#eeeeee")
  (request-focus! start-button)

  ;; State changes
  (if (empty? (@state :starts))            ; First press?
    (do
      (swap! state assoc-in [:tmax] (time-str-to-ms (config input-field :text)))
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

(def start-listener (listen start-button :action #(start-or-pause-pressed %)))
; (start-listener)



(defn reset-pressed [e]
  ;; UI changes
  (config! input-field :text (ms-to-time-str (@state :tmax)))
  (config! input-field :editable? true)
  (config! input-field :background :white)
  (config! start-button :text "Start")
  (config! main-progress :value 0)

  ;; State changes
  (swap! state merge {:starts [], :pauses []}))

(def reset-listener (listen reset-button :action #(reset-pressed %)))
; (reset-listener)



(defn key-input-pressed [e]
  (condp = (.getKeyChar e)
    \newline (when (time-str-to-ms (config input-field :text))
               (start-pressed))
    ;; `invoke-later` is necessary, because we get the key, before the
    ;; text gets updated.
    (invoke-later (if (time-str-to-ms (config input-field :text))
      (config! input-field :background :white)
      (config! input-field :background "#ee9999")))))


(def key-input-listener (listen input-field :key-typed #(key-input-pressed %)))
; (key-input-listener)



(defn tick []
  (let [{:keys [starts tmax pauses]} @state
        pauses (conj pauses (System/currentTimeMillis))
        time-passed (reduce + (map - pauses starts))]

    (if (seq starts)
      (config! input-field :text (ms-to-time-str (- tmax time-passed))))

    (config! main-progress :value time-passed)

    (when (> time-passed tmax)
      (reset-pressed nil)
      (alert "Time's up!"))))


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
    show!))

  (swap! state assoc :main-timer (timer (fn [e] (tick)) :delay 200)))
  ;; (.stop (:main-timer @state))
