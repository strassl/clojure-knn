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

(defn -main
  [& args]
  (let [path (nth *command-line-args* 0)
        k (read-string (nth *command-line-args* 1))
        sample (read-string (nth *command-line-args* 2))
        population (read-data path)
        result (apply classify k vec-dist sample population)]
    (println result)))
