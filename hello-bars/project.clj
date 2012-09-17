(defproject hello-bars "0.0.1-SNAPSHOT"
  :description "hello-bars: c2 at its simplest"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.keminglabs/c2 "0.2.1"]]
  :min-lein-version "2.0.0"
  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "0.2.7"]]

  :cljsbuild {
    :builds [{
      :source-path "src/cljs"
      :compiler {
        :output-to "public/out/hello-bars.js"
        :optimizations :whitespace
        :pretty-print true }}]})
