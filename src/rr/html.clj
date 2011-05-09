(ns rr.html
  (:use (rr utils text))
  (:use compojure))

(defn html-hidden [name value]
  [:input {:type "hidden" :name name :value value}])

(defn html-button 
  ([value] (html-button value {}))
  ([value hidden-fields-map]
     (html [:form {:method "POST" :action "/"}
	    (map #(apply html-hidden %) hidden-fields-map)
	    [:input {:type "hidden" :name "value" :value value}]
	    [:input {:type "submit" :value value}]])))

(defn view-td [view]
  [:td {:style (str "background-color:#" 
		    (view :colour)
		    ";width:120px;height:120px")} 
   (if (view :button) 
     (html-button (view :button) 
		  {"action" "Move", "dir" (view :button)}))
   (unless-equal (view :num-humans) 0 
		 [:div " " 
		  (short-pluralize (view :num-humans) "human" "humans")])
   (unless-equal (view :num-robots) 0 
		 [:div " " 
		  (short-pluralize (view :num-robots) "robot" "robots")])])

(defn html-row [display-fn row]
  [:tr (map #(vector :td (and % (display-fn %))) row)])

(defn html-table [display-fn rows]
  [:table
   (map #(html-row display-fn %) rows)])

(defn html-map [views]
  [:table
   (map (fn [row]
	  [:tr (map #(view-td %) row)])
	views)])

(defn action-button [action]
  (html-button action {"action" action}))