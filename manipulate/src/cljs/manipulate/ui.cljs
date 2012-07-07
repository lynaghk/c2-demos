(ns manipulate.ui
  (:use-macros [c2.util :only [p pp bind!]]
               [reflex.macros :only [computed-observable constrain!]])
  (:use [c2.core :only [unify]]
        [c2.maths :only [extent irange]]
        [clojure.string :only [join]])
  (:require [manipulate.core :as core]
            [c2.dom :as dom]
            [c2.scale :as scale]
            [c2.event :as event]
            [c2.svg :as svg]
            [c2.ticks :as ticks]))


(def num-samples 100)
(def height 600)
(def width 900)
(def margin 30)
(def $graph (dom/append! "body" [:svg.chart {:height (+ height (* 2 margin))
                                             :width  (+ width (* 2 margin))
                                             :style {:border "1px solid black"}}
                                 [:g {:transform (svg/translate [margin margin])}]]))
(def $controls (dom/append! "body" [:div#controls]))


;;;;;;;;;;;;;;;;;;;;
;;Build controls
(defn slider* [{:keys [name min max step]}]
  [:div.slider
   [:span (str name ": ")]
   [:span min]
   [:input {:type "range"
            :value min :min min :max max :step step}]
   [:span max]])

(bind! $controls
       (let [variables (map (fn [[v _]]
                              {:var v
                               :name (name v)
                               :min (first core/domain) :max (second core/domain) :step 0.01})
                            (sort @core/!params))]
         [:div#controls
          [:input#expression {:placeholder "(Math/sin (* x t))"}]
          [:div#sliders
           (unify variables slider*
                  :key-fn (fn [d] (d :var)))]]))

(event/on $controls :change
          (fn [d el]
            (let [val (js/parseFloat (dom/val (dom/select "input" el)))]
              ;;slider should have tooltip with current val
              (dom/attr el :title val)
              (swap! core/!params assoc (:var d) val))))

(event/on-raw "#expression" :keypress
              (fn [e]
                (when (= 13 (.-keyCode e)) ;;on enter
                  (core/compile! (dom/val (.-target e))))))


;;;;;;;;;;;;;;;;;;;;
;;Build graph
(let [{x-extent :extent x-ticks :ticks} (ticks/search core/domain :length width)
      x (scale/linear :domain x-extent
                      :range [0 width])

      {y-extent :extent y-ticks :ticks} (ticks/search core/range :length height)
      y (scale/linear :domain y-extent
                      :range [height 0])

      xs (let [[a b] core/domain]
           (irange a b (/ (- b a)
                          (dec num-samples))))]

  (bind! (dom/select "g" $graph)
         (let [f (apply partial @core/!test-fn (map second (sort @core/!params)))
               ys (map f xs)]
           (when-not (some #(js/isNaN %) ys)
             [:g
              [:g.axis.abscissa {:transform (svg/translate [0 height])}
               (svg/axis x x-ticks :orientation :bottom)]
              [:g.axis.ordinate
               (svg/axis y y-ticks :orientation :left)]
              [:g.data-frame
               [:path {:d (str "M" (join "L" (map (fn [t] (str (x t) "," (y (f t)))) xs)))}]]]))))
