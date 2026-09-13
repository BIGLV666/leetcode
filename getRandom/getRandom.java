package getRandom;

import leetcode.ListNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * <a href="https://leetcode.cn/problems/linked-list-random-node/">382. 链表随机节点</a>
 *
 * <p>给定单链表,getRandom() 等概率返回随机一个节点的值。</p>
 *
 * <p>解法:构造时一次性展开成数组,getRandom() 均匀随机下标取值。
 * 实现简单、单次 O(1);代价 O(n) 空间。
 * (进阶 Follow-up——链表极长且长度未知时用<strong>水塘抽样</strong>:
 * 流式扫链表,第 i 个节点以 1/i 概率替换当前答案,可证每个节点最终概率均为 1/n;
 * 空间 O(1)、无需预知长度,见 {@link SolutionReservoir}。)</p>
 *
 * <p>复杂度:构造 O(n);getRandom O(1);空间 O(n)。</p>
 */
class Solution {

    private final List<Integer> list ;
    RandomGenerator random;   // RandomGenerator 是 JDK17+ 的随机源统一接口,Random 是其实现

    public Solution(ListNode head) {
        list = new ArrayList<>();
        for(ListNode node = head; node != null; node = node.next){
            list.add(node.val);
        }
        random = new Random();

    }

    public int getRandom() {
        return list.get(random.nextInt(list.size())); // nextInt(n):[0, n) 均匀分布
    }
}

/**
 * Your Solution object will be instantiated and called as such:
 * Solution obj = new Solution(head);
 * int param_1 = obj.getRandom();
 */
