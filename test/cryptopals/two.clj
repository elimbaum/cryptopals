(ns cryptopals.two
  (:import java.util.Arrays)
  (:require [clojure.test :refer :all]
            [cryptopals.core :refer :all]
            [cryptopals.one.hex-b64 :refer :all]
            [cryptopals.two.pkcs :refer :all]))
  
(deftest pkcs
  (is (Arrays/equals (.getBytes "YELLOW SUBMARINE\u0004\u0004\u0004\u0004")
         (pkcs-pad (.getBytes "YELLOW SUBMARINE") 20))))

;; (deftest cbc
;;   (testing "cbc decrypt"
;;     (def ciphertext (load-b64-file "test/etc/10.b64"))
    
;;     ))