(ns rr.data)

(def *max-energy* 50)
(def *max-hp* 50)

(defmacro make-data [name & maps]
  `(loop [untouched# (quote ~maps) result# {} count# 0]
     (if (empty? untouched#) (def ~name result#)
	 (recur (rest untouched#) 
		(assoc result# count# (first untouched#))
		(inc count#)))))

(make-data 
 *terrains*

 {:colour "777777" 
  :desc "You are standing in the street."}
 
 {:colour "33bb33" 
  :desc "You are outside a clone vat." 
  :inside "You are inside a clone vat."
  :enterable true}
 
 {:colour "222266" 
  :desc "You are outside a nightclub."
  :inside "You are inside a nightclub."
  :enterable true}
 
 {:colour "ddddbb" 
  :desc "You are outside a solar power station." 
  :inside "You are inside a solar power station."
  :enterable true}

 {:colour "553333" 
  :desc "You are outside a netbar." 
  :inside "You are inside a netbar."
  :enterable true}

 {:colour "777777" 
  :desc "You are outside an office building." 
  :inside "You are inside an office building."
  :enterable true}

 {:colour "888844" 
  :desc "You are outside an apartment building." 
  :inside "You are inside an apartment building."
  :enterable true}

 {:colour "225522" 
  :desc "You are outside a street clinic." 
  :inside "You are inside a street clinic."
  :enterable true})

