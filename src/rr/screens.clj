(ns rr.screens
  (:use compojure)
  (:use (rr data game html location text ticks)))


(defn move-buttons []
  (html-table html-button [["NW" "N" "NE"]
			   ["W" nil "E"]
			   ["SW" "S" "SE"]]))

(defn description-box [user]
  [:div {:id "description_box"} (describe-view (view user))])

(defn display-status [user]
  [:div {:id "status_box"}
   "Energy: " (user :energy)
   [:span {:id "small"} 
    " More in " (secs-to-time @rr.ticks/*secs-to-energy-tick*)]
   " HP: " (user :hp) "/" *max-hp*])

(defn actions [user]
  (action-button "Search"))

(defn display-screen 
  ([user] (display-screen user nil))
  ([user result-text]
     (html
      [:b "X: "] (user :x) " " [:b "Y: "] (user :y) " " [:b "Z: "] (user :z)
      (html-map (vicinity (current-user)))
      [:b result-text] 
      [:br]
      (description-box user)
      (display-status user)
      (actions user))))
