(ns en-ru-cards.schedule
  (:use [en-ru-cards.tweet]
        [en-ru-cards.storage]
        [en-ru-cards.translate]
        [en-ru-cards.frequency])
  (:require [cronj.core :as cj]))

(defn get-word-and-tweet [storage vocabulary]
  (let [word (random-word vocabulary storage)
        tweet (create-tweet-for-word word)]
    (if (nil? tweet)
      (get-word-and-translation vocabulary storage)
      [word tweet])))

(defn tweet-new-card [dt {storage-file :storage-file vocabulary-file :vocabulary-file}]
  (let [storage (load-storage storage-file)
        vocabulary (read-vocabulary vocabulary-file)
        [word message] (get-word-and-tweet storage vocabulary)]
    (store storage-file storage word)
    (tweet message)))

(defn run [period vocabulary-file storage-file]
  (cj/defcronj hn
    :entries [{:id "en-ru-card"
               :oprs {:storage-file storage-file
                      :vocabulary-file vocabulary-file}
               :handler tweet-new-card
               :schedule (str "0 /" period " * * * * *")
               }])
  (cj/start! hn))

