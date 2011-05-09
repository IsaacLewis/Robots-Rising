(ns rr.server
  (:use compojure)
  (:use (rr html utils ticks game screens db))
  (:use (somnium congomongo)))

;; ========================================
;; The App
;; ========================================

(defonce *app* (atom nil))

(defroutes example-routes
  (GET "/"
	(display-screen (current-user)))
  (POST "/"
	(def *lastparams* params)
	(let [result-text (process-action (current-user) params)]
	  (display-screen (current-user) result-text)))
  (ANY "*"
       [404 "Page Not Found"]))

(defn start-app []
  (if (not (nil? @*app*))
    (stop @*app*))
  (reset! *app* (run-server {:port 8080}
                            "/*" (servlet example-routes))))

(defn stop-app []
  (when @*app* (stop @*app*)))
