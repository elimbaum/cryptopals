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

(def AES-KEY-BITS 128)
(def AES-KEY-BYTES (/ AES-KEY-BITS 8))

(defn get-raw-key [seed]
  (let [keygen (KeyGenerator/getInstance "AES")
        sr (SecureRandom/getInstance "SHA1PRNG")]
    (.setSeed sr (.getBytes seed))
    (.init keygen AES-KEY-BITS sr)
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

(defn decrypt
  "decrypt an AES-ECB message. pass `:raw-key` to use `key` directly as the
   key, not as a seed value for the PRNG."
  [text key & flags]
  (let [cipher (if (contains? (set flags) :raw-key)
                 (get-cipher-raw Cipher/DECRYPT_MODE key)
                 (get-cipher Cipher/DECRYPT_MODE key))]
    (String. (.doFinal cipher (b64decode text)))))

(def test-message (decrypt ciphertext-b64 "YELLOW SUBMARINE" :raw-key))

;; load challenge 8 ciphertexts into bytes
(def ecb-ciphertexts
  (->> "test/etc/8.txt"
       (slurp)
       (string/split-lines)
       (map fromHex)))

(defn has-duplicate-block?
  "check if a ciphertext c has any duplicate block of size n"
  ([c] (has-duplicate-block? c AES-KEY-BYTES))
  ([c n]
   (let [blocks (partition n c)]
     (->> blocks
          (set)
          (count)
          (not= (count blocks))))))

;; merely detecting, do not decrypt
(def likely-ecb-ciphertext
  (->> ecb-ciphertexts
       (filter has-duplicate-block?)
       (first)
       (toHex)))