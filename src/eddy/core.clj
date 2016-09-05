(ns eddy.core
  (:gen-class)
  (:require [eddy.schedule :refer [schedule-jobs sch]]
            [eddy.schedule.calendar-interval :refer [every]]
            [eddy.data :as data]
            [eddy.content :as content]
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
    (postal/send-message {:from "blah@blah"
                          :to "someone@somewhere"
                          :subject "Something"
                          :body (content/add-content (data/get-email-template) changed-objects)})))
