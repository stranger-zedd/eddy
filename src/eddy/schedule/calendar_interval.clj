(ns eddy.schedule.calendar-interval
  (:require [clojurewerkz.quartzite.schedule.calendar-interval]))

(def schedule-ns
  "clojurewerkz.quartzite.schedule.calendar-interval")

(defn- interval-function [unit]
  (symbol schedule-ns
          (case (str unit)
            "second" "with-interval-in-seconds"
            "seconds" "with-interval-in-seconds"
            "minute" "with-interval-in-minutes"
            "minutes" "with-interval-in-minutes"
            "hour" "with-interval-in-hours"
            "hours" "with-interval-in-hours"
            (throw (IllegalArgumentException. "Interval syntax invalid")))))

(defn- interval-operands [operand-data]
  (if (not (= (count operand-data) 2))
    (throw (IllegalArgumentException. "'Every' expects interval operands be in the format (<number> <unit>)'")))

  (let [interval (int (first operand-data))
        unit (second operand-data)]
    (list (interval-function unit) interval)))

(defmacro every [& schedule-data]
  `(clojurewerkz.quartzite.schedule.calendar-interval/schedule
    ~(interval-operands schedule-data)))
