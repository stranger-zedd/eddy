(defproject eddy "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clojurewerkz/quartzite "2.0.0"]
                 [amazonica "0.3.58"]]
  :resource-paths ["src/resources"]
  :main ^:skip-aot eddy.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
