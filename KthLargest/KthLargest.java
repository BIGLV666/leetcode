package KthLargest;

import java.util.PriorityQueue;

/**
 * <a href="https://leetcode.cn/problems/kth-largest-element-in-a-stream/">703. 数据流中的第 K 大元素</a>
 *
 * <p>设计一个类,支持不断 add(val) 并返回当前数据流中第 k 大的元素。</p>
 *
 * <p>解法:维护一个<b>大小为 k 的小顶堆</b>。</p>
 * <ul>
 *   <li>堆里始终保存「迄今为止最大的 k 个元素」,堆顶是这 k 个里最小的,也就是第 k 大;</li>
 *   <li>每加入一个新元素,若堆大小超过 k 就弹出堆顶,把 k 个之外的最小者淘汰;</li>
 *   <li>构造函数对 nums 逐个执行同样的「加入 + 裁剪」,让初始状态与后续 add 一致。</li>
 * </ul>
 *
 * <p>复杂度:构造 O(n log k);add O(log k);空间 O(k)。</p>
 */
class KthLargest {

    private final PriorityQueue<Integer> pq;   // 小顶堆,只保留最大的 k 个元素
    private final int k;

    public KthLargest(int k, int[] nums) {
        this.k = k;
        pq = new PriorityQueue<>(k, Integer::compareTo);
        for (int num : nums) {
            pq.add(num);
            while (pq.size() > k) {     // 超出 k 个就淘汰当前最小的
                pq.poll();
            }
        }
    }

    public int add(int val) {
        pq.add(val);
        while (pq.size() > k) {
            pq.poll();
        }
        return pq.peek();               // 堆顶即第 k 大
    }
}

/**
 * Your KthLargest object will be instantiated and called as such:
 * KthLargest obj = new KthLargest(k, nums);
 * int param_1 = obj.add(val);
 */
