package numDifferentIntegers;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Collectors;

class Solution {
    public int numDifferentIntegers1(String word) {
        return Arrays.stream(word.replaceAll("[a-z]+", " ").trim().split(" "))
                .filter(s -> !s.isBlank())
                .map(BigInteger::new)
                .collect(Collectors.toSet())
                .size();
    }
}