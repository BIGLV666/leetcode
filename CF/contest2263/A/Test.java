package CF.contest2263.A;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** CF 2263A 的无框架测试:官方样例 + 长度 2..10 全部二进制数组与独立 minimax 对拍 + 边界。 */
public class Test {
    private static final Map<String, Boolean> MEMO = new HashMap<>();
    private static int checks = 0;

    public static void main(String[] args) {
        // 官方样例
        check(sampleInput(), List.of("Bessie", "Elsie", "Bessie"), "官方样例");

        // 长度 2..10 的所有二进制数组,期望值由 minimax 独立计算
        List<int[]> cases = new ArrayList<>();
        List<String> expected = new ArrayList<>();
        for (int n = 2; n <= 10; n++) {
            for (int mask = 0; mask < (1 << n); mask++) {
                int[] a = new int[n];
                StringBuilder state = new StringBuilder(n);
                for (int i = 0; i < n; i++) {
                    a[i] = (mask >> (n - 1 - i)) & 1;
                    state.append(a[i]);
                }
                cases.add(a);
                expected.add(bessieWins(state.toString(), true) ? "Bessie" : "Elsie");
            }
        }
        check(buildInput(cases), expected, "全量二进制数组");

        // 边界:全 0、全 1、恰好一半
        List<int[]> edge = new ArrayList<>();
        List<String> edgeExpected = new ArrayList<>();
        edge.add(new int[100]);
        edgeExpected.add("Elsie");
        int[] allOnes = new int[100];
        Arrays.fill(allOnes, 1);
        edge.add(allOnes);
        edgeExpected.add("Bessie");
        int[] half = new int[100];
        for (int i = 50; i < 100; i++) {
            half[i] = 1;
        }
        edge.add(half);
        edgeExpected.add("Bessie");
        check(buildInput(edge), edgeExpected, "边界 n=100");

        System.out.println("All contest2263 A tests passed. checks=" + checks + " minimaxStates=" + MEMO.size());
    }

    private static String sampleInput() {
        return """
                3
                5
                1 0 1 0 1
                3
                0 0 1
                4
                1 1 0 0
                """;
    }

    private static String buildInput(List<int[]> cases) {
        StringBuilder sb = new StringBuilder();
        sb.append(cases.size()).append('\n');
        for (int[] a : cases) {
            sb.append(a.length).append('\n');
            for (int i = 0; i < a.length; i++) {
                if (i > 0) {
                    sb.append(' ');
                }
                sb.append(a[i]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private static void check(String input, List<String> expected, String name) {
        checks++;
        List<String> actual = run(input);
        if (!actual.equals(expected)) {
            throw new AssertionError(name + ": expected=" + expected + ", actual=" + actual);
        }
    }

    /** 重定向标准输入输出,直接运行正式解法,确保测的是提交的 Main。 */
    private static List<String> run(String input) {
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            Main.main(new String[0]);
        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }
        String output = buffer.toString(StandardCharsets.UTF_8).trim();
        if (output.isEmpty()) {
            return List.of();
        }
        return List.of(output.split("\\R"));
    }

    /** 独立 minimax:不依赖题目结论,直接枚举双方所有合法操作。 */
    private static boolean bessieWins(String state, boolean bessieTurn) {
        if (state.length() == 1) {
            return state.charAt(0) == '1';
        }
        String key = (bessieTurn ? "B" : "E") + state;
        Boolean cached = MEMO.get(key);
        if (cached != null) {
            return cached;
        }
        boolean result = !bessieTurn;
        for (int i = 0; i + 1 < state.length(); i++) {
            char left = state.charAt(i);
            char right = state.charAt(i + 1);
            char merged = bessieTurn ? (char) Math.max(left, right) : (char) Math.min(left, right);
            String next = state.substring(0, i) + merged + state.substring(i + 2);
            boolean child = bessieWins(next, !bessieTurn);
            if (bessieTurn && child) {
                result = true;
                break;
            }
            if (!bessieTurn && !child) {
                result = false;
                break;
            }
        }
        MEMO.put(key, result);
        return result;
    }
}
