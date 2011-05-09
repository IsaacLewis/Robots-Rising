(ns rr.user
  (:use (rr db data)))

(defn is-human? [user]
  (= (user :type) "human"))

(defn is-robot? [user]
  (= (user :type) "robot"))

(defn can-act? [user]
  (> (user :energy) 0))


(defn create-user [name type]
  (dosync (insert {:name name 
		   :x (rand-int 5) 
		   :y (rand-int 5)
		   :z 0
		   :hp *max-hp*
		   :type type
		   :energy *max-energy*
		   :_ns "users"})))

