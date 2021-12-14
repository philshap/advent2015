(ns day4
  (:require [clojure.string :as str])
  (:import (java.security MessageDigest)))

; https://gist.github.com/jizhang/4325757#gistcomment-2196746
(defn md5 [^String s]
  (let [algorithm (MessageDigest/getInstance "MD5")
        raw (.digest algorithm (.getBytes s))]
    (format "%032x" (BigInteger. 1 raw))))

(def input "ckczppom")

(defn is-target-md5? [target n]
  (-> (str input n)
      md5
      (str/starts-with? target)))

(defn find-md5-match [target]
  (->> (range)
       (drop-while (complement (partial is-target-md5? target)))
       first))

(defn part1 []
  (find-md5-match "00000"))

(defn part2 []
  (find-md5-match "000000"))

(comment
  (println "part 1: " (part1))
  (println "part 2: " (part2))
  )