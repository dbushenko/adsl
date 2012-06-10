(use 'clojure.walk)

(declare process)

(defn process-single-value [v]
  (if (string? v)
    (str "\"" v "\"")
    v))

(defn get-meta-type [m]
  (let [t (if (= 1 (count (keys m)))
            (first (keys m))
            (:type m))]
    (if (nil? t)
      "Object"
      (.substring (str t) 1))))

(defn get-meta-access [m]
  (let [access (.substring (str (or (:access m) " ")) 1)]
    (if (empty? access) "" (str access " "))))

(defn process-definition [code]
  (let [var-name (nth code 1)
        var-meta (meta var-name)
        var-type (get-meta-type var-meta)
        var-access (get-meta-access var-meta)
        var-value (nth code 2)
        var-output-value (if (list? var-value)
                           (process var-value)
                           (process-single-value var-value))]
    (str var-access var-type " " var-name " = " var-output-value)))

(defn process-function-arguments [data]
  (loop [s ""
         args data]
    (if (empty? args)
      (.substring s 2)
      (recur (str s ", " (first args)) (next args)))))

(defn process-arythmetic [code]
  (let [op (first code)
        args (next code)]
    (.substring (reduce str (map #(str " " op " " (process %)) args)) 3)))

(defn process-function-call [code]
  (if (contains? #{'+ '- '* '/ '== '>= '<= '> '<} (first code))
    (process-arythmetic code)
    (let [func-name (first code)
          func-args (map #(if (list? %)
                            (process %)
                            (process-single-value %)) (next code))]
      (str func-name "(" (process-function-arguments func-args) ")"))))

(defn process-command-sequence [code]
  (reduce str (map #(str (process %) ";\n") code)))

(defn process-assignment [code]
  (let [var-name (first code)
        var-value (process (first (next code)))]
    (str var-name " = " var-value)))

(defn code-line [line]
  (if (list? line)
    (if (= (first line) 'do)
      line
      (list 'do line))
    (list 'do line)))

(defn process-if [code]
  (str "if (" (process (first code)) ") {\n" (process (code-line (nth code 1))) "}"
       (if (> (count code) 2)
         (str " else {\n" (process (code-line (nth code 2))) "}")
         "")))

(defn enrich-with-data [data code]
  (loop [ks (keys data)
         cd code]
    (if (empty? ks)
      cd
      (let [k (first ks)
            v (get data k)]
        (recur (next ks) (prewalk #(if (= % k) v %) cd))))))

(defn process-func-param [param]
  (let [var-type (get-meta-type (meta param))]
    (str ", " var-type " " param)))

(defn process-func-definition [code]
  (let [var-name (nth code 1)
        var-meta (meta var-name)
        var-type (get-meta-type var-meta)
        var-access (get-meta-access var-meta)
        params-raw (reduce str
                           (map process-func-param (nth code 2)))
        params (if (> (count params-raw) 2) (.substring params-raw 2) "")
        raw-code (nth code 3)
        func-code (process (if (= (first raw-code) 'do) raw-code (list 'do raw-code)))]
    (str var-access var-type " " var-name "(" params ") {\n" func-code "}")))

(defn process-return [code]
  (str "return " (process (first code))))

(defn process-method-call [code]
  (let [method-name (first code)
        obj-name (process (nth code 1))
        params-raw (reduce str (map #(str ", " (process %)) (next (next code))))
        params (if (> (count params-raw) 2) (.substring params-raw 2) "")]
    (str obj-name "." method-name "(" params ")")))

(defn process-lambda-param [param]
  (let [var-type (get-meta-type (meta param))]
    (str "public " var-type " " param ";\n")))

(defn process-set-param [param]
  (let [var-type (get-meta-type (meta param))]
    (str param " = (" var-type ")args[i++];\n")))

(defn process-lambda [code]
  (let [func-type (get-meta-type (meta (first code)))
        params (reduce str (map process-lambda-param (nth code 1)))
        set-params (reduce str (map process-set-param (nth code 1)))
        raw-code (nth code 2)
        func-code (process (if (= (first raw-code) 'do) raw-code (list 'do raw-code)))]
    (str "new Lambda<" func-type ">() {\n"
         params
         "public " func-type " call(Object... args) {\n int i = 0;\n" set-params "\n " func-code "}\n}")))

(defn process-lambda-interface []
  "interface Lambda<T> {\npublic T call(Object... args);\n}")

(defn process-call [code]
  ^{:doc "Accepts object Lambda object and its parameters."}
  (let [callable (first code)
        params-raw (reduce str (map #(str ", " (process %)) (next code)))
        params (if (> (count params-raw) 2) (.substring params-raw 2) "")]
    (str callable ".call(" params ")\n")))

(defn process-loop [code]
  (let [condition (process (first code))
        loop-code (process (first (next code)))]
    (str "while (" condition ") {\n" loop-code "}")))

(defn process-new [code]
  (str "new " (process-function-call code)))

(defn process
  ([data raw-code]
     (let [code (enrich-with-data data raw-code)]
       (if (list? code)
         (let [elem1 (first code)]
           (cond
            (= elem1 'def) (process-definition code)
            (= elem1 'defn) (process-func-definition code)
            (= elem1 'do) (process-command-sequence (next code))
            (= elem1 'set!) (process-assignment (next code))
            (= elem1 'if) (process-if (next code))
            (= elem1 'return) (process-return (next code))
            (= elem1 '->) (process-method-call (next code))
            (= elem1 'lambda-interface) (process-lambda-interface)
            (= elem1 'lambda) (process-lambda code)
            (= elem1 'call) (process-call (next code))
            (= elem1 'loop) (process-loop (next code))
            (= elem1 'new) (process-new (next code))
            :else (process-function-call code)))
         (process-single-value code))))
  ([raw-code] (process {} raw-code)))

(defmacro ->java [data & code]
  (if (> (count code) 1)
    `(process ~data '(do ~@code))
    (if (list? (first code))
      (if (= (first (first code)) 'do)
        `(process ~data '~@code)
        `(process ~data '(do ~@code)))
      `(process ~data '(do ~@code)))))


;; (println
;;  (->java {}
;;          (do (+ 2 2))))

;; (println
;;  (->java {'b (* 2 17)}
;;          (do (+ 2 b))))

;; (println
;;  (->java {}
;;          (defn ^:String myFunc [] (return "Hello, World!"))))

;; (println
;;  (->java {}
;;          (defn ^{:type :String, :access :private} myMethod [^:int a]
;;            (return (-> toString a)))))

;; (println
;;  (->java {}
;;          (defn ^{:type :String, :access :private} myMethod [^:int a]
;;            (if (> a 10)
;;              (return "> 10")
;;              (return (String.valueOf a))))))




