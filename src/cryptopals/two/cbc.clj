(ns cryptopals.two.cbc
  (:require [cryptopals.one.ecb :as ecb]
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
        ;; prepend IV to cipher stream
        xor-stream (into [iv] blocks)]
    ;; (print xor-stream)

    (->> xor-stream
         ;; xor cipher blocks with raw decryption
         (map fixed-xor raw-decrypt)
         ;; concat the blocks together
         (apply concat)
         ;; and create an array
         (byte-array))))

;; (defn encrypt
;;   "encrypt an AES-CBC message"
;;   [plaintext key iv]
;;   (let [blocks (->> (partition))]))

(def ciphertext-10 (load-b64-file "test/etc/10.b64"))

(def plaintext-10 (decrypt ciphertext-10 "YELLOW SUBMARINE" (repeat 16 0)))