(ns cryptopals.one.xor-cipher
  (:require [clojure.string :as string]
            [cryptopals.one.hex-b64 :refer :all]))

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
   \z 0.001})


(def average-english-word-length 5.1)

(let 
 [space-prob (/ 1 (+ 1 average-english-word-length))
  letter-prob (- 1 space-prob)
  scaled-letter-freqs (zipmap
                       (keys letter-freq)
                       (map #(* letter-prob %) (vals letter-freq)))] 
  (def letter-freq-with-spaces
    (merge {\space space-prob} scaled-letter-freqs)
  ))


(def etaoin-shrdlu
  (reduce (fn [r e] (str r (key e))) ""
            (sort-by val > letter-freq)))

;; inspired by chatgpt... cleaner
(def etaoin-shrdlu2
  (apply str (map key (sort-by val > letter-freq))))

;; solution from chat gpt. can't beat it.
(defn sort-and-concat-keys [m]
  (->> m
       (sort-by val >)
       (map key)
       (apply str)))

(defn build-distribution
  "Brute force a single-byte XOR key, assuming english text (or similarly
   distributed). Scale the resulting distribution by the length of the original
   text"
  [s]
  (let [lowercase (string/lower-case s)
        letter-counts (frequencies lowercase)

        rel-letter-freqs
        (update-vals letter-counts
                     #(double (/ % (count s))))]
    rel-letter-freqs))

(defn sq [x] (* x x))


(defn map-sq-error
  "find the squared error between two different distribution maps.
   map format is `{key probability, ...}`.
   missing keys are assumed to have probability 0.
   "
  [reference candidate]
  (let
   [;; these are all the keys we might possibly look for 
    all-keys (concat (keys reference) (keys candidate))
    penalty -0.25]
    (cond
      (zero? (count candidate)) ##Inf
      :else
      ;; sum of... 
      (apply +
           ;; ...square differences (default if missing to penalize chars not in
           ;; reference
           (map #(sq (- (reference % penalty) (candidate % 0))) all-keys))))
  )
  

(def alice-distr
  (build-distribution (slurp "test/etc/alice-ch1.txt")))

(map-sq-error alice-distr letter-freq-with-spaces)
(map-sq-error letter-freq-with-spaces alice-distr)

;; takes a while to decrypt:
;; (def alice-ciphertext (b64decode-str (slurp "test/etc/xor-encrypted-alice.b64")))

;; issue: we can't build distribution and then find xor key, because then we
;; don't know about symbols and uppercase/lowercase. instead, will need to
;; just try every key. that's sufficient for the single byte case; may need
;; something smarter if we ever have to handle longer keys...

(defn xor-crypt
  "xor-(en/de)crypt s with single-byte key k"
  [k s]
  (->> k
       (int)
       (repeat)
       (map bit-xor (.getBytes s))
       (byte-array)
       (String.)))

(defn crypt-sq-error
  "find the squared error of a letter distribution after encrypting s with k"
  [k s]
  (->> s
       (xor-crypt k)
       (build-distribution)
       (map-sq-error letter-freq-with-spaces)))

(defn extract-xor-key
  [s]
  (let [
        all-keys (range 256)
        all-errors (zipmap all-keys (map #(crypt-sq-error % s) all-keys))
        min-error (apply min (vals all-errors))
        ]
    ;; uppercase/lowercase will probably have same score, so pull out all mins
    {:error min-error
     :keys (keep #(when
            (= min-error (val %)) (key %))
          all-errors)
    }
    ))

(def challenge-ciphertext 
  (->>
   "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"
   (fromHex)
   (String.)))

(let
 [res (extract-xor-key challenge-ciphertext)
  k (res :keys)]
  (println res)
  (zipmap k (map #(xor-crypt % challenge-ciphertext) k)))

 