(ns perfect-timer.util
  (:require [clojure.edn :as edn]))


(defn ms-to-time-str [ms]
  (let [seconds (quot ms 1000)
        minutes (quot seconds 60)
        hours (quot minutes 60)]
    (format "%d:%02d:%02d" hours (rem minutes 60) (rem seconds 60))))

;; (ms-to-time-str 3661200)



(defn time-str-to-ms [string]

  (as-> {:hh 0 :mm 0 :ss 0} hhmmss
    (if-let [[_ ss] (re-matches #"([0-9\.]+)" string)] ; ss
      (assoc hhmmss :ss (edn/read-string ss))
      hhmmss)
    (if-let [[_ mm ss] (re-matches #"([0-9]+):([0-9\.]+)" string)] ; mm:ss
      (assoc hhmmss :mm (edn/read-string mm) :ss (edn/read-string ss))
      hhmmss)
    (if-let [[_ hh mm ss] (re-matches #"([0-9]+):([0-9]+):([0-9\.]+)" string)] ; hh:mm:ss
      (assoc hhmmss :hh (edn/read-string hh) :mm (edn/read-string mm) :ss (edn/read-string ss))
      hhmmss)
    (if-let [[_ mm] (re-find #"([0-9\.]+)\s*m" string)] ; mm m
      (assoc hhmmss :mm (edn/read-string mm))
      hhmmss)
    (if-let [[_ ss] (re-find #"([0-9\.]+)\s*s" string)] ; ss s
      (assoc hhmmss :ss (edn/read-string ss))
      hhmmss)
    (if-let [[_ hh] (re-find #"([0-9\.]+)\s*h" string)] ; hh h
      (assoc hhmmss :hh (edn/read-string hh))
      hhmmss)
    (int (+ (* (:hh hhmmss) 3600000)
            (* (:mm hhmmss) 60000)
            (* (:ss hhmmss) 1000))))

  )

;; (time-str-to-ms "10:10")
