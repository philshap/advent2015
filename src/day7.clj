(ns day7
  (:require [clojure.string :as str]
            [clojure.edn :as edn]))

(def input
  (-> "src/day7-input.txt"
      slurp
      str/split-lines))

(defn maybe-parse [arg]
  (let [parsed (edn/read-string arg)]
    (if (number? parsed) parsed arg)))

(defn parse-wiring [line]
  (let [[direct d-in d-out] (re-matches #"(.+) -> (.+)" line)
        [binop b-in1 b-op b-in2 b-out] (re-matches #"(.+) ((?:AND)|(?:OR)) (.+) -> (.+)" line)
        [shift s-in1 s-op s-value s-out] (re-matches #"(.+) ((?:LSHIFT)|(?:RSHIFT)) (\d+) -> (.+)" line)
        [not-op n-in1 n-out] (re-matches #"NOT (.+) -> (.+)" line)]
    (cond
      binop {:op (keyword b-op), :in1 (maybe-parse b-in1), :in2 (maybe-parse b-in2), :out b-out}
      shift {:op (keyword s-op), :in1 (maybe-parse s-in1), :value (maybe-parse s-value), :out s-out}
      not-op {:op :NOT, :in1 (maybe-parse n-in1), :out n-out}
      direct {:op :=, :in1 (maybe-parse d-in), :out d-out}
      :else (println "bad instruction " + line))))

(defn mask-16bit [v]
  (bit-and (dec (bit-shift-left 1 16)) v))

(defn eval-wiring [wires wiring]
  (let [in1 (if (number? (:in1 wiring)) (:in1 wiring) (wires (:in1 wiring)))
        in2 (if (number? (:in2 wiring)) (:in2 wiring) (wires (:in2 wiring)))
        out (:out wiring)]
    (case (:op wiring)
      := (if in1 [out in1])
      :NOT (if in1 [out (bit-not in1)])
      :AND (if (and in1 in2) [out (bit-and in1 in2)])
      :OR (if (and in1 in2) [out (bit-or in1 in2)])
      :LSHIFT (if in1 [out (bit-shift-left in1 (:value wiring))])
      :RSHIFT (if in1 [out (bit-shift-right in1 (:value wiring))]))))

(defn next-cycle [[wires circuit]]
  (reduce
    (fn [_ wiring]
      (if-let [[out value] (eval-wiring wires wiring)]
        (reduced [(conj wires [out (mask-16bit value)])
                  (remove (partial = wiring) circuit)])))
    nil circuit))

(defn get-wire-value [initial-wires circuit wire]
  (->> [initial-wires circuit]
       (iterate next-cycle)
       (drop-while #(nil? ((first %) wire)))
       (#((ffirst %) wire))))

(defn part1 []
  (get-wire-value {} (map parse-wiring input) "a"))

(defn part2 []
  (let [circuit (->> (map parse-wiring input)
                     (remove #(= (% :out) "b")))]
    (get-wire-value {"b" (part1)} circuit "a")))

(comment
  (println "part 1: " (part1))
  (println "part 2: " (part2))
  )