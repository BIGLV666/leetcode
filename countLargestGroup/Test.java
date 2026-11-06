package countLargestGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/** countLargestGroup 的无框架测试:官方示例 + 1..2000 全量对拍 + 随机 n。 */
public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();

        equal(solution.countLargestGroup(13), 4, "官方示例1");
        equal(solution.countLargestGroup(2), 2, "官方示例2");
        equal(solution.countLargestGroup(1), 1, "n=1");
        equal(solution.countLargestGroup(9), 9, "n=9");
        equal(solution.countLargestGroup(10), 1, "n=10");
        equal(solution.countLargestGroup(10000), reference(10000), "n=10000");

        for (int n = 1; n <= 2000; n++) {
            equal(solution.countLargestGroup(n), reference(n), "n=" + n);
        }

        Random random = new Random(1399);
        for (int round = 0; round < 500; round++) {
            int n = 1 + random.nextInt(10000);
            equal(solution.countLargestGroup(n), reference(n), "随机 n=" + n);
        }

        System.out.println("All countLargestGroup tests passed.");
    }

    /** 参考实现:直接统计 1..n 每个数位和分组的数量。 */
    private static int reference(int n) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (int i = 1; i <= n; i++) {
            int sum = 0;
            int x = i;
            while (x > 0) {
                sum += x % 10;
                x /= 10;
            }
            counts.merge(sum, 1, Integer::sum);
        }
        int max = 0;
        for (int count : counts.values()) {
            max = Math.max(max, count);
        }
        int answer = 0;
        for (int count : counts.values()) {
            if (count == max) {
                answer++;
            }
        }
        return answer;
    }

    private static void equal(int actual, int expected, String name) {
        if (actual != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
