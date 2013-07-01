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
   :else (String. (.getBytes translation) (Charset/forName "UTF-8"))))

(defn get-translation [word]
  (->> word
       (str url)
       slurp
       json/read-json
       :data
       :translations
       first
       :translatedText
       (validate word)))
