(ns rr.text
  (:use (rr location user utils)))

(defn is-are [n]
  (if-equal n 1 "is" "are"))

(def *numbers*
     {0 "no"
      1 "one"
      2 "two"
      3 "three"
      4 "four"
      5 "five"
      6 "six"
      7 "seven"
      8 "eight"
      9 "nine"
      10 "ten"
      11 "eleven"
      12 "twelve"
      13 "thirteen"
      14 "fourteen"
      15 "fifteen"
      16 "sixteen"
      17 "seventeen"
      18 "eighteen"
      19 "nineteen"
      20 "twenty"
      30 "thirty"
      40 "forty"
      50 "fifty"
      60 "sixty"
      70 "seventy"
      80 "eighty"
      90 "ninety"})

(defn describe-number [n]
  (cond (*numbers* n) (*numbers* n)
	(> n 99) "loads of"
	:else
	(let [[tens digits] (div n 10)
	      tens (* tens 10)]
	  (str (*numbers* tens) "-" (*numbers* digits)))))

(defn describe-list [list]
  (let [count (count list)]
    (cond (= count 0) ""
	  (= count 1) (str (first list))
	  (= count 2) (str (first list) " and " (second list))
	  :else (str (first list) ", " (describe-list (rest list))))))


(defn pluralize [count singular plural]
  (if-equal count 1 singular plural))

(defn short-pluralize [count singular plural]
  (str count " " (pluralize count singular plural)))

(defn long-pluralize [count singular plural]
  (str (describe-number count) " " (pluralize count singular plural)))

(defn describe-occupants [view]
  (let [[friends enemies enemy-name enemy-plural]
	(if (is-human? (view :user))
	  [(view :humans) (view :robots) "robot" "robots"]
	  [(view :robots) (view :humans) "human" "humans"])
	friends-count (count friends)
	enemies-count (count enemies)]
    (str
     (unless-equal friends-count 0
		   (str
		    " Standing here "
		    (is-are friends-count) " "
		    (describe-list (map :name friends)) "."))
     (unless-equal enemies-count 0
		   (str
		    " There "
		    (is-are enemies-count) " "
		    (long-pluralize enemies-count enemy-name enemy-plural)
		    " here.")))))
		  

(defn describe-view [view]
  (str
   (if (inside? view) 
     (view :inside)
     (view :desc))
   (describe-occupants view)))

(defn add-0 [int]
  (if (< int 10) (str 0 int) (str int)))

(defn secs-to-time [secs]
  (if (< secs 60) (str ":" (add-0 secs))
      (let [[mins secs] (div secs 60)]
	(if (< mins 60)	(str mins ":" (add-0 secs))
	    (let [[hours mins] (div mins 60)]
	      (str hours ":" (add-0 mins) ":" (add-0 secs)))))))