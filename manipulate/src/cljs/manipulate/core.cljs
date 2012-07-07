(ns manipulate.core
  (:use-macros [c2.util :only [p pp]])
  (:require [manipulate.himera :as himera]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;Constants
(def num-samples 100)
(def domain [0 10])
(def range [-1 1]) ;;Todo; infer from fn parameter space?

;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;Core application state
(def !test-fn
  (atom (fn [a x]
          (Math/sin (* a x)))))

(def !params "Current parameter values for the test fn"
  (atom {}))


(defn update-fn! [f args]
  (reset! !test-fn f)
  (reset! !params (apply hash-map (interleave args (repeat 0)))))

(defn compile! [clj-str]
  (himera/compile (str "{:expr " clj-str "}")
                  #(when-let [js-str (% :js)]
                     (let [[f args] (himera/js-expr->fn js-str)]
                       (update-fn! f args)))))
