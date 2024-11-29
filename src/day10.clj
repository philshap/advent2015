(ns day10)

(def input "3113322113")

(defn look-and-say-one [[[digit & tail] current count output]]
  (if (= current digit)
    [tail digit (inc count) output]
    [tail digit 1 (str output count current)]))

(defn look-and-say [[digit & tail]]
  (->> [tail digit 1 ""]
       (iterate look-and-say-one)
       (drop-while #(not (nil? (second %))))
       first
       last))

(defn part1 []
  (->> input
       (iterate look-and-say)
       (drop 40)
       first
       count))

(defn part2 []
  (->> input
       ))

(comment
  (println "part 1: " (part1))
  (println "part 2: " (part2))
  )
