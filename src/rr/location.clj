(ns rr.location
  (:use (rr db utils)))

(defn location [entity]
  (fetch-tile (entity :x) (entity :y) (or (entity :z) 0)))

(defn outside? [entity] (= (entity :z) 0))

(defn inside? [entity] (not (outside? entity)))

(defn enterable? [location] (:enterable location))
  
(defn same-location? [obj1 obj2]
  (and (= (obj1 :x) (obj2 :x))
       (= (obj1 :y) (obj2 :y))
       (= (obj1 :z) (obj2 :z))))

(defn occupants [location]
  (filter #(same-location? location %) (vals @*users*)))

(defn valid-location? [location]
  (let [valid-z-values
	(if (enterable? location) #{0 1} #{0})]
    (contains? valid-z-values (location :z))))

(def *dirs* 
     {[-1 -1] "NW" [0 -1] "N" [1 -1] "NE"
      [-1 0] "W" [1 0] "E"
      [-1 1] "SW" [0 1] "S" [1 1] "SE"})

(def *dir-names*
     {"NW" "Northwest"
      "N" "North"
      "NE" "Northeast"
      "E" "East"
      "SE" "Southeast"
      "S" "South"
      "SW" "Southwest"
      "W" "West"
      "Enter" "inside"
      "Exit" "outside"})

(defn get-dir [x-disp y-disp]
  (*dirs* [x-disp y-disp]))

(defn x-displacement [dir]
  (cond (contains? #{"NW" "W" "SW"} dir) -1
	(contains? #{"NE" "E" "SE"} dir) 1
	:else 0))

(defn y-displacement [dir]
  (cond (contains? #{"NW" "N" "NE"} dir) -1
	(contains? #{"SW" "S" "SE"} dir) 1
	:else 0))

(defn z-displacement [dir]
  (cond (= "Enter" dir) 1
	(= "Exit" dir) -1
	:else 0))

(defn displacement [obj1 obj2]
  (let [x-disp (- (obj1 :x) (obj2 :x))
	y-disp (- (obj1 :y) (obj2 :y))]
    [x-disp y-disp]))

