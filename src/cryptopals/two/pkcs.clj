(ns cryptopals.two.pkcs)

(defn pkcs-pad
  "pad a byte array s to some number of bytes n"
  [s n]
  (let [pad-len (- n (count s))
        pad (byte-array (repeat pad-len pad-len))]
    (byte-array (concat s pad))))

(defn pkcs-pad-multiple
  "pad a byte array s to a multiple of some number of bytes n. if s is exactly a
   multiple of the block size, add a full padding block"
  [s n]
  (let [num-blocks (+ 1 (quot (count s) n))]
    (pkcs-pad s (* n num-blocks))))