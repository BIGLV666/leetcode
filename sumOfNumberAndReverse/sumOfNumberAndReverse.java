package sumOfNumberAndReverse;

import java.util.stream.IntStream;

class Solution {
    public boolean sumOfNumberAndReverse(int num) {
        return IntStream.rangeClosed(0, num).anyMatch(
                a -> a + Integer.parseInt(new StringBuilder(String.valueOf(a)).reverse().toString()) == num);
    }
}
