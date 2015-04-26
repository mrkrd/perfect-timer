(ns perfect-timer.util
  (:require [clojure.edn :as edn]))


(defn ms-to-time-str [ms]
  (let [seconds (quot ms 1000)
        minutes (quot seconds 60)
        hours (quot minutes 60)]
    (format "%d:%02d:%02d" hours (rem minutes 60) (rem seconds 60))))

;; (ms-to-time-str 3661200)



(defn hhmmss-map-to-ms
  [hhmmss]
  (int (+ (* (:hh hhmmss 0) 3600000)
          (* (:mm hhmmss 0) 60000)
          (* (:ss hhmmss 0) 1000))))


(defn time-str-to-ms
  "Convert string (e.g. 12:00, 3:30:00, 15m 30s) to milliseconds.
  Return nil, if not possible."
  [string]
  (condp re-matches string
    #"([0-9\.]+)" :>> #(hhmmss-map-to-ms {:hh 0
                                          :mm (edn/read-string (nth % 1))
                                          :ss 0})

    #"([0-9]+):([0-9\.]+)" :>> #(hhmmss-map-to-ms {:hh 0
                                                   :mm (edn/read-string (nth % 1))
                                                   :ss (edn/read-string (nth % 2))})

    #"([0-9]+):([0-9]+):([0-9\.]+)" :>> #(hhmmss-map-to-ms {:hh (edn/read-string (nth % 1))
                                                            :mm (edn/read-string (nth % 2))
                                                            :ss (edn/read-string (nth % 3))})
    nil))


;; (time-str-to-ms "10:10")
;; (time-str-to-ms "asdf")
