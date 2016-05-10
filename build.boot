(set-env!
 :resource-paths #{"src" "resources"}
 :dependencies '[[org.clojure/clojure "1.8.0"]
                 [seesaw "1.4.5"]
                 ])


(task-options!
 aot {:namespace '#{perfect-timer.core}}
 pom {:project 'perfect-timer
      :version "0.4.0"}
 jar {:main 'perfect-timer.core}
 target {:dir #{"target"}}
)


(deftask build
  "Build project.jar uberjar."
  []
  (comp (aot) (pom) (uber) (jar) (target)))
