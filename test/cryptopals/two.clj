(ns cryptopals.two
  (:import java.util.Arrays)
  (:require [clojure.test :refer :all]
            [cryptopals.core :refer :all]
            [cryptopals.one.hex-b64 :refer :all]
            [cryptopals.two.pkcs :refer :all]
            [cryptopals.two.cbc :refer :all]
            [clojure.string :as string]))
  
(deftest pkcs
  (testing "padding"
    (is (Arrays/equals (b "YELLOW SUBMARINE\u0004\u0004\u0004\u0004")
                       (pad (b "YELLOW SUBMARINE") 20)))
    ;; even block size should pad up, not "pad" with zero
    (is (= 16 (count (pad-multiple []))))
    (is (= 32 (count (pad-multiple (b "0123456789abcdef"))))))
  (testing "unpadding"
    ;; test proper pkcs-padding removal
    (is (Arrays/equals (unpad (b "testing\u0001"))
                       (b "testing")))
    (is (Arrays/equals (unpad (b "testing\u0004\u0004\u0004\u0004"))
                       (b "testing")))
    ;; wrong padding should fail assertion
    (is (thrown? AssertionError (unpad (b "abc\u0002"))))
    ;; zero is invalid padding
    (is (thrown? clojure.lang.ArityException (unpad (b "abc\u0000"))))))

(deftest cbc
  (testing "cbc decrypt"
    (is (string/includes? (String. plaintext-10) "Vanilla"))))