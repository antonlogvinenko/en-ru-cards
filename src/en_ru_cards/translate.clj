(ns en-ru-cards.translate
  (:use [clojure.java.shell]
        [clojure.string :only [split]])
  (:require [clojure.data.json :as json]))

(defn debug [x] (println x) x)

(def url "http://glosbe.com/gapi/translate?from=eng&dest=rus&format=json&pretty=true&phrase=")

(defn validate [translation]
  (println translation)
  (if (empty? translation)
    (-> "Not translated" RuntimeException. throw)
    translation))

(defn get-article [word]
  (->> word
       (str url)
       slurp
       json/read-json))

(defn get-translation [article word n]
  (->> article
       :tuc
       (map :phrase)
       (filter #(= (:language %) "rus"))
       (map :text)
       (filter (comp not nil?))
       (take n)
       (interpose ", ")
       (apply str)
       validate))

(defn create-tweet-for-word [word]
  (let [prefix-length (+ 3 (count word))
        article (get-article word)
        translation (->> 30
                         (range 1)
                         reverse
                         (map (partial get-translation article word))
                         (filter #(< (count %) (- 140 prefix-length)))
                         first)]
    (str (.toUpperCase word) ": " translation ".")))
