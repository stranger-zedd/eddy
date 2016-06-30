(ns eddy.core
  (:require [eddy.schedule :refer [schedule-jobs sch]]
            [clojurewerkz.quartzite.jobs :refer [defjob]]))

(defjob NoOpJob
  [ctx]
  (println "Something"))

(defn -main [& args]
  (schedule-jobs
   (sch NoOpJob :every 1 second)))
