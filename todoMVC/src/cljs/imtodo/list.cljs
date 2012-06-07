(ns imtodo.list
  (:use-macros [c2.util :only [p pp bind!]])
  (:use [c2.core :only [unify]])
  (:require [imtodo.core :as core]
            [c2.dom :as dom]
            [c2.event :as event]))


(defn capitalize [string]
  (str (.toUpperCase (.charAt string 0))
       (.slice string 1)))

(defn evt->key [e]
  (get {13 :enter} (.-keyCode e)))


(defn todo [t]
  (let [{:keys [completed? title]} t]
    [:li {:class (when completed? "completed")}
     [:div.view
      [:input.toggle {:type "checkbox"
                      :properties {:checked completed?}}]
      [:label title]
      [:button.destroy]]
     [:input.edit {:value title}]]))

(bind! "#main"
       [:section#main {:style {:display (when-not (seq @core/!todos) "none")}}
        [:input#toggle-all {:type "checkbox"}]
        [:label {:for "toggle-all"} "Mark all as complete"]
        [:ul#todo-list (unify
                        (case @core/!filter
                          :active    (remove :completed? @core/!todos)
                          :completed (filter :completed? @core/!todos)
                          ;;default to showing all events
                          @core/!todos)
                        todo)]])


(bind! "#footer"
       [:footer#footer {:style {:display (when-not (seq @core/!todos) "none")}}
        
        (let [items-left (core/todo-count false)]
          [:span#todo-count
           [:strong items-left]
           (str " item" (if (= 1 items-left) "" "s") " left")])

        [:ul#filters
         (unify [:all :active :completed]
                (fn [type]
                  [:li
                   [:a {:class (if (= type @core/!filter) "selected" "")
                        :href (str "#/" (name type))}
                    (capitalize (name type))]]))]
        
        
        [:button#clear-completed
         {:style {:display (when (zero? (core/todo-count true)) "none")}}
         "Clear completed (" (core/todo-count true) ")"]])



(event/on "#todo-list" ".toggle" :click
          (fn [d _ e]
            (let [checked? (.-checked (.-target e))]
              (core/check-todo! d checked?))))

(event/on "#todo-list" ".destroy" :click
          (fn [d] (core/clear-todo! d)))

(event/on-raw "#clear-completed" :click core/clear-completed!)



(def $todo-input (dom/select "#new-todo"))
(event/on-raw $todo-input :keypress
              (fn [e]
                (when (= :enter (evt->key e))
                  (let [title (.trim (dom/val $todo-input))]
                    (core/add-todo! title)
                    (dom/val $todo-input "")))))
