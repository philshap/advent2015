(ns day1)

(def input
  (-> "src/day1-input.txt"
      slurp))

(def move->val {\( 1, \) -1})

(defn part1 []
  (reduce (fn [acc move]
            (+ acc (move->val move))) 0 input))

(defn part2 []
  (let [moves (into [] (map move->val input))]
    (->> (range (count input))
         (map #(reduce + (subvec moves 0 %)))
         (take-while (complement neg-int?))
         count)))

(comment
  (println "part 1: " (part1))
  (println "part 2: " (part2))
  )