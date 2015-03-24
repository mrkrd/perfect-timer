(ns perfect-timer.audio
  (:use [clojure.java.io :as io])

  (:import (javax.sound.sampled AudioSystem)))



(defn play-sound [sound-file]
  (let [sound-stream (AudioSystem/getAudioInputStream sound-file)
        clip (AudioSystem/getClip)]

    (doto clip
      (.open sound-stream)
      (.start))))



;; (-> "chirps.wav" io/resource io/file play-sound)
;; (play-sound (io/input-stream "jar:file:/home/marek/projects/perfect-timer/target/uberjar/perfect-timer-0.2.0-standalone.jar!/chirps.wav"))
;; (play-sound (io/file "resources/chirps.wav"))
