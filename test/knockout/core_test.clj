(ns knockout.core-test
  (:require [clojure.test :refer :all]
            [knockout.core :refer :all]))

(deftest deterministic-shuffle-test
  (testing "deterministic-shuffle with a fixed seed"
    (let [contestants ["a" "b" "c" "d"]
          rng (java.util.Random. 42)]
      (is (= ["c" "b" "d" "a"] (vec (deterministic-shuffle contestants rng)))))))

(deftest advance-winners-test
  (testing "advance-winners with a fixed seed"
    (let [contestants ["a" "b" "c" "d"]
          rng (java.util.Random. 42)]
      (is (= ["c" "b"] (vec (advance-winners contestants rng)))))))

(deftest tournament-rounds-test
  (testing "tournament-rounds with a fixed seed"
    (let [contestants ["a" "b" "c" "d" "e" "f" "g" "h"]]
      (is (= [["a" "b" "c" "d" "e" "f" "g" "h"]
              ["c" "g" "e" "b"]
              ["b" "g"]
              ["g"]]
             (mapv vec (tournament-rounds contestants 42)))))))

(deftest render-round-test
  (testing "render-round"
    (is (= (str "  Round 2:" (pr-str ["a" "b"])) (render-round 1 ["a" "b"])))
    (is (= "      Winner:a" (render-round 3 ["a"])))))

(deftest render-bracket-test
  (testing "render-bracket"
    (let [rounds [["a" "b" "c" "d"]
                  ["a" "d"]
                  ["a"]]]
      (is (= [(str "Round 1:" (pr-str ["a" "b" "c" "d"]))
              (str "  Round 2:" (pr-str ["a" "d"]))
              "    Winner:a"]
             (render-bracket rounds))))))
