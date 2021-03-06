(ns en-ru-cards.frequency
  (:use [clojure.java.io :only (reader)]
        [clojure.string :only (split)]
        [clojure.java.shell]))

(defn is-good-word [word]
  (or
   (->> "'" (.contains word) not)
   (->> "s" (.endsWith word) not)))

(defn read-vocabulary [file]
  (with-open [rdr (reader file)]
    (->> rdr
         line-seq
         (filter is-good-word)
         (map #(split % #" "))
         (map first)
         (drop 1000)
         vec)))

(defn random-word [vocab exclude]
  (->> vocab
       repeat
       (map rand-nth)
       (filter (comp not #(some (partial = %) exclude)))
       first))
