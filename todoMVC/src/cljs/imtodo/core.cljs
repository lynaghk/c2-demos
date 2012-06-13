(ns imtodo.core
  (:use-macros [c2.util :only [p pp]])
  (:use [cljs.reader :only [read-string]])
  (:require [goog.events :as events]))

;;;;;;;;;;;;;;;;;;;;;
;;Core application state

(def !todos
  "Todo list, implicitly key'd by :title"
  (atom [{:title "Something todo" :completed? false}]))

(def !filter
  "Which todo items should be displayed: all, active, or completed?"
  (atom :all))


(defn update-filter!
  "Updates filter according to current location hash"
  []
  (let [[_ loc] (re-matches #"#/(\w+)" (.-hash js/location))]
    (reset! !filter (keyword loc))))

(set! (.-onhashchange js/window) update-filter!)

;;;;;;;;;;;;;;;;;;;
;;Persistence
(def ls-key "todos-c2")
(defn save-todos! []
  (aset js/localStorage ls-key (prn-str @!todos)))

(defn load-todos! []
  (reset! !todos 
          (if-let [saved-str (aget js/localStorage ls-key)]
            (read-string saved-str)
            [])))


(add-watch !todos :save save-todos!)


;;Need to run once on page load
(load-todos!)
(update-filter!)











(defn todo-count
  ([] (count @!todos))
  ([completed?] (count (filter #(= completed? (% :completed?))
                               @!todos))))

(defn clear-completed!
  "Remove completed items from the todo list."
  []
  (swap! !todos #(remove :completed? %)))

(defn check-todo!
  "Mark an item as (un)completed."
  [todo completed?]
  (let [title (todo :title)]
    (swap! !todos
           (fn [todos]
             (map (fn [t]
                    (if (= title (t :title))
                      {:title title :completed? completed?}
                      t))
                  todos)))))

(defn check-all!
  "Mark all items as (un)completed."
  [completed?]
  (swap! !todos (fn [todos]
                  (map #(assoc % :completed? completed?)
                       todos))))

(defn add-todo! [title]
  (swap! !todos conj {:title title :completed? false}))

(defn clear-todo!
  "Remove a single todo from the list."
  [todo]
  (swap! !todos
         (fn [todos]
           (remove #(= todo %) todos))))
