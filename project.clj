(defproject clojure-knn "0.1.0-SNAPSHOT"
  :description ""
  :url ""
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clojure-csv "2.0.2"]]
  :main ^:skip-aot clojure-knn.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
