(ns clojure-knn.core
  (:gen-class)
  (:use clojure-csv.core))

(defrecord Sample [location label])

(defn k-nearest
  [k distance & values]
  (take k
        (sort-by distance values)))

(defn class-frequencies
  [& population]
  (frequencies
    (map :label population)))

(defn dominating-class
  [& population]
  (key (apply max-key val (apply class-frequencies population))))

(defn classify
  [k metric sample & population]
  (let [dist (fn [x] (metric sample (:location x)))]
    (apply dominating-class (apply k-nearest k dist population))))

(defn dot
  [a b]
  (reduce + (map * a b)))

(defn norm
  [v]
  (Math/sqrt (dot v v)))

(defn vec-dist
  [a b]
  (norm (map - a b)))

(defn read-data
  [path]
  (map (fn [row] (Sample. (into [] (map read-string (butlast row)))
                          (last row)))
       (parse-csv (slurp path))))

(defn loss
  [classify, validation]
  (let [classify-sample (fn [sample] (classify (:location sample)))
        correct-class (fn [sample] (if (= (classify-sample sample)(:label sample)) true false))
        results (frequencies (map correct-class validation))
        incorrect (get results false)
        correct (get results true)
        incorrect-ratio (/ incorrect (+ correct incorrect))
        ]
    incorrect-ratio))

(defn -main
  [& args]
  (let [train-path (nth *command-line-args* 0)
        val-path (nth *command-line-args* 1)
        k (read-string (nth *command-line-args* 2))
        sample (read-string (nth *command-line-args* 3))
        validation (read-data val-path)
        population (read-data train-path)
        val-loss (loss (fn [sample] (apply classify k vec-dist sample population)) validation)
        result (apply classify k vec-dist sample population)]
    (do (println result)
        (println val-loss))))
