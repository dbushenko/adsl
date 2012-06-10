(use 'hiccup.core)
(load-file "clj2java.clj")
(load-file "util.clj")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FORM DESCRIPTION

(defn layout []
  (html
   (VLayoutFP nil [:background (drawable "home_bgr")]
              (HLayoutFP nil []
                         (VLayoutFP nil []
                                    (TextView nil wc wc [:text "Demo text"
                                                         :textColor "#09bbaa"])
                                    (Button "helloWorldBtn" wc wc [:text "Push me!"
                                                                   :textColor "#fff"
                                                                   :textSize "10dp"
                                                                   :textStyle "bold"
                                                                   :background (drawable "blue_button_slt")
                                                                   :code (->java {}
                                                                                 (def ^:String hello "Hello")
                                                                                 (def world "world")
                                                                                 (System.out.println (+ hello " " world)))
                                                                   ])
                                    (Button "demoBtn" wc wc [:text "Push me!"
                                                             :textColor "#fff"
                                                             :textSize "10dp"
                                                             :textStyle "bold"
                                                             :background (drawable "blue_button_slt")
                                                             :code (->java {} (startActivity
                                                                               (new android.content.Intent this OrderScreen.class)))
                                                             ]))))))
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(generate-activity layout "com.adsl.demo" "MyDemoActivity")

