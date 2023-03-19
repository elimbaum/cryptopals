# one

## 1.3: Single-byte XOR cipher

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