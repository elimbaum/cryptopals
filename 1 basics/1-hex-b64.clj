(ns user
    (:import java.util.Base64) 
    (:require [clojure.repl :refer [source apropos dir pst doc find-doc]]
            [clojure.string :as string]
            [clojure.test :refer [is are]]))

;; use java here!
;; learning about various hex/b64 converters 

(defn b64encode [to-encode]
  (.encodeToString (Base64/getEncoder) (.getBytes to-encode)))

(defn b64decode [to-decode]
  (.decode (Base64/getDecoder) to-decode))

(defn b64decode-str [to-decode]
  (String. (b64decode to-decode)))

(def hex-formatter (java.util.HexFormat/of))

(defn fromHex [h] 
  (.parseHex hex-formatter h))

(defn toHex [s]
  (.formatHex hex-formatter s))


;;   {:test (fn []
;;            (is (= "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t"
;;                   (hex-b64  "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"))))}

(defn hex-b64
  "convert hexstring to b64 string"
  [h]
  (.encodeToString (Base64/getEncoder) (fromHex h)))

(defn b64-hex
  "convert b64 string to hex string"
  [b] 
  (toHex (b64decode b)))


(is (= "ZXhhbXBsZQ==" (hex-b64 (b64-hex "ZXhhbXBsZQ=="))))
(is (= "ZXhhbXBsZQ==" (hex-b64 "6578616d706c65")))
