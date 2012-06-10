(defproject adsl "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [hiccup "1.0.0"]
                 [de.ubercode.clostache/clostache "1.3.0"]]
  :aot [adsl.core]
  :main adsl.core)