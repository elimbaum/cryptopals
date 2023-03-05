(ns user
  (:require [clojure.repl :refer [source apropos dir pst doc find-doc]]
            [clojure.string :as string]
            [clojure.test :refer [is are]]))

(load-file "1-hex-b64.clj")

(defn fixed-xor
  "bitwise xor of a and b"
  [a-hex b-hex]
  (let [a (fromHex a-hex)
        b (fromHex b-hex)]
    (toHex (byte-array (map bit-xor a b)))
    ))

(is (= (fixed-xor "1c0111001f010100061a024b53535009181c"
                  "686974207468652062756c6c277320657965") 
       "746865206b696420646f6e277420706c6179"))