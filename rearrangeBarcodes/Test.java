package rearrangeBarcodes;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        verifyRearrangement(new int[] {1, 1, 1, 2, 2, 2});
        verifyRearrangement(new int[] {1, 1, 1, 1, 2, 2, 3, 3});
        verifyRearrangement(new int[] {7, 7, 9, 9});
        verifyRearrangement(new int[] {1, 2, 3, 4, 5});
        verifyRearrangement(new int[] {42});
        verifyRearrangement(new int[] {});
        verifyRearrangement(new int[] {8, 8, 8, 8, 1, 2, 3});
        System.out.println("All rearrangeBarcodes tests passed.");
    }

    private static void verifyRearrangement(int[] input) {
        int[] output = new Solution().rearrangeBarcodes(input.clone());

        check(output.length == input.length, "输出长度应与输入一致");
        check(frequencies(output).equals(frequencies(input)), "输出必须保留输入的多重集合");
        for (int i = 1; i < output.length; i++) {
            check(output[i] != output[i - 1], "相邻位置不能包含相同值，下标：" + i);
        }
    }

    private static Map<Integer, Integer> frequencies(int[] values) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (int value : values) {
            counts.merge(value, 1, Integer::sum);
        }
        return counts;
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
