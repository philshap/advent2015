(ns day9
  (:require [clojure.string :as str])
  (:require [clojure.data.priority-map :refer [priority-map]]))

(defn parse-line [line]
  (let [[_ node1 node2 distance] (re-matches #"(\w+) to (\w+) = (\d+)" line)]
    [node1 node2 (read-string distance)]))

(def input
  (->> (slurp "src/day9-input.txt")
       str/split-lines
       (map parse-line)))

;; could simplify by storing both X->Y and Y->X route costs
(defn neighbors [graph path]
  (let [node (last path)]
    (->> graph
         (map
           (fn [[node1 node2 cost]]
             (cond
               (and (= node1 node) (or (not-any? (partial = node2) path)
                                       (and (= (count path) 8)
                                            (= node2 (first path))))) [node2 cost]
               (and (= node2 node) (or (not-any? (partial = node1) path)
                                       (and (= (count path) 8)
                                            (= node1 (first path))))) [node1 cost])))
         (remove nil?))))

(defn extend-path [graph [path cost]]
  (map
    (fn [[node node-cost]]
      {(conj path node) (+ cost node-cost)})
    (neighbors graph path)))

(defn extend-paths [graph select-path paths]
  (let [[key _ :as path] (select-path paths)]
    (into (dissoc paths key) (extend-path graph path))))

(defn path-length [graph all-nodes start]
  (->> (priority-map [start] 0)
       (iterate (partial extend-paths graph first))
       (map (fn [path] (first (sort #(> (count (first %1)) (count (first %2))) path))))
       (drop-while #(not= (count (first %)) (count all-nodes)))
       (take 2)
       first
       last
       ))

(defn part1 []
  (let [all-nodes (into [] (into #{} (mapcat (juxt first second) input)))]
    (->> all-nodes
         (map (partial path-length input all-nodes))
         sort
         first)))

;(comment
;(println "part 1: " (part1))
;(println "part 2: " (part2))
;)