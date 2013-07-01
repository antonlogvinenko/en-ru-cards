(ns en-ru-cards.schedule
  (:use [en-ru-cards.tweet]
        [en-ru-cards.storage]
        [en-ru-cards.translate]
        [en-ru-cards.frequency])
  (:require [cronj.core :as cj]))

(defn get-word-and-translation [storage vocabulary]
  (let [word (random-word vocabulary storage)
        translation (get-translation word)
        transcription (get-transcription word)]
    (if (nil? translation)
      (get-word-and-translation vocabulary storage)
      [word transcription translation])))

(defn capitalize [word]
  (str (.toUpperCase (.substring word 0 1)) (apply str (rest word))))

(defn tweet-new-card [dt {storage-file :storage-file vocabulary-file :vocabulary-file}]
  (let [storage (load-storage storage-file)
        vocabulary (read-vocabulary vocabulary-file)
        [word transcription translation] (get-word-and-translation storage vocabulary)]
    (println word translation)
    (store storage-file storage word)
    (tweet (str (capitalize word)
                " " "[" transcription "]" " "
                (capitalize translation)))))

(defn run [period vocabulary-file storage-file]
  (cj/defcronj hn
    :entries [{:id "en-ru-card"
               :opts {:storage-file storage-file
                      :vocabulary-file vocabulary-file}
               :handler tweet-new-card
               :schedule (str "0 0 " "/" period " * * * *")
               }])
  (cj/start! hn))

