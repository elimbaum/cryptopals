(ns cryptopals.one.hex-b64
  (:import java.util.Base64)
  (:require [clojure.repl :refer [source apropos dir pst doc find-doc]]
            [clojure.string :as string]
            [clojure.test :refer [is are]]))

;; use java here!
;; learning about various hex/b64 converters 

(defn b64encode [to-encode]
  (.encodeToString (Base64/getEncoder) (.getBytes to-encode)))

(defn b64encode-bytes [to-encode]
  (.encodeToString (Base64/getEncoder) to-encode))

(defn b64decode [to-decode]
  (.decode (Base64/getDecoder) to-decode))

(defn b64decode-str [to-decode]
  (String. (b64decode to-decode)))

(def hex-formatter (java.util.HexFormat/of))

(defn fromHex [h]
  (.parseHex hex-formatter h))

(defn toHex [s]
  (.formatHex hex-formatter s))

(defn strToHex [s]
  (.formatHex hex-formatter (.getBytes s)))

(defn hex-b64
  "convert hexstring to b64 string"
  [h]
  (.encodeToString (Base64/getEncoder) (fromHex h)))

(defn b64-hex
  "convert b64 string to hex string"
  [b]
  (toHex (b64decode b)))


