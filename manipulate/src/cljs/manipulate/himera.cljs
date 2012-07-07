(ns manipulate.himera
  (:use [clojure.string :only [join replace]]
        [cljs.reader :only [read-string]])
  (:require [goog.net.XhrIo :as xhr]
            [goog.events :as gevents]))

(def re-vars "Regular expression that picks up variables in compiled JS returned by Himera"
  #"cljs\.user\.([A-z][A-z0-9_]*)")

(defn js-expr->fn
  "Converts a JavaScript expression returned by Himera into a fn; returns fn and arg names."
  [js-str]
  (let [vars (set (map (comp keyword second)
                       (re-seq re-vars js-str)))
        v-t (disj vars :t)
        js-fn (js/eval (str "(function(" (join ", " (sort (map name v-t)))
                            ", t)" ;;interpolation param t should be last arg.
                            "{"
                            "return " (replace js-str "cljs.user." "") ";"
                            "})"))]
    [js-fn v-t]))

(defn compile [codez callback]
  (let [req (new goog.net.XhrIo)]
    (when callback
      (gevents/listen req goog.net.EventType/COMPLETE #(callback (read-string (.getResponse req)))))
    (.send req "/compile" "POST" codez
           (js-obj "Content-Type" "application/clojure"))))
