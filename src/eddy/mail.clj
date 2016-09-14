(ns eddy.mail
  (:require [postal.core :as postal]
            [environ.core :refer [env]]))

(def email-user (env :email-user))
(def email-pass (env :email-pass))
(def email-host (env :email-host))
(def email-delay (env :email-delay 70))

(defn send-message [address, body]
  (postal/send-message {:user email-user
                        :pass email-pass
                        :host email-host
                        :tls true
                        :port 587}
                       {:from "notifications@zephyrus.godai.xyz"
                        :to address
                        :subject "New content on Zephyrus"
                        :body [{:type "text/html"
                                :content body}]}))

(defn sender-delay []
  email-delay)
