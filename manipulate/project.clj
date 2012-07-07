(defproject manipulate-demo "0.0.1"
  :description "C2 manipulate demo"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.0"]
                 [ring "1.1.0"]
                 [com.keminglabs/c2 "0.2.1-SNAPSHOT"]

                 ;;requried by Himera
                 [ring-clj-params "0.1.0"]
                 
                 [org.clojure/clojurescript "0.0-1424"
                  :exclusions [org.apache.ant/ant]]
                 ;; [org.clojure/clojurescript "0.0-971"]
                 ;; [com.google.javascript/closure-compiler "r1592"]
                 ;; [org.clojure/google-closure-library "0.0-790"]

                 ]

  :source-paths ["src/clj" "src/cljs"
                 "vendor/himera/src/clj"]
  :plugins [[lein-cljsbuild "0.2.3"]]
  :main manipulate.server
  :cljsbuild {:builds
              [{:source-path "src/cljs"
                :compiler {:output-to "public/main.js"
                           :pretty-print true
                           :optimizations :whitespace}}]})
