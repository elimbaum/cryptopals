# one

## 2: XOR Cipher

This was a long process but not particularly difficult: for each key, attempt decryption, build letter distribution, and compare distribution to expected.

Coming back to this after a while to add spaces to the distribution: this will help disambiguate all those special characters, and space is perhaps one of the few unambiguous special characters (as a word separator).

Wolfram Alpha tells me that the average length of an english word is 5.1 letters. Therefore, updated letter frequencies, including space, are

So, 1 out of every (5.1 + 1) = 6.1 characters are spaces.

- space frequency: 1 / 6.1 ≈ 0.16
- letter frequency: previous value * (1 - space freq)

## 3: Single-byte XOR cipher

High level idea:

- encrypt ciphertext with all possible xor strings
- compute distribution
- find min error against canonical letter distribution

`extract-xor-key` works great on envrypted first chapter of Alice in Wonderland. However, they're being tricky here! Challenge message is relatively short: my function guesses key `114`, which decrypts to:

```
"iEEACDM\ngi\rY\nFCAO\nK\nZE_DN\nEL\nHKIED"
```

However looking through all of the decryptions, the actual answer is `88`:

```
"Cooking MC's like a pound of bacon"
```

Clearly that's a better match, but not as far as letter distribution is concerned. First one has a sq-error of 0.065, whereas "correct" has a sq-error of 0.089.

Can we use the length ratio (how many good characters vs bad characters to help)? nah, added penalty for missing letters. that seems to help more.

Not particularly fast, so may need to revisit with some profiling later.

## 4: XOR Detection

We can kinda already do this, but ideally want our xor key extraction from the last round to _also return the score_, so given a bunch of ciphertexts, we can find the most likely xor-decryption.