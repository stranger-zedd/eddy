(ns eddy.core
  (:require [eddy.schedule :as schedule]))


(defn -main [& args]
  (schedule/run))
