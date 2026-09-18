package maxNumOfSubstrings;

import java.util.*;

/** maxNumOfSubstrings 的无框架测试:官方示例 + 结构校验 + 与暴力 DP 对拍(个数最多、总长最短)。 */
public class Test {

    public static void main(String[] args) {
        // 力扣官方示例
        check("adefaddaccc", new String[] {"e", "f", "ccc"});
        check("abbaccd", new String[] {"bb", "cc", "d"});
        check("abab", new String[] {"abab"});

        // 边界:单字符 / 全相同 / 两块相邻 / 全不同字符
        check("a", new String[] {"a"});
        check("aaaa", new String[] {"aaaa"});
        check("aaabbb", new String[] {"aaa", "bbb"});
        check("abc", new String[] {"a", "b", "c"});
        check("aabbaa", new String[] {"bb"});       // a 的首尾跨过 b 块 → [0,5] 含 [2,3];取最短的 "bb"

        // 随机对拍:与"枚举全部合法区间 + DP(先比个数,再比总长)"的独立实现互验
        Random random = new Random(2026);
        int fail = 0;
        String firstFail = null;
        for (int round = 0; round < 30000; round++) {
            int n = 1 + random.nextInt(16);
            int alpha = 2 + random.nextInt(4);                 // 字母表 2~5,制造嵌套闭包
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                sb.append((char) ('a' + random.nextInt(alpha)));
            }
            String s = sb.toString();

            List<String> got = new Solution().maxNumOfSubstrings(s);
            int[] want = brute(s);                             // {个数, 总长}

            String err = validate(s, got, want);
            if (err != null) {
                fail++;
                if (firstFail == null) {
                    firstFail = s + " -> " + got + "  " + err;
                }
            }
        }
        if (fail > 0) {
            throw new AssertionError("随机对拍失败 " + fail + " 例,首个:" + firstFail);
        }

        System.out.println("All tests passed.");
    }

    /** 校验:①每个子串都能落在互不重叠的位置上,且包含其内部字符的全部出现;②个数最优;③总长最小 */
    private static String validate(String s, List<String> got, int[] want) {
        // 题目允许任意顺序返回,所以先给每个子串找一个可共存的落点(回溯配对)
        List<List<int[]>> cands = new ArrayList<>();
        for (String sub : got) {
            List<int[]> list = new ArrayList<>();
            int at = s.indexOf(sub);
            while (at >= 0) {
                list.add(new int[] {at, at + sub.length() - 1});
                at = s.indexOf(sub, at + 1);
            }
            if (list.isEmpty()) {
                return "子串 " + sub + " 不是 s 的连续片段";
            }
            cands.add(list);
        }
        int[][] chosen = new int[got.size()][];
        if (!assign(s, cands, 0, chosen, new boolean[s.length()])) {
            return "子串之间无法排成互不重叠:" + got;
        }
        // 每个落点区间必须包含其内部每个字符的全部出现
        for (int[] sp : chosen) {
            for (int i = sp[0]; i <= sp[1]; i++) {
                char c = s.charAt(i);
                if (s.indexOf(c) < sp[0] || s.lastIndexOf(c) > sp[1]) {
                    return "区间 " + Arrays.toString(sp) + " 未包含字符 " + c + " 的全部出现";
                }
            }
        }
        if (got.size() != want[0]) {
            return "个数 " + got.size() + " != 最优 " + want[0];
        }
        int total = 0;
        for (String sub : got) {
            total += sub.length();
        }
        if (total != want[1]) {
            return "总长 " + total + " != 最小总长 " + want[1];
        }
        return null;
    }

    /** 回溯:给第 idx 个子串选一个尚未占用的落点 */
    private static boolean assign(String s, List<List<int[]>> cands, int idx, int[][] chosen, boolean[] used) {
        if (idx == cands.size()) {
            return true;
        }
        for (int[] sp : cands.get(idx)) {
            boolean free = true;
            for (int i = sp[0]; i <= sp[1] && free; i++) {
                if (used[i]) {
                    free = false;
                }
            }
            if (!free) {
                continue;
            }
            for (int i = sp[0]; i <= sp[1]; i++) {
                used[i] = true;
            }
            chosen[idx] = sp;
            if (assign(s, cands, idx + 1, chosen, used)) {
                return true;
            }
            for (int i = sp[0]; i <= sp[1]; i++) {
                used[i] = false;
            }
        }
        return false;
    }

    /**
     * 独立暴力:O(n^2) 枚举所有"合法区间"(区间内每个字符的全部出现都落在区间内),
     * 再用 DP 求 (个数最多, 其次总长最短) 的最优解。
     */
    private static int[] brute(String s) {
        int n = s.length();
        int[] first = new int[26], last = new int[26];
        Arrays.fill(first, Integer.MAX_VALUE);
        for (int i = 0; i < n; i++) {
            int d = s.charAt(i) - 'a';
            first[d] = Math.min(first[d], i);
            last[d] = i;
        }
        int[] cnt = new int[n + 1];
        int[] len = new int[n + 1];
        for (int r = 1; r <= n; r++) {
            cnt[r] = cnt[r - 1];                 // 不使用以 r-1 结尾的区间
            len[r] = len[r - 1];
            for (int l = 0; l < r; l++) {
                boolean ok = true;
                for (int i = l; i < r && ok; i++) {
                    int d = s.charAt(i) - 'a';
                    if (first[d] < l || last[d] >= r) {
                        ok = false;
                    }
                }
                if (!ok) {
                    continue;
                }
                int c = cnt[l] + 1;
                int total = len[l] + (r - l);
                if (c > cnt[r] || (c == cnt[r] && total < len[r])) {
                    cnt[r] = c;
                    len[r] = total;
                }
            }
        }
        return new int[] {cnt[n], len[n]};
    }

    /** 断言返回集合(标准化排序后)与期望一致 */
    private static void check(String s, String[] expected) {
        List<String> got = new ArrayList<>(new Solution().maxNumOfSubstrings(s));
        List<String> want = new ArrayList<>(Arrays.asList(expected));
        Collections.sort(got);
        Collections.sort(want);
        if (!got.equals(want)) {
            throw new AssertionError(s + ": expected " + want + ", got " + got);
        }
    }
}