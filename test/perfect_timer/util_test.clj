(ns perfect-timer.core-test
  (:require [clojure.test :refer :all]
            [perfect-timer.util :refer :all]))



(is (= (ms-to-time-str 3661200)
       "1:01:01"))

(is (= (time-str-to-ms "10:10")
       610000))

(is (= (time-str-to-ms "asdf")
       nil))
