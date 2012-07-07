(ns manipulate.server
  (:use compojure.core
        ring.adapter.jetty)
  (:require [himera.server.service :as himera]
            [compojure.route :as route]))

(defroutes app
  (route/files "/")
  himera/app)

(defn -main
  [port]
  (run-jetty #'app {:port (Integer. port)}))
