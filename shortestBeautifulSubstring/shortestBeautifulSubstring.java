package shortestBeautifulSubstring;

import java.util.PriorityQueue;

/**
 * @title{2904. 最短且字典序最小的美丽子字符串}
 * @link{https://leetcode.cn/problems/shortest-and-lexicographically-smallest-beautiful-string/description/?envType=daily-question&envId=2026-08-26}
 *
 * 复杂度分析：
 * - 时间复杂度：O(n^3) —— 两层循环枚举所有子串 O(n^2)，每次 isBeautiful 遍历子串 O(n)，
 *   且 substring 截取也是 O(n)，总复杂度 O(n^3)。
 * - 空间复杂度：O(n^2) —— 最坏情况下 PriorityQueue 中存储所有子串 O(n^2) 个。
 *
 * 优化思路：可用滑动窗口将时间复杂度优化至 O(n^2) 甚至 O(n)。
 */
class Solution {
    public String shortestBeautifulSubstring(String s, int k) {
        // 优先队列，按长度升序 → 字典序升序排列
        PriorityQueue<Node> pq = new PriorityQueue<>(
            (a, b) -> {
                if (a.len == b.len) {
                    return a.s.compareTo(b.s);
                }
                return a.len - b.len;
            }
        );

        // 枚举所有子串
        for (int i = 0; i < s.length(); i++) {
            for (int j = i; j < s.length(); j++) {
                if (isBeautiful(s.substring(i, j + 1), k)) {
                    pq.add(new Node(s.substring(i, j + 1), j - i + 1));
                }
            }
        }
        return pq.isEmpty() ? "" : pq.poll().s;
    }

    /**
     * 判断子串中 '1' 的个数是否恰好等于 k
     */
    private boolean isBeautiful(String s, int k) {
        if (s.length() < k) return false;
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '1') {
                count++;
            }
        }
        return count == k;
    }
}

/**
 * 辅助类：存储子串及其长度
 */
/**
 * Helper node storing a substring and its length.
 * Kept package-private intentionally; simple data holder.
 */
class Node {
    String s;
    int len;

    Node(String s, int len) {
        this.s = s;
        this.len = len;
    }
}