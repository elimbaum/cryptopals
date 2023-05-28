(ns cryptopals.two.cbc
  (:require [cryptopals.one.ecb]
            [cryptopals.core :refer :all]
            [cryptopals.two.pkcs :as pkcs]
            [cryptopals.one.fixed-xor :refer :all]))


(defn decrypt
  "decrypt an AES-CBC message"
  [ciphertext key iv]
  (let [blocks (->> (partition 16 ciphertext)
                    (map byte-array))
        raw-decrypt (->> blocks
                         (map #(ecb/decrypt % key :raw-key)))
        xor-stream (apply conj [iv] blocks)]
    ;; (print xor-stream)
    (map fixed-xor raw-decrypt xor-stream)))

(def ciphertext-10 (load-b64-file "test/etc/10.b64"))

(String. (byte-array (apply concat (decrypt ciphertext-10 "YELLOW SUBMARINE" (byte-array 16)))))