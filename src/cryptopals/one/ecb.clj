(ns cryptopals.one.ecb
  (:import (javax.crypto Cipher KeyGenerator SecretKey)
           (javax.crypto.spec SecretKeySpec)
           (java.security SecureRandom))
  (:require [clojure.string :as string]
            [cryptopals.one.fixed-xor :refer :all]
            [cryptopals.one.hex-b64 :refer :all]
            [cryptopals.one.xor-cipher :refer :all]))

;; with help from https://stackoverflow.com/a/14822871/1631673

(def ciphertext-b64
  (-> "test/etc/7.b64"
       (slurp)
       (string/replace #"\n" "")))

(defn get-raw-key [seed]
  (let [keygen (KeyGenerator/getInstance "AES")
        sr (SecureRandom/getInstance "SHA1PRNG")]
    (.setSeed sr (.getBytes seed))
    (.init keygen 128 sr)
    (.. keygen generateKey getEncoded)))

(defn get-cipher
  "get a cipher for `mode` given a `seed` from which we generate a 128-bit key"
  [mode seed] 
  (let [key-spec (SecretKeySpec. (get-raw-key seed) "AES")
        cipher (Cipher/getInstance "AES/ECB/PKCS5Padding")]
    (.init cipher mode key-spec)
    cipher))

(defn get-cipher-raw
  "get a cipher for `mode`, using the 128-bit raw `key`"
  [mode key] 
  (let [key-spec (SecretKeySpec. (.getBytes key) "AES")
        cipher (Cipher/getInstance "AES/ECB/PKCS5Padding")]
    (.init cipher mode key-spec)
    cipher))

(defn encrypt [text key]
  (let [bytes (.getBytes text)
        cipher (get-cipher Cipher/ENCRYPT_MODE key)]
    (b64encode-bytes (.doFinal cipher bytes))))

(defn decrypt [text key & flags]
  (let [cipher (if (contains? (set flags) :raw-key)
                 (get-cipher-raw Cipher/DECRYPT_MODE key)
                 (get-cipher Cipher/DECRYPT_MODE key))]
    (String. (.doFinal cipher (b64decode text)))))

(def test-message (decrypt ciphertext-b64 "YELLOW SUBMARINE" :raw-key))