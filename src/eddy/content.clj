(ns eddy.content
  (:require [clojure.string :as string]
            [ring.util.codec :refer [url-encode]]))

(defn url-encode-parts [s3-key]
  (string/join "/" (map #(url-encode %) (string/split s3-key #"\/"))))

(defn zephyrus-url [s3-key]
  (str "http://zephyrus.godai.xyz/" (url-encode-parts s3-key)))

(defn zephyrus-post-name [s3-key]
  (string/replace (re-find #"(?<=_)[^_]+$" s3-key) "-" " "))

(defn zephyrus-link [s3-key]
  (str "<li><a href=\"" (zephyrus-url s3-key) "\">"
       (zephyrus-post-name s3-key)
       "</a></li>"))

(defn zephyrus-links [content]
  (string/join "\n" (map #(zephyrus-link (get % :key)) content)))

(defn add-content [template content]
  (string/replace-first template "{{content-links}}" (zephyrus-links content)))
