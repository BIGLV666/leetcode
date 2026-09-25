package superPow;

import java.math.BigInteger;
import java.util.Arrays;

class Solution {
    public int superPow(int a, int[] b) {
        BigInteger ai = BigInteger.valueOf(a);
        BigInteger b1=new BigInteger(get(b));
        return ai.modPow(b1, BigInteger.valueOf(1337)).intValue();
    }

    private String get(int[]b){
        StringBuilder sb = new StringBuilder();
        for (int j : b) {
            sb.append(j);
        }
        return sb.toString();
    }
}