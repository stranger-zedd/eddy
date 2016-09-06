(ns eddy.core
  (:gen-class)
  (:require [eddy.schedule :refer [schedule-jobs sch]]
            [eddy.schedule.calendar-interval :refer [every]]
            [eddy.data :as data]
            [eddy.content :as content]
            [eddy.mail :as mail]
            [clj-time.core :as t]
            [clojurewerkz.quartzite.jobs :refer [defjob]]))

(defn -main [& args]
  (let [now (t/date-time 2016 6 2)
        changed-objects (data/leaves-since-last-check)
        email-body (content/render (data/get-email-template) changed-objects)]
    (if (not (empty? changed-objects))
      (doseq [address (data/get-mailing-list)]
        (mail/send-message address email-body)
        (Thread/sleep (mail/sender-delay))))
    (data/put-last-check now)))
