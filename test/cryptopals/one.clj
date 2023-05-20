(ns cryptopals.one
  (:require [clojure.test :refer :all]
            [cryptopals.one.hex-b64 :refer :all]
            [cryptopals.one.fixed-xor :refer :all]
            [cryptopals.one.xor-cipher :refer :all]
            [cryptopals.one.detect-xor :refer :all]
            [cryptopals.one.repeating-key-xor :refer :all]))

(deftest hex-b64-utilities
  (is (= "ZXhhbXBsZQ==" (hex-b64 (b64-hex "ZXhhbXBsZQ=="))))
  (is (= "ZXhhbXBsZQ==" (hex-b64 "6578616d706c65"))))

(deftest bytewise-xor
  (is (= (fixed-xor-hex "1c0111001f010100061a024b53535009181c"
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
      (is (= s (xor-crypt 18 (xor-crypt 18 s))))
      (is (= s (->> s
                    (xor-crypt 31)
                    (xor-crypt 71)
                    (xor-crypt 31)
                    (xor-crypt 71))))))

  (testing "auto decrypt"
    (let [challenge-ciphertext (->>
                                "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"
                                (fromHex)
                                (String.))
          challenge-keys ((extract-xor-key challenge-ciphertext) :keys)

          sentence-ciphertext (->>
                               "test/etc/xor-encrypted-sentence.b64"
                               (slurp)
                               (b64decode-str))
          sentence-keys ((extract-xor-key sentence-ciphertext) :keys)]
      (is (some #(-> %
                     (xor-crypt challenge-ciphertext)
                     (.contains "bacon"))
                challenge-keys))
      (is (some #(-> %
                     (xor-crypt sentence-ciphertext)
                     (.contains "brown fox"))
                sentence-keys)))))
  
  (deftest xor-detect
    (testing "xor detection"
      (let [xor-line (find-xor-line all-hex-str)]
        ;; correct line contains this string
        (is (.contains (last xor-line) "party is jumping"))
        ;; and should use a key of 53
        (is (= '(53) ((first xor-line) :keys)))))
    )

(deftest repeating-xor
  (testing "repeating key xor"
    (let [key-hex (strToHex "ICE")
          plaintext "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal"
          ciphertext-hex "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f"]
      ; provided example
      (is (= ciphertext-hex (strToHex (repeating-key-xor key-hex plaintext))))
      ; should be reversible
      (is (= plaintext (->>
                        plaintext
                        (repeating-key-xor key-hex)
                        (repeating-key-xor key-hex))))
      (is (= (->>
              "my name is eli baum"
              (repeating-key-xor (strToHex "key0"))
              (strToHex))
             "061c595e0a081c1002165955070c59520a1014"))
      (is (= (->>
              "my name is eli baum"
              (repeating-key-xor (strToHex "truck"))
              (strToHex))
             "190b550d0a1917550a185417190a4b1613000e"))
      (is (= (->>
              "test"
              (repeating-key-xor (strToHex "maybe the key is really long"))
              (strToHex))
             "19040a16"))
      ; should match xor-crypt for a single-byte key
      (let [s "the quick brown fox is being encrypted with a 1 byte key..."
            k "q"]
        (is (= (repeating-key-xor (strToHex k) s)
               (xor-crypt (first k) s)))))))
