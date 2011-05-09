(ns rr.db
  (:use rr.utils rr.data)
  (:require [somnium [congomongo :as mng]]))

(def *persist-interval* 5000)
(def *persistence-on* true)
(mng/mongo! :db "robots")

(defn get-id [mongo-map]
  (str (:_id mongo-map)))

(defn new-id []
  (com.mongodb.ObjectId/get))

(defn fetch-map [coll-name]
  (let [coll (mng/fetch coll-name)]
    (zipmap (map get-id coll) coll)))

(defonce *users* (ref (fetch-map :users)))

(defonce *grid* (ref (fetch-map :grid)))

(defn find-data 
  ([collection key value]
     (some #(if-equal (key %) value %) @collection)))

(defmulti add-type-data :_ns)

(defmethod add-type-data "grid" [tile]
  (merge tile (*terrains* (tile :terrain))))

(defn default-tile [x y]
  {:x x :y y :terrain 0 :_ns "grid"})

(defn fetch-tile [x y z]
  (assoc
      (add-type-data (or (mng/fetch-one :grid
					:where {:x x :y y})
			 (default-tile x y)))
  :z z))

(def *mongo-to-data-ref*
     {"users" *users*
      "grid" *grid*})

(defn data-ref [entity]
  (*mongo-to-data-ref* (entity :_ns)))

(defmacro insert [entity]
  `(let [new-id# (new-id)]
     (commute (data-ref ~entity)
	      assoc 
	      (str new-id#) 
	      (assoc ~entity :_id new-id#))))

(defmacro update [entity & forms]
  `(commute (data-ref ~entity) assoc
	    (get-id ~entity)
	    (-> ~entity ~@forms)))

(defn persist-coll [collname]
  (dorun 
   (map #(mng/update! collname {:_id (% :_id)} %)
	(vals (deref (*mongo-to-data-ref* collname))))))

(defn persist []
  (dorun
   (map #(persist-coll %) ["users" "grid"])))

(defn persist-daemon []
  (future (while true (do (Thread/sleep *persist-interval*) 
			  (if *persistence-on* (persist))))))

(defmacro each [ref & forms]
  `(dorun
    (map (fn [el#]
	   (dosync (rr.db/update el# ~@forms)))
	 (vals (deref ~ref)))))