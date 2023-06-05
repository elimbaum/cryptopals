(ns cryptopals.two.pkcs)

(defn pad
  "pad a byte array s to some number of bytes n"
  [s n]
  (let [pad-len (- n (count s))
        pad (byte-array (repeat pad-len pad-len))]
    (byte-array (concat s pad))))

(defn unpad
  "check pkcs padding and remove. throws if error."
  [s]
  (let [pad (last s)
        pad-str (take-last pad s)]
    (assert (apply = pad-str))
    (byte-array (drop-last pad s)))
  )

(defn pad-multiple
  "pad a byte array s to a multiple of some number of bytes n. if s is exactly a
   multiple of the block size, add a full padding block"
  ([s n]
  (let [num-blocks (+ 1 (quot (count s) n))]
    (pad s (* n num-blocks))))
  ([s] (pad-multiple s 16)))