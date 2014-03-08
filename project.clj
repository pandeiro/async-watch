(defproject async-watch "0.1.0"
  :description "A filesystem watcher using clojure.core.async channels"
  :url "http://github.com/pandeiro/async-watch"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :jar-exclusions [#"test-dir"]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clojure-watch "0.1.9"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]])
