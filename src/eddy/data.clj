(ns eddy.data
  (:require [amazonica.aws.s3 :refer [get-object list-objects]]
            [clojure.java.io :refer [reader]]))

(defn get-leaves [bucket]
  (filter #(re-find #"[^\/]$" (get % :key))
          (get (list-objects bucket) :object-summaries)))

(defn leaves-after [bucket time]
  (filter #(> 0 (compare time (get % :last-modified)))
          (get-leaves)))
