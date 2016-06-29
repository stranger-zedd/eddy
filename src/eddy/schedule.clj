(ns eddy.schedule
  (:require [clojurewerkz.quartzite.scheduler :as quartzite]
            [clojurewerkz.quartzite.jobs :as jobs :refer [defjob]]
            [clojurewerkz.quartzite.triggers :as triggers]
            [clojurewerkz.quartzite.schedule.calendar-interval :refer [schedule with-interval-in-seconds]]))

(defjob NoOpJob
  [ctx]
  (println "Something"))

(defn job [job-type key]
  (jobs/build
   (jobs/of-type job-type)
   (jobs/with-identity (jobs/key key))))

(defn trigger [job-key interval]
  (triggers/build
   (triggers/with-identity (triggers/key job-key))
   (triggers/start-now)
   (triggers/with-schedule (schedule
                            (with-interval-in-seconds interval)))))

(def jobs
  (list
   {:job (job NoOpJob "jobs.mail") :trigger (trigger "jobs.mail" 2)}))

(defn run []
  (let [scheduler (-> (quartzite/initialize) quartzite/start)]
    (doseq [job jobs]
      (quartzite/schedule scheduler (get job :job) (get job :trigger)))))
