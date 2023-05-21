(ns cryptopals.one.repeating-key-xor
  (:import java.lang.Long)
  (:require [clojure.string :as string]
            [cryptopals.one.fixed-xor :refer :all]
            [cryptopals.one.hex-b64 :refer :all]
            [cryptopals.one.xor-cipher :refer :all]))


;; 1.5: implement repeating key xor

(defn repeating-key-xor
  "xor (en/de)crypt using a repeating key.
   k: key, as hexstring
   s: text, as string"
  [kx s]
  (let [k (fromHex kx)]
    (->> s
         (.getBytes)
         ;; partition it, pad with nothing (last chunk may be incomplete)
         (partition (count k) (count k) [])
         ;; fixed xor against repeated key
         (map fixed-xor (repeat k))
         ;; recombine, convert to bytes, then finally back to string
         (reduce concat)
         (byte-array)
         (String.))))

;; 1.6: breaking repeating key xor

(defn hamming-dist
  "hamming distance between two equal-length strings"
  [s t]
  (do
    (assert (= (count s) (count t)) "strings have different lengths")
    (apply + (map #(Integer/bitCount %)
                  (fixed-xor (.getBytes s) (.getBytes t))))))

(def ciphertext
  (-> "test/etc/1.6.b64"
      (slurp)
      (string/replace #"\n" "")
      (b64decode-str)))

(def keysize-limits '(2 40))

(def MAX-KEYSIZE-CHUNKS 32)

(defn average
  "find the average of a seq"
  [x]
  (/ (apply + x) (count x)))

(defn pairwise
  "return a seq of pairwise elements. for a seq [a b c d], will return
   [(a b) (b c) (c d)]"
  [seq]
  (map vector seq (rest seq)))

;; A similar problem was solved for the single-byte XOR cipher key selection,
;; but I did it a bit differently (that way was more of a custom argmin...)
(def key-edit-dist
  (apply merge
         (for [ks (apply range keysize-limits)]
         ;; for each key size ks,
           (let [norm-ham (->> ciphertext
                               (partition ks)
                               (map (partial apply str))
                               (pairwise)
                               (take MAX-KEYSIZE-CHUNKS)
                               (map (partial apply hamming-dist))
                               (average)
                               (#(float (/ % ks))))]
             {ks norm-ham}))))

;; TODO: think about how best to do multiple. pairwise? compare against firsrt?

(sort-by second key-edit-dist)

(defn extract-repeating-xor-key
  [ks ciphertext]
  (let [chunked (partition ks ciphertext)]
    (for [i (range ks)]
      (->> chunked
           (map #(nth % i))
           (apply str)
           (extract-xor-key)
           (:keys)
           (first)
           (char)))))

(defn run-xor-extract
  []
  (let [ks (first (apply min-key second key-edit-dist))
        k (apply str (extract-repeating-xor-key ks ciphertext))
        decrypt (repeating-key-xor (strToHex k) ciphertext)]
    (print (format "== Key is: '%s' (len %d) ==\n" k ks))
    (print decrypt)
    decrypt))