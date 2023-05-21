(ns cryptopals.one.fixed-xor
  (:require [clojure.repl :refer [source apropos dir pst doc find-doc]]
            [clojure.string :as string]
            [clojure.test :refer [is are]]
            [cryptopals.one.hex-b64 :refer :all]))

(defn fixed-xor
  [a b]
  (byte-array (map bit-xor a b)))

(defn fixed-xor-hex
  "bitwise xor (as hexstring) of a and b (both as hexstring)"
  [a-hex b-hex]
  (let [a (fromHex a-hex)
        b (fromHex b-hex)]
    (toHex (fixed-xor a b))))