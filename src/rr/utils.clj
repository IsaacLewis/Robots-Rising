(ns rr.utils)

(defn shift-map
  "Similar to assoc, but sets the value of key in map to its existing value plus val."
  ([map key val]
     (assoc map key (+ (map key) val)))
  ([map key val & kvs]
     (apply shift-map (shift-map map key val) kvs))) 

(defn without [item coll]
  (remove #(= item %) coll))

(defn includes? [item coll]
  (some #{item} coll))

(defmacro log-eval [form]
  `(do (println (macroexpand (quote ~form)))
       (eval ~form)))

(defmacro if-equal 
  ([item1 item2 true-form]
     `(if (= ~item1 ~item2) ~true-form))
  ([item1 item2 true-form false-form]
     `(if (= ~item1 ~item2) ~true-form ~false-form)))

(defmacro unless-equal 
  ([item1 item2 true-form]
     `(if (not (= ~item1 ~item2)) ~true-form))
  ([item1 item2 true-form false-form]
     `(if (not (= ~item1 ~item2)) ~true-form ~false-form)))

(defn split-by [pred coll]
  (loop [remainder-seq coll true-seq [] false-seq []]
    (if (empty? remainder-seq) (list true-seq false-seq)
	(let [first (first remainder-seq)]
	  (if (pred first)
	    (recur (rest remainder-seq) (conj true-seq first) false-seq)
	    (recur (rest remainder-seq) true-seq (conj false-seq first)))))))

(defn unix-time []
  (Math/round (Math/floor (/ (System/currentTimeMillis) 1000))))

(defn div [dividend divisor]
  "Divides dividend by divisor, returning a vector [quotient remainder]"
  (let [quotient (int (Math/floor (/ dividend divisor)))]
    [quotient (- dividend (* quotient divisor))]))

(defn r [string] (re-gsub rregex (fn [match] (load-string (str "(" (second match) ")"))) string))