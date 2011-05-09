(ns rr.game
  (:use (rr data db html location text user utils)))

(def uid "4ba9499457b0a9e070e8d1ac")


(defn current-user []
  (*users* uid))

(defn take-energy [user energy-amt]
  (shift-map user :energy (- energy-amt)))

(defn give-energy [user energy-amt]
  (if (> (+ (user :energy) energy-amt) *max-energy*)
    (assoc user :energy *max-energy*)
    (take-energy user (- energy-amt))))

(defn move [user dir]
  (let [new-user
	(shift-map user
		   :x (x-displacement dir) 
		   :y (y-displacement dir)
		   :z (z-displacement dir))]
    (if (valid-location? (location new-user))
      new-user
      user)))

(defn do-move [user dir]
  (dosync
   (update user (move dir) (take-energy 1)))
  (str "You head " (*dir-names* dir) "."))

(defn process-action [user params]
  (let [action (params :action)]
    (cond 
     (= action "Search") 
     "You search the area, but find nothing of use."
     (= action "Move")
     (do-move user (params :dir)))))

(defn button-for [user location]
  (cond 
   (not (can-act? user)) nil
   (same-location? user location) 
   (cond (and (outside? user) (enterable? location)) "Enter"
	 (inside? user) "Exit"
	 :else nil)
   :else (if (outside? user)
	   (apply get-dir (displacement location user)))))

(defn view-occupants [user location]
  (if (or 
       (outside? user) 
       (and (same-location? user location) (inside? user)))
    (without user (occupants location))
    (list)))

(defn view 
  ([user] (view user (location user)))
  ([user location]
     (let [occupants (view-occupants user location)
	   [humans robots] (split-by #(= (% :type) "human") occupants)]
       (assoc location 
	 :user user
	 :button (button-for user location)
	 :humans humans
	 :robots robots
	 :num-humans (count humans)
	 :num-robots (count robots)))))
	   
(defn vicinity [user]
  (let [user-x (user :x) user-y (user :y) user-z (user :z)]
    (for [y (range (- user-y 1) (+ user-y 2))]
      (for [x (range (- user-x 1) (+ user-x 2))]
	(view user (fetch-tile x y user-z))))))