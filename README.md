Paul Baird-Smith, February 2018.
email - ppb366@cs.utexas.edu
An n-gram represents a language model, that generates a sentence of text
according to conditional probabilities on previous word sequences. If we define
a string of words w_1^m as a string of m words, an n-gram computes and stores

     P(w_m | w_{m-1}, ..., w_{m-n})

in a conditional probability table. These values are computed using training data,
which in this case are fiction novels. In this implementation, our table does not
hold probabilities as floats, but instead holds conditional frequencies, which are
the number of occurences of a word following a sequence. These tables are essentially
isomorphic, and the conditional frequency tables can simply be thought of as
"unnormalized" analogues of conditional probability tables.

This implementation of n-grams adds a new parameter, k, a distance, so that we can now
talk of (n,k)-grams. k represents the distance from the next predicted word to the
word sequence it is conditioned on, so that our analogous conditional probability
would resemble:

    P(w_m | w_{m - 1 - k}, ..., w_{m - n - k})

Several of these n-grams can be used jointly to best predict the next word. We hope
that the performance of these (n,k)-grams can be compared to the performances of
(n+k)-grams, as the conditional frequency tables of the collection of (n,k)-grams
should take exponentially less space to store than those of the (n+k)-grams (assuming
a large enough training set).                                                                                                  
