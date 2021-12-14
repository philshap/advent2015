(ns day6
  (:require [clojure.string :as str]
            [clojure.edn :as edn]))

(def input
  (-> "src/day6-input.txt"
      slurp
      str/split-lines))

(defn parse-rule [on off toggle line]
  (let [[_ op & coords] (re-matches #"(.*) (\d+),(\d+) through (\d+),(\d+)" line)
        [x1 y1 x2 y2] (map edn/read-string coords)
        inside? (fn [[x y]] (and (>= x x1) (<= x x2)
                                 (>= y y1) (<= y y2)))]
    (case op
      "turn on" (fn [light pos] (if (inside? pos) (on light) light))
      "turn off" (fn [light pos] (if (inside? pos) (off light) light))
      "toggle" (fn [light pos] (if (inside? pos) (toggle light) light)))))

(defn apply-rules [start rules pos]
  (reduce (fn [lit op] (op lit pos)) start rules))

(defn light-up-grid [start on off toggle]
  (let [rules (map (partial parse-rule on off toggle) input)]
    (->> (for [y (range 1000), x (range 1000)] [x y])
         (map (partial apply-rules start rules)))))

(defn part1 []
  (->>
    (light-up-grid false (constantly true) (constantly false) not)
    (filter true?)
    count))

(defn part2 []
  (->>
    (light-up-grid 0 inc #(max 0 (dec %)) (partial + 2))
    (reduce +)))

(comment
  (println "part 1: " (part1))
  (println "part 2: " (part2))
  )