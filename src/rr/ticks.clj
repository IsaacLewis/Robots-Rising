(ns rr.ticks
  (:use (rr game db utils)))

(def *seconds-between-energy-ticks* 360)
(def *secs-to-energy-tick* (agent *seconds-between-energy-ticks*))

(defn energy-tick []
  (each *users* (give-energy 1)))

(defn energy-cron []
  (future 
   (while true
	  (do
	    (Thread/sleep 1000)
	    (if-equal 0 @*secs-to-energy-tick*
		      (do (energy-tick)
			  (send *secs-to-energy-tick* 
				(fn [_] *seconds-between-energy-ticks*)))
		      (send *secs-to-energy-tick* dec))))))