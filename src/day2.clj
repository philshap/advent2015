(ns day2
  (:require [clojure.string :as str]
            [clojure.edn :as edn]))

(defn parse-line [line]
  (->> (str/split line #"x")
       (map edn/read-string)))

(def input
  (->> "src/day2-input.txt"
       slurp
       str/split-lines
       (map parse-line)))

(defn paper-needed [[x y z]]
  (let [sides [(* x y) (* x z) (* y z)]]
    (+ (reduce + (map (partial * 2) sides)) (reduce min sides))))

(defn part1 []
  (->> input
       (map paper-needed)
       (reduce +)))

(defn ribbon-needed [xyz]
  (+ (reduce * xyz)
     (reduce + (map (partial * 2) (take 2 (sort xyz))))))

;; part 2 => 3811321 too low

(defn part2 []
  (->> input
       (map ribbon-needed)
       (reduce +)))

(comment
  (println "part 1: " (part1))
  (println "part 2: " (part2))
  )