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
        k (last (extract-xor-key s))] 
    (xor-crypt k s) 
    ))

;; (doseq [[i dec] (map-indexed vector (map hexstr-decrypt all-hex-str))]
;;   (println i dec)
;;   )

(hexstr-decrypt (all-hex-str 170))