(ns en-ru-cards.translate
  (:use [clojure.java.shell]
        [clojure.string :only [split]])
  (:require [clojure.data.json :as json])
  (:import [java.nio.charset Charset]))

(def url "https://www.googleapis.com/language/translate/v2?key=AIzaSyC3JN8PuA_gS7Jj_XSJv1fyb1UxmlSKl6s&source=en&target=ru&q=")

(defn validate [word translation]
  (cond
   (= word translation) nil
   (nil? translation) nil
   :else translation))

(defn my-slurp [url]
  (slurp url :encoding "UTF-8"))

(defn from-braces [word]
  (-> word (split #"\[") second (split #"\]") first))

(defn get-transcription [word]
  (let [lines (-> (sh "sdcv" "--utf8-output" "-n"
                "--data-dir" "/Users/anton/Downloads/stardict-ER-LingvoUniversal-2.4.2/" word)
                  :out
                  (split #"\n"))]
    (->> lines
         (filter #(.contains % "["))
         first
         from-braces
         )))

(defn get-translation [word]
  (->> word
       (str url)
       my-slurp
       json/read-json
       :data
       :translations
       first
       :translatedText
       (validate word)))
