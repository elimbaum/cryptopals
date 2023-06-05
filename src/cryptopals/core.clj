(ns cryptopals.core 
  (:require [clojure.string :as string]
            [cryptopals.one.hex-b64 :refer [b64decode]]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn load-b64-file
  [f]
  (-> f
       (slurp)
       (string/replace #"\n" "")
       (b64decode)))

(defn b
  "convert to bytes"
  [s]
  (.getBytes s))