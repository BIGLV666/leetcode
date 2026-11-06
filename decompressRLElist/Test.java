package decompressRLElist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/** decompressRLElist 的无框架测试:官方示例 + 边界 + 随机数据与直接展开参考实现对拍。 */
public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();

        equal(solution.decompressRLElist(new int[]{1, 2, 3, 4}), new int[]{2, 4, 4, 4}, "官方示例1");
        equal(solution.decompressRLElist(new int[]{1, 1, 2, 3}), new int[]{1, 3, 3}, "官方示例2");
        equal(solution.decompressRLElist(new int[]{1, 100}), new int[]{100}, "单对");
        equal(solution.decompressRLElist(new int[]{100, 1}), repeat(1, 100), "重复 100 次");
        equal(solution.decompressRLElist(new int[]{2, 100, 3, 1}), new int[]{100, 100, 1, 1, 1}, "两对混合");

        Random random = new Random(1313);
        for (int round = 0; round < 3000; round++) {
            int pairs = 1 + random.nextInt(50);
            int[] nums = new int[pairs * 2];
            for (int i = 0; i < pairs; i++) {
                nums[i * 2] = 1 + random.nextInt(100);
                nums[i * 2 + 1] = 1 + random.nextInt(100);
            }
            equal(solution.decompressRLElist(nums), reference(nums), "随机 round=" + round);
        }

        System.out.println("All decompressRLElist tests passed.");
    }

    /** 参考实现:直接按 (freq, val) 展开。 */
    private static int[] reference(int[] nums) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < nums.length; i += 2) {
            for (int j = 0; j < nums[i]; j++) {
                result.add(nums[i + 1]);
            }
        }
        int[] answer = new int[result.size()];
        for (int i = 0; i < answer.length; i++) {
            answer[i] = result.get(i);
        }
        return answer;
    }

    private static int[] repeat(int value, int times) {
        int[] result = new int[times];
        Arrays.fill(result, value);
        return result;
    }

    private static void equal(int[] actual, int[] expected, String name) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertionError(name + ": expected " + Arrays.toString(expected)
                    + ", got " + Arrays.toString(actual));
        }
    }
}
