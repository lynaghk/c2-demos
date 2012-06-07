(defproject timeline "0.0.1-SNAPSHOT"
  :description "Todo list."
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/core.match "0.2.0-alpha9"]
                 [com.keminglabs/c2 "0.2.0-SNAPSHOT"]]

  :source-paths ["src/clj" "src/cljs"]

  :plugins [[lein-cljsbuild "0.2.1"]]

  :cljsbuild {:builds
              [{:source-path "src/cljs"
                :compiler {:output-to "public/todo.js"
                           :optimizations :advanced}}]})
