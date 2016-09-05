(defproject eddy "0.1.0-SNAPSHOT"
  :description "A daemon to check s3 and send mail"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clojurewerkz/quartzite "2.0.0"]
                 [environ "1.0.3"]
                 [ch.qos.logback/logback-classic "1.1.3"]
                 [com.draines/postal "2.0.0"]
                 [ring/ring-codec "1.0.1"]
                 [amazonica "0.3.58"]]
  :resource-paths ["src/resources"]
  :main ^:skip-aot eddy.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
