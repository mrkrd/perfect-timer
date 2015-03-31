(ns perfect-timer.util
  (:require [clojure.edn :as edn]))


(defn ms-to-time-str [ms]
  (let [seconds (quot ms 1000)
        minutes (quot seconds 60)
        hours (quot minutes 60)]
    (format "%d:%02d:%02d" hours (rem minutes 60) (rem seconds 60))))

;; (ms-to-time-str 3661200)



(defn time-str-to-ms
  "Convert string (e.g. 12:00, 3:30:00, 15m 30s) to milliseconds.
  Return nil, if not possible."
  [string]
  (as-> nil hhmmss
    (if-let [[_ ss] (re-matches #"([0-9\.]+)" string)] ; 12
      (assoc hhmmss :ss (edn/read-string ss))
      hhmmss)
    (if-let [[_ mm ss] (re-matches #"([0-9]+):([0-9\.]+)" string)] ; 30:00
      (assoc hhmmss :mm (edn/read-string mm) :ss (edn/read-string ss))
      hhmmss)
    (if-let [[_ hh mm ss] (re-matches #"([0-9]+):([0-9]+):([0-9\.]+)" string)] ; 1:20:00
      (assoc hhmmss :hh (edn/read-string hh) :mm (edn/read-string mm) :ss (edn/read-string ss))
      hhmmss)
    (if-let [[_ mm] (re-find #"([0-9\.]+)\s*m" string)] ; 20 m
      (assoc hhmmss :mm (edn/read-string mm))
      hhmmss)
    (if-let [[_ ss] (re-find #"([0-9\.]+)\s*s" string)] ; 15 s
      (assoc hhmmss :ss (edn/read-string ss))
      hhmmss)
    (if-let [[_ hh] (re-find #"([0-9\.]+)\s*h" string)] ; 1 h
      (assoc hhmmss :hh (edn/read-string hh))
      hhmmss)
    (when hhmmss
      (int (+ (* (:hh hhmmss 0) 3600000)
              (* (:mm hhmmss 0) 60000)
              (* (:ss hhmmss 0) 1000))))))


;; (time-str-to-ms "10:10")
;; (time-str-to-ms "asdf")
