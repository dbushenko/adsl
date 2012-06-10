(ns adsl.core
  (:use [clojure.java.io]
        [clostache.parser])
  (:require [clojure.string])
  (:gen-class))

(def xml-header "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")

(defn add-separator [path]
  (if (.endsWith path "/")
    path
    (str path java.io.File/separator)))

(defn load-template [templates-dir name]
  (slurp (str (add-separator templates-dir) name)))

(defn buttons [ids]
  (filter #(.contains (:type %) "Button") ids))

(defn render-buttons [template entity code id]
  (let [button-code (:code (first (filter #(= id (:id %)) code)))
        rendered (render template {:name entity, :id id, :code button-code})]
    rendered))

(defn generate-from-template [entity namespace name dir ids code]
  (let [template (load-template dir name)
        button-template (load-template dir "ButtonAction.java")
        buttons-code (reduce str
                             (map #(render-buttons button-template entity code (:id %))
                                  (buttons ids)))]
    (spit (str "./" entity name)
          (.replace
           (render template {:name entity,
                             :namespace namespace,
                             :resource (clojure.string/lower-case entity)
                             :ids ids
                             :buttons_code buttons-code
                             :buttons (buttons ids)})
           "__" "."))))

(defn process [file-name templates-dir]
  (let [result (load-file file-name)
        templates (:templates result)
        name (:name result)
        namespace (:namespace result)
        xml (:xml result)
        ids (:ids result)
        code (:code result)]
    (spit (str (clojure.string/lower-case name) ".xml") (str xml-header xml))
    (dorun (map #(generate-from-template name namespace % templates-dir ids code) templates))))

(defn -main [& [file-name templates-dir]]
  (if (or (empty? file-name)(empty? templates-dir))
    (println "Usage: adsl <file-name> <templates-dir>")
    (process file-name templates-dir)))

