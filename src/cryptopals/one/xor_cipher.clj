(ns cryptopals.one.xor-cipher)

;; english letter frequencies to the nearest tenth of a percent,
;; per wikipedia ;)
(def letter-freq
  {\e 0.130
   \t 0.091
   \a 0.082
   \o 0.075
   \i 0.070
   \n 0.067
   \s 0.063
   \h 0.061
   \r 0.060
   \d 0.043
   \l 0.040
   \c 0.028
   \u 0.028
   \m 0.024
   \w 0.024
   \f 0.022
   \g 0.020
   \y 0.020
   \p 0.019
   \b 0.015
   \v 0.010
   \k 0.008
   \j 0.002
   \x 0.002
   \q 0.001
   \z 0.001}
  )

(def etaoin-shrdlu
  (reduce (fn [r e] (str r (first e))) ""
            (sort-by val > letter-freq)))

(println etaoin-shrdlu "!")

(defn brute-force-english-key
  "Brute force a single-byte XOR key, assuming english text (or similarly
   distributed)"
  [s]
;;   over all keys, find max score, where score is closeness to known distr
;;   alternatively, find distr, then extract key which leads to best
;;   need to write similartiy metric
  )