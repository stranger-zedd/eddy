(ns eddy.data
  (:require [amazonica.aws.s3 :refer [get-object put-object list-objects]]
            [clj-time.format :as time-format]
            [environ.core :refer [env]]
            [clojure.string :as string]
            [clojure.java.io :refer [reader]]))

(def zephyrus-bucket (env :zephyrus-bucket))
(def data-bucket (env :data-bucket))

(defn get-leaves [bucket]
  (filter #(re-find #"[^\/]$" (get % :key))
          (get (list-objects bucket) :object-summaries)))

(defn leaves-after [bucket time]
  (filter #(> 0 (compare time (get % :last-modified)))
          (get-leaves bucket)))

(defn get-data [bucket path]
  (let [s3-response (get-object bucket path)]
    (slurp (reader (get s3-response :object-content)))))

(defn put-data [bucket path data]
  (let [bytes (.getBytes data "UTF-8")
        input-stream (java.io.ByteArrayInputStream. bytes)]
    (put-object :bucket-name bucket
                :key path
                :input-stream input-stream
                :metadata {:content-length (count bytes)})))

(defn get-last-check []
  (time-format/parse (get-data data-bucket "last-check")))

(defn get-email-template []
  (get-data data-bucket "template.html"))

(defn get-mailing-list []
  (string/split (get-data data-bucket "subscribers.txt") #"\n"))

(defn put-last-check [date]
  (put-data data-bucket "last-check" (str date)))

(defn leaves-since-last-check []
  (leaves-after zephyrus-bucket (get-last-check)))
