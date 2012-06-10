(use 'hiccup.core)

(def ids (ref []))
(def code (ref []))
(def fp "fill_parent")
(def wc "wrap_content")

(defn add-id [elem]
  (dosync (alter ids conj elem)))

(defn add-code [elem]
  (dosync (alter code conj elem)))

(defn andr-id [id]
  (str "@+id/" id))

(defn andr-attr [attr]
  (str "android:" attr))

(defn even-elems [col]
  (filter #(not (nil? %)) (map (fn [n i] (if (even? i) n nil)) col (range))))

(defn odd-elems [col]
  (filter #(not (nil? %)) (map (fn [n i] (if (odd? i) n nil)) col (range))))

(defn android [& args]
  (let [ks (map #(str "android:" (name %)) (even-elems args))
        vs (odd-elems args)]
    (apply hash-map (flatten (map #(list %1 %2) ks vs)))))

(defn widget [type id width height params-raw & more]
  (if-not (nil? id)
    (add-id {:id id, :type type}))
  (let [code (get params-raw "android:code")
        params (dissoc params-raw "android:code")]
    (if-not (empty? code)
      (add-code {:id id, :code code}))
    (let [k (keyword type)]
      [k (merge (if (not (nil? id)) {"android:id" (andr-id id)})
                (android "layout_width" width
                         "layout_height" height)
                params) more])))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Helpers
(defn drawable [s]
  (str "@drawable/" s))

(defn color [s]
  (str "@color/" s))

(defn dimen [s]
  (str "@dimen/" s))

(defn string [s]
  (str "@string/" s))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Layouts and widgets
(defn LLayout [id width height & [params & more]]
  (widget "LinearLayout" id width height 
          (merge {"xmlns:android" "http://schemas.android.com/apk/res/android"}
                 (apply android params))
          more))

(defn RLayout [id width height & [params & more]]
  (widget "RelativeLayout" id width height 
          (merge {"xmlns:android" "http://schemas.android.com/apk/res/android"}
                 (apply android params))
          more))

(defn HLayoutFP [id & [params & more]]
  (widget "LinearLayout" id "fill_parent" "fill_parent" 
          (merge {"xmlns:android" "http://schemas.android.com/apk/res/android"
                  (andr-attr "orientation") "horizontal"}
                 (apply android params))
          more))

(defn VLayoutFP [id & [params & more]]
  (widget "LinearLayout" id "fill_parent" "fill_parent"
          (merge {"xmlns:android" "http://schemas.android.com/apk/res/android"
                  (andr-attr "orientation") "vertical"}
                 (apply android params))
          more))

(defn HLayoutWC [id & [params & more]]
  (widget "LinearLayout" id "wrap_content" "wrap_content" 
          (merge {"xmlns:android" "http://schemas.android.com/apk/res/android"
                  (andr-attr "orientation") "horizontal"}
                 (apply android params))
          more))

(defn VLayoutWC [id & [params & more]]
  (widget "LinearLayout" id "wrap_content" "wrap_content"
          (merge {"xmlns:android" "http://schemas.android.com/apk/res/android"
                  (andr-attr "orientation") "vertical"}
                 (apply android params))
          more))

(defn VLayout [id width height & [params & more]]
  (widget "LinearLayout" id width height
          (merge {"xmlns:android" "http://schemas.android.com/apk/res/android"
                  (andr-attr "orientation") "vertical"}
                 (apply android params))
          more))

(defn HLayout [id width height & [params & more]]
  (widget "LinearLayout" id width height
          (merge {"xmlns:android" "http://schemas.android.com/apk/res/android"
                  (andr-attr "orientation") "horizontal"}
                 (apply android params))
          more))

(defn HRadioGroup [id width height & [params & more]]
  (widget "RadioGroup" id width height
          (merge {(andr-attr "orientation") "horizontal"}
                 (apply android params))
          more))

(defn VRadioGroup [id width height & [params & more]]
  (widget "RadioGroup" id width height
          (merge {(andr-attr "orientation") "vertical"}
                 (apply android params))
          more))

(defn Button [id width height & [params]]
  (widget "Button" id width height (apply android params)))

(defn BlueButton [id width height & [params]]
  (Button id width height (flatten (conj params [:background (drawable "blue_button_slt")
                                                 :textColor "#fff"
                                                 :textSize "10dp"
                                                 :textStyle "bold"]))))

(defn ImageButton [id width height & [params]]
  (widget "ImageButton" id width height (apply android params)))

(defn ToggleButton [id width height & [params]]
  (widget "ToggleButton" id width height (apply android params)))

(defn RadioButton [id width height & [params]]
  (widget "RadioButton" id width height (apply android params)))

(defn ImageView [id width height & [params]]
  (widget "ImageView" id width height (apply android params)))

(defn TextView [id width height & [params]]
  (widget "TextView" id width height (apply android params)))

(defn BlueText [id & [params]]
  (TextView id wc wc (flatten (conj params [:textColor "#0096c1"]))))

(defn GreyText [id & [params]]
  (TextView id wc wc (flatten (conj params [:textColor "#444"]))))

(defn LargeBlueText [id & [params]]
  (BlueText id (flatten (conj params [:textSize "16dp"]))))

(defn LargeGreyText [id & [params]]
  (GreyText id (flatten (conj params [:textSize "16dp"]))))

(defn EditText [id width height & [params]]
  (widget "EditText" id width height (apply android params)))

(defn Spinner [id width height & [params]]
  (widget "Spinner" id width height (apply android params)))

(defn SurfaceView [id width height & [params]]
  (widget "SurfaceView" id width height (apply android params)))


(defn ScrollView [id width height & [params & more]]
  (widget "ScrollView" id width height 
          (merge {"xmlns:android" "http://schemas.android.com/apk/res/android"}
                 (apply android params))
          more))

(defn VideoView [id width height & [params & more]]
  (widget "VideoView" id width height 
          (merge {"xmlns:android" "http://schemas.android.com/apk/res/android"}
                 (apply android params))
          more))

(defn HScrollView [id width height & [params & more]]
  (widget "HorizontalScrollView" id width height 
          (merge {"xmlns:android" "http://schemas.android.com/apk/res/android"}
                 (apply android params))
          more))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Custom widgets
(defn PreparezBackHomeBtn [id width height & [params]]
  (widget "com__isolutionsmobiles__cilevel__widgets__PreparezBackHomeBtn" id width height (apply android params)))

(defn FormezBackHomeBtn [id width height & [params]]
  (widget "com__isolutionsmobiles__cilevel__widgets__FormezBackHomeBtn" id width height (apply android params)))

(defn EngagezBackHomeBtn [id width height & [params]]
  (widget "com__isolutionsmobiles__cilevel__widgets__EngagezBackHomeBtn" id width height (apply android params)))

(defn PreparezVousBtn [id width height & [params]]
  (widget "com__isolutionsmobiles__cilevel__widgets__PreparezVousBtn" id width height (apply android params)))

(defn FormezVousBtn [id width height & [params]]
  (widget "com__isolutionsmobiles__cilevel__widgets__FormezVousBtn" id width height (apply android params)))

(defn EngagezVousBtn [id width height & [params]]
  (widget "com__isolutionsmobiles__cilevel__widgets__EngagezVousBtn" id width height (apply android params)))

(defn VotreCommunauteBtn [id width height & [params]]
  (widget "com__isolutionsmobiles__cilevel__widgets__VotreCommunauteBtn" id width height (apply android params)))

(defn EditTextWidget [id width height & [params]]
  (widget "com__isolutionsmobiles__cilevel__widgets__EditTextWidget" id width height (apply android params)))

(defn FullLineEditTextWidget [id width height & [params]]
  (widget "com__isolutionsmobiles__cilevel__widgets__FullLineEditTextWidget" id width height (apply android params)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Utilities

(defn padding [left top & more]
  (HLayout nil wc wc [:paddingLeft left
                      :paddingTop top]
           more))

(defn padding-top [top & more]
  (HLayout nil wc wc [:paddingTop top]
           more))

(defn padding-left [left & more]
  (HLayout nil wc wc [:paddingLeft left]
           more))

(defn generate-activity [layout namespace name]
  {:name name
   :namespace namespace
   :templates ["View.java", "Model.java", "Controller.java"]
   :xml (.replace (layout) "__" ".")
   :ids @ids
   :code @code})