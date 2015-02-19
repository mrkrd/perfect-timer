(ns perfect-timer.core
  (:use seesaw.core)
  (:gen-class))

(native!)

(def main_frame (frame :title "Perfect Timer"))


(-> main_frame pack! show!)


(def status_label (label))
(def input_field (text))
(def start_button (button :text "Start"))
(def reset_button (button :text "Reset"))


(def grid (grid-panel :rows 2
                      :columns 2
                      :items [status_label input_field start_button reset_button]))


(config! main_frame :content grid)


; XXX clojure timer needed

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
