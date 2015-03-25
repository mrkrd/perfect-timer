(defproject perfect-timer "0.3.0"
  :description "A simple timer app"
  :url "https://github.com/mrkrd/perfect-timer"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [seesaw "1.4.5"]]
  :main ^:skip-aot perfect-timer.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
