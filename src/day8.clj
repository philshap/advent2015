(ns day8
  (:require [clojure.string :as str]))

(def input
  (-> "src/day8-input.txt"
      slurp
      str/split-lines))

(defn replace-codes [s]
  ;; all escape sequences used are valid except \xff, so remove it first
  (read-string (str/replace s #"\\x[a-f0-9][a-f0-9]" "x")))

(defn part1 []
  (->> input
       (map #(- (count %) (count (replace-codes %))))
       (reduce +)))

(defn part2 []
  (->> input
       (map #(- (count (pr-str %)) (count %)))
       (reduce +)))

(comment
  (println "part 1: " (part1))
  (println "part 2: " (part2))
  )