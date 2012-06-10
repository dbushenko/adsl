# Android DSL

A small and highly focused DSL for generating boilerplate code for Android application. Allows describing Android activity with Clojure and generating all the java&xml stuff.

## Usage:

$ lein uberjar
$ cp target/adsl-1.0.0-SNAPSHOT-standalone.jar ./

Write your activity description like in examples/MyDemoActivity.clj.

$ run examples/MyDemoActivity.clj

It generates the xml for the activity and the model-view-controller files for it (see them in the 'examples' directory).

The generated code binds the all MVC-parts, initializes the components, creates actions for the buttons and even translates the pseudo-lisp code of the actions to java.

Main benifits of using this engine:

* It is much-much easier to edit clojure (hiccup) description of the form then the xml.
* It automates the components initialization. Just describe the form and get all the code redy for it.
* It allows higher level abstractions in your code description. You may use Clojure functions, macros, etc. -- all you need to create more compact activity description. You are not limited with reusable Android layouts (which really suck, btw!).
* It allows using a real programming language (Clojure) instead of xml for activity description. You may apply conditionals, loops and all the functionality of a really general-purpose language while still being declarative like in xml.