(ns day5
  (:require [clojure.string :as str]))

(def input
  (-> "src/day5-input.txt"
      slurp
      str/split-lines))

(def vowels (set "aeiou"))
(defn three-vowels? [s]
  (> (count (filter vowels s)) 2))
(def forbidden ["ab", "cd", "pq", "xy"])
(defn has-forbidden? [s]
  (some #(str/index-of s %) forbidden))
(defn has-double? [s]
  (some (partial apply =) (partition 2 1 s)))

(defn part1 []
  (->> input
       (filter three-vowels?)
       (filter has-double?)
       (remove has-forbidden?)
       count))

(defn has-two-pairs? [s]
  (re-find #".*(..).*\1.*" s))

(defn has-repeat-around? [s]
  (re-find #".*(.).\1.*" s))

(defn part2 []
  (->> input
       (filter has-two-pairs?)
       (filter has-repeat-around?)
       count))

(comment
  (println "part 1: " (part1))
  (println "part 2: " (part2))
  )