(defproject perfect-timer "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [seesaw "1.4.5"]
                 [overtone/at-at "1.2.0"]]
  :main ^:skip-aot perfect-timer.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
