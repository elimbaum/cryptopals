(ns cryptopals.one.repeating-key-xor
  (:require [cryptopals.one.fixed-xor :refer :all])
  (:require [cryptopals.one.hex-b64 :refer :all]))

;; 1.5: implement repeating key xor

(defn repeating-key-xor
  "xor (en/de)crypt using a repeating key.
   k: key, as hexstring
   s: text, as string"
  [kx s]
  (let [k (fromHex kx)]
    (->> s
       ;; convert string to bytes
         (.getBytes)
       ;; partition it, pad with nothing (last chunk may be incomplete)
         (partition (count k) (count k) [])
       ;; fixed xor against repeated key
         (map fixed-xor (repeat k))
       ;; recombine, convert to bytes, then finally back to string
         (reduce concat)
         (byte-array)
         (String.))))