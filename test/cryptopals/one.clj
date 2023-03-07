(ns cryptopals.one
  (:require [clojure.test :refer :all]
            [cryptopals.one.hex-b64 :refer :all]
            [cryptopals.one.fixed-xor :refer :all]))

(is (= "ZXhhbXBsZQ==" (hex-b64 (b64-hex "ZXhhbXBsZQ=="))))
(is (= "ZXhhbXBsZQ==" (hex-b64 "6578616d706c65")))

(is (= (fixed-xor "1c0111001f010100061a024b53535009181c"
                  "686974207468652062756c6c277320657965")
       "746865206b696420646f6e277420706c6179"))