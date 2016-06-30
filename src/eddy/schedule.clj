(ns eddy.schedule
  (:require [clojurewerkz.quartzite.scheduler :as quartzite]
            [clojurewerkz.quartzite.jobs :as jobs]
            [clojurewerkz.quartzite.triggers :as triggers]
            [clojurewerkz.quartzite.schedule.calendar-interval :refer [schedule with-interval-in-seconds]]))

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

(defn schedule-jobs [& jobs]
  (let [scheduler (-> (quartzite/initialize) quartzite/start)]
    (doseq [job jobs]
      (quartzite/schedule scheduler (get job :job) (get job :trigger)))))

(defmacro sch [job-type interval]
  (let [job-id (str "jobs." (str job-type) "." (. (str (gensym)) toLowerCase))
        job-class (resolve job-type)]
    `{:job (job ~job-class ~job-id) :trigger (trigger ~job-id ~interval)}))
