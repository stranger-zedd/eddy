(ns eddy.core
  (:gen-class)
  (:require [eddy.schedule :refer [schedule-jobs sch]]
            [eddy.schedule.calendar-interval :refer [every]]
            [eddy.data :as data]
            [postal.core :as postal]
            [clj-time.core :as t]
            [clojurewerkz.quartzite.jobs :refer [defjob]]))

(defjob MailJob [ctx]
  (println "sending?")
  (postal/send-message {:from "blah@blah"
                        :to "someone@somewhere"
                        :subject "Something"
                        :body "Abc"})
  (println "sent?"))

(defn -main [& args]
  (let [now (t/date-time 2016 6 2)
        changed-objects (data/leaves-since-last-check)]
    (doseq [x changed-objects]
      (println (get x :key)))
    (data/put-last-check now)
    (println (str (data/get-last-check)))))
