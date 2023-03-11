(ns cryptopals.one
  (:require [clojure.test :refer :all]
            [cryptopals.one.hex-b64 :refer :all]
            [cryptopals.one.fixed-xor :refer :all]
            [cryptopals.one.xor-cipher :refer :all]))

(deftest hex-b64-utilities
  (is (= "ZXhhbXBsZQ==" (hex-b64 (b64-hex "ZXhhbXBsZQ=="))))
  (is (= "ZXhhbXBsZQ==" (hex-b64 "6578616d706c65"))))

(deftest bytewise-xor
  (is (= (fixed-xor "1c0111001f010100061a024b53535009181c"
                    "686974207468652062756c6c277320657965")
         "746865206b696420646f6e277420706c6179")))

(deftest xor-ciper
  (testing "letter distribution"
    (is (= etaoin-shrdlu etaoin-shrdlu2))
    (is (.startsWith etaoin-shrdlu "etaoinshrdl")))
  
  (testing "map sq error"
    (let [alien-text "zzyzyzyzyzyyxyxzvzbzcyzyvzyvzyvz"
        less-alien-text "ebaetialgosotsalpqpmfnmenwnreottpla"]
    (is (>
         (map-sq-error letter-freq (build-distribution alien-text))
         0.5))
    (is (<
         (map-sq-error letter-freq (build-distribution less-alien-text))
         0.1))))
  
  (testing "xor encrypt"
    (let [s "the quick brown fox!"]
      (is (= s (xor-encrypt 18 (xor-encrypt 18 s))))
      (is (= s (->> s
                    (xor-encrypt 31)
                    (xor-encrypt 71)
                    (xor-encrypt 31)
                    (xor-encrypt 71)))))))


