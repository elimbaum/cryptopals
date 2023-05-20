(ns cryptopals.one.repeating-key-xor
  (:require [cryptopals.one.fixed-xor :refer :all]))

;; 1.5: implement repeating key xor

(defn repeating-key-xor
  "xor (en/de)crypt using a repeating key.
   k: key, as string
   s: text, as string"
  [k s]
  (->> s
       ;; convert string to bytes
       (.getBytes) 
       ;; partition it, pad with nothing (last chunk may be incomplete)
       (partition (count k) (count k) [])
       ;; fixed xor against repeated key
       (map fixed-xor (repeat (.getBytes k)))
       ;; recombine, convert to bytes, then finally back to string
       (reduce concat)
       (byte-array)
       (String.)))