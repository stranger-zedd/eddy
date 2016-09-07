(ns eddy.core
  (:gen-class)
  (:require [eddy.schedule :refer [schedule-jobs sch]]
            [eddy.schedule.calendar-interval :refer [every]]
            [eddy.data :as data]
            [eddy.content :as content]
            [eddy.mail :as mail]
            [clojure.string :refer [join]]
            [clj-time.core :as t]
            [clojure.tools.logging :as log]
            [clojurewerkz.quartzite.jobs :refer [defjob]]))

(defjob check-and-mail [ctx]
  (log/info "Checking for changed objects...")
  (let [now (t/now)
        changed-objects (data/leaves-since-last-check)
        email-body (content/render (data/get-email-template) changed-objects)]
    (if (not (empty? changed-objects))
      (do
        (log/info (str "Found changed objects:" (join ", " changed-objects)))
        (doseq [address (data/get-mailing-list)]
          (log/info (str "Sending notification to " address))
          (mail/send-message address email-body)
          (Thread/sleep (mail/sender-delay)))))
    (log/info (str "Saving last-check: " now))
    (data/put-last-check now)))

(defn -main [& args]
  (schedule-jobs
   (sch check-and-mail (every 1 hour))))
