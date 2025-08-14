(ns knockout.core
  (:require [clojure.java.io :as io]
            [clojure.tools.cli :as cli])
  (:gen-class))

(defn read-names [file-path]
  (with-open [rdr (io/reader file-path)]
    (vec (line-seq rdr))))

(defn deterministic-shuffle
  [contestants ^java.util.Random rng]
  (let [rnd (fn [_] (.nextInt rng))]
    (sort-by rnd contestants)))

(defn advance-winners
  ([contestants] (advance-winners contestants (java.util.Random.)))
  ([contestants ^java.util.Random rng]
   (take (quot (count contestants) 2)
         (deterministic-shuffle contestants rng))))

(defn tournament-rounds
  ([contestants] (tournament-rounds contestants (java.util.Random.)))
  ([contestants seed]
   (let [rng (if (instance? java.util.Random seed)
               seed
               (java.util.Random. (long seed)))]
     (->> contestants
          (iterate #(advance-winners % rng))
          (take-while seq)))))

(defn render-round [round-number contestants]
  (let [prefix (apply str (repeat round-number "  "))]
    (if (= 1 (count contestants))
      (str prefix "Winner:" (first contestants))
      (str prefix "Round " (inc round-number) ":" (pr-str contestants)))))

(defn render-bracket [rounds]
  (map-indexed render-round rounds))

(def cli-options
  [["-f" "--file FILE" "File with contestant names"
    :default "resources/names.txt"]
   ["-s" "--seed SEED" "Seed for random number generator"
    :parse-fn #(Long/parseLong %)
    :validate [#(< 0 % 0x100000000) "Must be a 64-bit integer"]] 
   ["-i" "--interactive" "Run in interactive mode"]
   ["-h" "--help"]])

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (cli/parse-opts args cli-options)
        {:keys [file seed interactive help]} options]
    (when help
      (println summary)
      (System/exit 0))
    (when errors
      (println (clojure.string/join "\n" errors))
      (System/exit 1))
    (let [contestants (read-names file)
          rounds      (if seed
                        (tournament-rounds contestants seed)
                        (tournament-rounds contestants))]
      (->> rounds
           render-bracket
           (run! (fn [line]
                   (when interactive (read-line))
                   (println line)))))))
