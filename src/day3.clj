(ns day3)

(def input
  (-> "src/day3-input.txt"
      slurp))

(def dir->delta {\^ [0 -1], \v [0 1], \> [1 0], \< [-1 0]})

(defn part1 []
  (loop [pos [0 0]
         visit #{pos}
         moves (map dir->delta input)]
    (if (empty? moves)
      (count visit)
      (let [[dx dy] (first moves)
            new-pos [(+ (first pos) dx) (+ (last pos) dy)]]
        (recur new-pos (conj visit new-pos) (rest moves))))))

(defn part2 []
  (loop [s-pos [0 0]
         r-pos [0 0]
         visit #{s-pos}
         moves (map dir->delta input)]
    (if (empty? moves)
      (count visit)
      (if (even? (count moves))
        (let [[dx dy] (first moves)
              new-pos [(+ (first s-pos) dx) (+ (last s-pos) dy)]]
          (recur new-pos r-pos (conj visit new-pos) (rest moves)))
        (let [[dx dy] (first moves)
              new-pos [(+ (first r-pos) dx) (+ (last r-pos) dy)]]
          (recur s-pos new-pos (conj visit new-pos) (rest moves)))))))

(comment
  (println "part 1: " (part1))
  (println "part 2: " (part2))
  )
