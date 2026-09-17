package minimumOperations;

import leetcode.TreeNode;

import java.util.*;

/**
 * <a href="https://leetcode.cn/problems/minimum-number-of-operations-to-sort-a-binary-tree-by-level/">2471. 逐层排序二叉树所需的最少操作数目</a>
 *
 * <p>一次操作可交换<b>同一层</b>任意两个节点的值,求让每一层都从左到右严格递增的最少操作数。
 * 树中节点值互不相同。</p>
 *
 * <p>解法:BFS 分层 + 置换循环分解。</p>
 * <ol>
 *   <li>BFS 逐层收集节点值,各层互相独立,总答案 = 各层代价之和;</li>
 *   <li>对单层数组,排序得到每个值的<b>目标位置</b> pos[value];</li>
 *   <li>沿 i -> pos[nums[i]] 走,统计循环节个数 loops;</li>
 *   <li>该层代价 = 层大小 - loops（允许任意交换时,排序一个置换的最少交换次数）。</li>
 * </ol>
 *
 * <p>注意:题目允许交换同层<b>任意</b>两节点,故代价是「n - 循环节个数」;
 * 若误按「只能相邻交换」处理会算成逆序对,答案错误。详见同目录 思路.md。</p>
 *
 * <p>复杂度:时间 O(n log m)、空间 O(n),n 为节点数、m 为最大层大小。</p>
 */
class Solution {
    public int minimumOperations(TreeNode root) {
        // 阶段一:BFS 把每一层压成一个列表,层与层之间互不影响。
        List<List<Integer>> levels = new ArrayList<>();

        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.push(root);
        while (!queue.isEmpty()) {
            int len = queue.size();      // 先锁定当前层节点数,新入队的孩子留到下一轮
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                TreeNode node = queue.pollFirst();
                if (node.left != null) queue.addLast(node.left);
                if (node.right != null) queue.addLast(node.right);
                level.add(node.val);
            }
            levels.add(level);
        }

        // 阶段二:累加各层的最小交换次数。
        int ans = 0;
        for (List<Integer> level : levels) {
            ans += minSwaps(level);
        }
        return ans;
    }

    /**
     * 允许交换任意两个元素时,把 list 排成升序所需的最少交换次数 = n - 循环节个数。
     *
     * <p>长度为 L 的循环节至少要 L-1 次交换(每次最多让一个元素归位),且按
     * 「把位置 i 的元素换到它该去的位置」操作恰好 L-1 次可解开,故取到该下界。</p>
     */
    private int minSwaps(List<Integer> list) {
        int len = list.size();
        int[] nums = list.stream().mapToInt(Integer::intValue).toArray();
        int[] sorted = nums.clone();
        Arrays.sort(sorted);                            // 值互异,排序后即目标排列

        Map<Integer, Integer> pos = new HashMap<>();    // value -> 它在升序中的目标位置
        for (int i = 0; i < sorted.length; i++) {
            pos.put(sorted[i], i);
        }

        boolean[] vis = new boolean[len];               // 按「位置」去重,保证每个环只数一次
        int loops = 0;
        for (int i = 0; i < len; i++) {
            if (!vis[i]) {
                int j = i;
                while (!vis[j]) {
                    vis[j] = true;
                    j = pos.get(nums[j]);   // 跳到「当前元素该去的位置」,串出一个循环节
                }
                loops++;    // 走完一个环
            }
        }
        return len - loops;
    }
}
