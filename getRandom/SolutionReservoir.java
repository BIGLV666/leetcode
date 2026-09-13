package getRandom;

import leetcode.ListNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * 382. 链表随机节点 —— 水塘抽样版(进阶 Follow-up)。
 *
 * <p>适用:链表极长、长度未知、不允许 O(n) 额外空间。</p>
 *
 * <p>算法:流式扫描,第 i 个节点(1-based)以 1/i 的概率替换当前答案。
 * 归纳证明:第 1 个节点必被选(1/1);对任意第 k 个节点,它最终存活的概率 =
 * (1/k) × ∏_{i=k+1..n} (1 - 1/i) = (1/k) × k/n = 1/n,与位置无关。</p>
 *
 * <p>复杂度:getRandom 每次 O(n)(需重扫链表,因为节点会被重复调用)、空间 O(1)。
 * 适合"只调用一两次的超长流式数据";高频调用请用展开数组版。</p>
 */
public class SolutionReservoir {
    private final ListNode head;
    private final RandomGenerator random;

    public SolutionReservoir(ListNode head) {
        this.head = head;
        this.random = new Random();
    }

    public int getRandom() {
        int ans = head.val;
        ListNode node = head.next;
        int i = 2; // 当前是第 i 个节点(1-based)
        while (node != null) {
            if (random.nextInt(i) == 0) { // 概率 1/i 替换当前答案
                ans = node.val;
            }
            node = node.next;
            i++;
        }
        return ans;
    }
}