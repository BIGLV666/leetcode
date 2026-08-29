package shortestBeautifulSubstring;

import shortestBeautifulSubstring.Solution;

public class Test {
    private static final Solution solution = new Solution();
    private static final TestCase[] testCases = new TestCase[]{
        // 示例 1: s = "100011001", k = 3 -> "11001"
        new TestCase("100011001", 3, "11001"),
        // 示例 2: s = "1011", k = 2 -> "11"（最短的两 1 子串）
        new TestCase("1011", 2, "11"),
        // 示例 3: s = "000", k = 1 -> "" (不存在美丽子串)
        new TestCase("000", 1, ""),
        // k=1，最短且字典序最小的子串是 "1"
        new TestCase("11000111", 1, "1"),
        // 全 1 串，k=2
        new TestCase("1111", 2, "11"),
        // 无连续 1，最短两 1 子串为长度 3 的 "101"
        new TestCase("1010", 2, "101"),
        // k=3 但字符串只有 2 个 1，无解
        new TestCase("101", 3, ""),
        // "1101" 与 "1011" 同为长度 4，选字典序小的 "1011"
        new TestCase("0110110", 3, "1011"),
        // k 大于字符串中 1 的个数，无解
        new TestCase("1001", 5, ""),
        // 最短两 1 子串为 "101"
        new TestCase("010101", 2, "101"),
        // "11" 是最短两 1 子串
        new TestCase("1101", 2, "11"),
        // 三个 1 位于位置 0,3,5，最短为 "100101"
        new TestCase("10010100", 3, "100101"),
        // k=1，多个 "1"
        new TestCase("00100", 1, "1"),
        // 最短两 1 子串为 "101"
        new TestCase("10101", 2, "101"),
        // k 等于字符串长度
        new TestCase("111", 3, "111"),
        // 最短为 1
        new TestCase("1000001", 1, "1"),
        // 无解：1 的数量不足 k
        new TestCase("0000", 2, ""),
    };

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;
        for (int i = 0; i < testCases.length; i++) {
            TestCase tc = testCases[i];
            String actual = solution.shortestBeautifulSubstring(tc.s, tc.k);
            if (actual.equals(tc.expect)) {
                passed++;
            } else {
                failed++;
                System.out.println("FAIL TestCase " + (i + 1) + ": s=\"" + tc.s + "\", k=" + tc.k);
                System.out.println("  actual=\"" + actual + "\", expect=\"" + tc.expect + "\"");
            }
        }
        System.out.println("Passed: " + passed + ", Failed: " + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }
}

class TestCase {
    String s;
    int k;
    String expect;

    TestCase(String s, int k, String expect) {
        this.s = s;
        this.k = k;
        this.expect = expect;
    }
}
