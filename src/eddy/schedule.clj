(ns eddy.schedule
  (:require [clojurewerkz.quartzite.scheduler :as quartzite]
            [clojurewerkz.quartzite.jobs :as jobs]
            [clojurewerkz.quartzite.triggers :as triggers]))

(defmacro job [job-type key]
  `(jobs/build
    (jobs/of-type ~job-type)
    (jobs/with-identity (jobs/key ~key))))

(defn interval-class [interval-key]
  (let [namespace (str
                   "clojurewerkz.quartzite.schedule."
                   (case interval-key
                     :every "calendar-interval"
                     (throw (IllegalArgumentException. "Interval syntax invalid"))))]
    (require (symbol namespace))
    namespace))

(defn calendar-interval-function [unit]
  (case (str unit)
    "second" "with-interval-in-seconds"
    "seconds" "with-interval-in-seconds"
    "minute" "with-interval-in-minutes"
    "minutes" "with-interval-in-minutes"
    "hour" "with-interval-in-hours"
    "hours" "with-interval-in-hours"
    (throw (IllegalArgumentException. "Interval syntax invalid"))))

(defn calendar-interval-operands [operand-data]
  (if (not (= (count operand-data) 2))
    (throw (IllegalArgumentException. "Calendar interval syntax invalid")))

  (let [interval (int (first operand-data))
        unit (second operand-data)]
    (list (list (calendar-interval-function unit) interval))))

(defn interval-operands [namespace operand-data]
  (let [operands (case namespace
                   "clojurewerkz.quartzite.schedule.calendar-interval" (calendar-interval-operands operand-data)
                   (throw (IllegalArgumentException. "Interval syntax invalid")))]
    (map #(cons (symbol namespace (first %)) (rest %)) operands)))

(defn make-schedule [interval-data]
  (let [operator (first interval-data)
        namespace (interval-class operator)
        operands (interval-operands namespace (next interval-data))]
    (println namespace)
    (cons (symbol namespace "schedule")
          operands)))

(defmacro trigger [job-key interval]
  `(triggers/build
    (triggers/with-identity (triggers/key ~job-key))
    (triggers/start-now)
    (triggers/with-schedule ~(make-schedule interval))))

(defn schedule-jobs [& jobs]
  (let [scheduler (-> (quartzite/initialize) quartzite/start)]
    (doseq [job jobs]
      (quartzite/schedule scheduler (get job :job) (get job :trigger)))))

(defmacro sch [job-type & interval]
  (let [job-id (str "jobs." (str job-type) "." (. (str (gensym)) toLowerCase))
        job-class (resolve job-type)]
    `{:job (job ~job-class ~job-id) :trigger (trigger ~job-id ~interval)}))
