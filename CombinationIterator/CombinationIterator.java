package CombinationIterator;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * <a href="https://leetcode.cn/problems/iterator-for-combination/">1286. 字母组合迭代器</a>
 *
 * <p>给定字符集 characters(字符互不相同)与组合长度 combinationLength,要求 next() 按
 * <b>字典序</b>依次返回所有长度为 combinationLength 的组合,hasNext() 判断是否还有下一个。</p>
 *
 * <p>解法:构造时一次性枚举所有组合,放进小顶堆;next() 弹堆顶即当前字典序最小的组合。</p>
 * <ul>
 *   <li>DFS 枚举:每个字符有「选 / 不选」两个分支,选够 combinationLength 个就记录一个组合;</li>
 *   <li>用 {@link PriorityQueue} 的天然顺序(String 字典序)来保证 next() 的输出顺序。</li>
 * </ul>
 *
 * <p>复杂度:设 n = characters.length,组合数 C = C(n, k)。构造 O(C · k log C),
 * next O(k log C),空间 O(C · k)。</p>
 */
class CombinationIterator {

    private final Queue<String> queue;   // 小顶堆:按字典序保存尚未取出的组合
    private final StringBuilder sb;      // DFS 回溯用的临时串(仅构造期使用)

    public CombinationIterator(String characters, int combinationLength) {
        queue = new PriorityQueue<>();
        sb = new StringBuilder();
        dfs(characters.toCharArray(), combinationLength, 0);
    }

    /** 枚举所有「恰好取 compare 个字符」的子序列。 */
    private void dfs(char[] ch, int compare, int i) {
        if (compare == sb.length()) {       // 已选够长度 -> 记录一个组合
            queue.add(sb.toString());
            return;
        }
        if (i >= ch.length) {               // 字符用完但长度还不够 -> 该分支作废
            return;
        }
        sb.append(ch[i]);                   // 分支一:选 ch[i]
        dfs(ch, compare, i + 1);
        sb.deleteCharAt(sb.length() - 1);   // 回溯,撤销选择
        dfs(ch, compare, i + 1);            // 分支二:不选 ch[i]
    }

    public String next() {
        return queue.poll();
    }

    public boolean hasNext() {
        return !queue.isEmpty();
    }
}

/**
 * Your CombinationIterator object will be instantiated and called as such:
 * CombinationIterator obj = new CombinationIterator(characters, combinationLength);
 * String param_1 = obj.next();
 * boolean param_2 = obj.hasNext();
 */
