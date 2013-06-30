(ns en-ru-cards.tweet
  (:use [clojure.string :only (split)]
        [twitter.oauth]
        [twitter.api.restful]))

(def twitter-creds (apply make-oauth-creds (-> "secret/twitter" slurp (split #"\n"))))

(defn tweet [message]
  (statuses-update :oauth-creds twitter-creds
                   :params {:status message}))

