(ns en-ru-cards.core
  (:gen-class :main true)
  (:use [en-ru-cards.schedule]))

(defn -main [period storage vocabulary]
  (run period vocabulary storage))
