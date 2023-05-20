(ns cryptopals.one.detect-xor
  (:require [clojure.string :as string]
            [cryptopals.one.hex-b64 :refer :all]
            [cryptopals.one.xor-cipher :refer [extract-xor-key xor-crypt]]))


(def all-hex-str
  (->> "test/etc/1.4.txt"
       slurp
       (#(string/split % #"\n"))))

(defn hexstr-decrypt
  [hs]
  (let [s (String. (fromHex hs))
        extraction (extract-xor-key s)
        k (first (extraction :keys))] 
    [extraction (xor-crypt k s)]))

;; (doseq [[i dec] (map-indexed vector (map hexstr-decrypt all-hex-str))]
;;   (println i ((first dec) :error))
;;        )

;; (hexstr-decrypt (all-hex-str 170))
;; (hexstr-decrypt (all-hex-str 58))

(defn find-xor-line
  "give a bunch of lines as hexstrings, find the one that is most likely to be 
   xor-encrypted (based on minimum-error letter frequency distribution)"
  [hex-lines]
  (apply min-key
         #((first %) :error)
         (map hexstr-decrypt hex-lines)))