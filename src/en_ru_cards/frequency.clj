(ns en-ru-cards.frequency
  (:use [clojure.java.io :only (reader)]
        [clojure.string :only (split)]))

(defn is-bad-word [word]
  (->> "'"
       (.contains word)
       not))

(defn read-vocabulary [file]
  (with-open [rdr (reader file)]
    (->> rdr
         line-seq
         (filter is-bad-word)
         (map #(split % #" "))
         (map first)
         vec)))

(defn random-word [vocab exclude]
  (->> vocab
       repeat
       (map rand-nth)
       (filter (comp not #(some (partial = %) exclude)))
       first))
