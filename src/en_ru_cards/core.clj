(ns en-ru-cards.core
  (:gen-class :main true)
  (:use [en-ru-cards.schedule]))

(defn -main [period vocabulary storage]
  (run period vocabulary storage))
