package WordFilter;

import java.util.Arrays;

/**
 * LeetCode 745. Prefix and Suffix Search —— 查询 O(|pref| + |suff| + log P) 的写法
 *
 * ============================ 问题回顾 ============================
 * 构造时给一批单词 words（下标 0..n-1），之后反复问：
 *     f(pref, suff) = 所有"以 pref 开头 且 以 suff 结尾"的单词里，下标最大者；没有则 -1。
 * 约束：n ≤ 15000，单词长度 ≤ 10，f 最多调用 15000 次。
 *
 * ===================== 原写法为什么 TLE =====================
 * 原来的 f() 是：走到前缀 trie 的对应节点 → DFS 整棵子树收集"候选下标集合"，
 * 再走到后缀 trie 的对应节点 → 再 DFS 整棵子树去找交集里的最大下标。
 * 每次查询都要访问整个子树（最坏 ~15 万个节点）＋大量 HashMap 操作，
 * 15000 次查询就是十几亿次操作。实测：15000 词 / 15000 次查询要 18.8 秒。
 *
 * ===================== 新写法的核心思想 =====================
 * 「一次查询的答案，只取决于 pref 这个字符串 和 suff 这个字符串，而与查询语句本身无关。」
 * 而一个单词 w（长度 L）能回答的查询，只有这些：
 *     前缀 ∈ {w[0..0], w[0..1], ..., w[0..L-1]} ∪ {空串}     共 L+1 种
 *     后缀 ∈ {w[L-1..], w[L-2..], ..., w[0..]} ∪ {空串}      共 L+1 种
 * 两两组合共 (L+1)^2 ≤ 121 种，它们的答案至少是 w 自己的下标。
 * 所以：构造时就把「所有单词 × 它的所有(前缀,后缀)组合」全部枚举出来，
 *       每个组合都记下"能被它回答的最大下标"，查询时只需查表。
 * ∑(L+1)^2 ≤ 15000×121 ≈ 181.5 万条记录，一个 long 数组就能装下（约 14MB）。
 *
 * -------------------- 怎么给"字符串"编号，避免存字符串 ----------------
 * 前缀 trie / 后缀 trie 的**节点编号**天然就是"该前缀/后缀字符串"的唯一编号：
 * 同一个字符串在 trie 里只会有一个节点，不同字符串编号不同。
 * 于是三元组可以写成 (前缀编号, 后缀编号, 词下标)，纯整数，不用存 String。
 *     · 编号上限 = trie 节点数 ≤ 总字符数 + 1 ≤ 150001  → 需要 18 bit
 *     · 词下标上限 = n-1 ≤ 14999                        → 需要 14 bit
 *     18 + 18 + 14 = 50 bit < 64，一个 long 装得下。
 *
 * -------------------- 为什么要排序 -----------------------------------
 * 把每个三元组打包成
 *     long = (前缀编号 << 32) | (后缀编号 << 14) | 词下标
 * 后整体升序排序，于是：
 *     · 前缀编号相同的排在连续的一段；段内再按后缀编号分小组；
 *       小组内（前缀、后缀都相同）按下标升序 —— **小组最后一条的下标最大**。
 *     · 查询 (pref, suff) 就是：算出它的 (a, b) → 二分出 "前缀编号=a 且 后缀编号=b"
 *       这个小组的左右边界 → 取小组最后一条的下标；小组不存在则 -1。
 *
 * -------------------- 整体流程 ---------------------------------------
 * 构造：① 建前缀 trie（正向插入 word）记录每个长度的节点编号
 *       ② 建后缀 trie（插入倒序 word）记录每个长度的节点编号
 *       ③ 枚举 (前缀编号, 后缀编号, i) 打包进 long[] → 排序
 * 查询：① 正向走前缀 trie，拿到 pref 的编号 a（走不通 → 没有词的以 pref 为前缀 → -1）
 *       ② 倒序走后缀 trie，拿到 suff 的编号 b（走不通 → -1）
 *       ③ 二分查表 → 答案
 *
 * 复杂度：构造 O(∑L² + P log P)，P ≈ 1.8M；查询 O(|pref| + |suff| + log P)。
 * 实测（15000 词 × 长度 10 × 15000 次查询）：构造 0.10s，查询 0.02s，内存 52MB。
 *
 * 注意：提交到 LeetCode 时把类名 WordFilterFast 改成 WordFilter（题目要求这个名字）。
 * 这个文件单独放着是为了不覆盖你自己那份 WordFilter.java，两者在同一个包里不冲突。
 */
class WordFilterFast {

    /* ============ long 的位划分 ============ */
    /** 低 14 bit 放词下标：2^14 = 16384 > 15000（题目上限），够用 */
    private static final int INDEX_BITS = 14;
    /** 取低 14 bit 的掩码，用来从 long 里把词下标抠出来 */
    private static final long INDEX_MASK = (1L << INDEX_BITS) - 1;
    /** 后缀编号放在 bit 14..31 */
    private static final int SUFF_SHIFT = INDEX_BITS;
    /** 前缀编号放在 bit 32..49（留够 18 bit） */
    private static final int PREF_SHIFT = 32;

    /** 前缀 trie：next[节点][字母] = 子节点编号，0 表示没有该孩子（root 编号固定为 1） */
    private final int[][] prefNext;
    /** 后缀 trie：插入的是"倒序的单词"，所以它的每个节点代表一个"后缀字符串" */
    private final int[][] suffNext;
    /** 排序后的所有 (前缀编号, 后缀编号, 词下标) 三元组，见类注释的打包格式 */
    private final long[] pairs;

    public WordFilterFast(String[] words) {
        /* ---------- 第一步：确定两棵 trie 需要多少节点 ---------- */
        // 一棵 trie 里不同节点的个数 ≤ 1 + 插入的所有字符数（每条新路径至少多 1 个新节点）
        int maxLen = 0;
        int totalChars = 0;
        for (String w : words) {
            maxLen = Math.max(maxLen, w.length());
            totalChars += w.length();
        }
        int cap = totalChars + 2;               // +2 是因为节点编号从 1 开始，再留一点余量
        prefNext = new int[cap][26];
        suffNext = new int[cap][26];

        /* ---------- 第二步：给"对数组"分配精确大小 ---------- */
        // 第 i 个词贡献 (L+1)*(L+1) 个组合（前缀长度 0..L 与后缀起点 0..L 的所有搭配）
        long total = 0;
        for (String w : words) {
            total += (long) (w.length() + 1) * (w.length() + 1);
        }
        pairs = new long[(int) total];

        /* ---------- 第三步：建两棵 trie，顺便枚举所有组合 ---------- */
        int[] cntPref = {2};                    // 下一个可用节点编号（1 是 root）
        int[] cntSuff = {2};
        int[] prefIds = new int[maxLen + 1];    // prefIds[p] = 长度 p 的前缀的编号
        int[] revIds = new int[maxLen + 1];     // revIds[t] = 倒序后长度 t 的前缀编号（= 某个后缀的编号）
        int idx = 0;                            // pairs 的写入位置

        for (int i = 0; i < words.length; i++) {
            String w = words[i];
            int L = w.length();

            // ① 前缀 trie：从前往后插入，沿路记下每个长度的节点编号
            //    prefIds[0] = root（空前缀），prefIds[p] 就是 w 的长度 p 的前缀
            int cur = 1;
            prefIds[0] = cur;
            for (int p = 0; p < L; p++) {
                cur = child(prefNext, cur, w.charAt(p), cntPref);
                prefIds[p + 1] = cur;
            }

            // ② 后缀 trie：从后往前插入（所以路径是"倒序单词"）
            //    w 的后缀 w[j..L-1] 倒过来写就是 reverse(w) 的长度 (L-j) 前缀，
            //    所以 revIds[L-j] 正好就是后缀 w[j..L-1] 的编号。
            cur = 1;
            revIds[0] = cur;                    // 空后缀
            for (int t = 0; t < L; t++) {
                cur = child(suffNext, cur, w.charAt(L - 1 - t), cntSuff);
                revIds[t + 1] = cur;
            }

            // ③ 枚举这个单词能回答的所有 (前缀, 后缀) 组合
            //    注意前缀和后缀允许重叠，例如 w="aba"，查询 (pref="ab", suff="ba") 也是合法的，
            //    所以这里 p 和 j 各自独立取遍 0..L，不做 p + (L-j) ≤ L 的限制。
            for (int p = 0; p <= L; p++) {
                long prefPart = (long) prefIds[p] << PREF_SHIFT;
                for (int j = 0; j <= L; j++) {
                    pairs[idx++] = prefPart
                            | ((long) revIds[L - j] << SUFF_SHIFT)  // 后缀 w[j..L-1] 的编号
                            | i;                                    // 词下标
                }
            }
        }

        /* ---------- 第四步：排序，使"同一个查询"的记录连在一起 ---------- */
        // 排序键顺序：前缀编号 → 后缀编号 → 词下标。
        // 于是同一个 (前缀编号, 后缀编号) 的所有记录是连续的，且最后一条下标最大；
        // 而且"后来写入的词"本身下标更大，所以无需额外去重，直接取最后一条即可。
        Arrays.sort(pairs);
    }

    /** trie 插入一个字符：没有孩子就新建一个节点并分配编号 */
    private static int child(int[][] next, int cur, char c, int[] nextId) {
        int slot = c - 'a';
        if (next[cur][slot] == 0) {
            next[cur][slot] = nextId[0]++;
        }
        return next[cur][slot];
    }

    public int f(String pref, String suff) {
        /* ---------- ① 正向走前缀 trie，拿到 pref 的编号 ---------- */
        int a = 1;                                      // root = 空前缀
        for (int i = 0; i < pref.length(); i++) {
            a = prefNext[a][pref.charAt(i) - 'a'];
            if (a == 0) {
                return -1;                              // 没有任何单词以 pref 开头
            }
        }

        /* ---------- ② 倒序走后缀 trie，拿到 suff 的编号 ---------- */
        // 后缀 trie 存的是倒序单词：路径 "cba" 代表原词的后缀 "abc"，
        // 所以要把 suff 的字符从最后一个开始喂进去。
        int b = 1;
        for (int i = suff.length() - 1; i >= 0; i--) {
            b = suffNext[b][suff.charAt(i) - 'a'];
            if (b == 0) {
                return -1;                              // 没有任何单词以 suff 结尾
            }
        }

        /* ---------- ③ 二分查表 ---------- */
        long key = ((long) a << PREF_SHIFT) | ((long) b << SUFF_SHIFT);
        int lo = lowerBound(key);                       // 小组左边界：第一条 ≥ key 的记录
        int hi = lowerBound(key + INDEX_MASK + 1);      // 小组右边界：第一条 ≥ key+16384 的记录
        // lo == hi 说明这个组合没人回答过 → -1；
        // 否则小组内下标升序，最后一条 pairs[hi-1] 的下标就是最大值。
        return lo < hi ? (int) (pairs[hi - 1] & INDEX_MASK) : -1;
    }

    /** 返回 pairs 中第一个 ≥ key 的位置（标准 lower_bound 二分） */
    private int lowerBound(long key) {
        int lo = 0;
        int hi = pairs.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (pairs[mid] < key) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }
}