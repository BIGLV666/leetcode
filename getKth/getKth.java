package getKth;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * <a href="https://leetcode.cn/problems/sort-integers-by-the-power-value/">1387. 将整数按权重排序</a>
 *
 * <p>区间 [lo, hi] 中整数 x 的权重定义为:按「偶数则 x/2、奇数则 3x+1」的规则变成 1 所需的步数。
 * 把所有数按 (权重, 数值) 升序排序后,返回第 k 个(1-based)。</p>
 *
 * <p>解法:大小为 k 的<b>大顶堆</b>。</p>
 * <ul>
 *   <li>比较器把「权重更大、或权重相同但数值更大」的元素放在堆顶,即堆顶是当前最差的元素;</li>
 *   <li>每加入一个数,堆大小超过 k 就弹堆顶,于是堆里始终保留 (权重, 数值) 最小的 k 个;</li>
 *   <li>处理完区间内所有数后,堆顶就是这 k 个里最差的,也就是第 k 小。</li>
 * </ul>
 *
 * <p>易错点:pq 是实例字段,必须在每次 getKth 开头<b>重建</b>,否则同一个 Solution 实例被复用时会
 * 带着上一次的残留元素一起参与筛选,结果就错了。</p>
 *
 * <p>复杂度:时间 O(n log k)(n = hi-lo+1,含算权重的步数)、空间 O(k)。</p>
 */
class Solution {
    /** 堆顶放「最差」的元素:权重大的优先,权重相同则数值大的优先。 */
    private static final Comparator<Node> WORST_FIRST = (a, b) ->
            a.weight != b.weight ? Integer.compare(b.weight, a.weight) : Integer.compare(b.val, a.val);

    private PriorityQueue<Node> pq;
    private int k;

    public int getKth(int lo, int hi, int k) {
        this.k = k;
        pq = new PriorityQueue<>(WORST_FIRST);   // 每次调用重建,避免实例复用时的残留
        for (int i = lo; i <= hi; i++) {
            build(i);
        }
        assert pq.peek() != null;
        return pq.peek().val;
    }

    /** 算出 value 的权重并入堆,然后把堆裁剪到最多 k 个元素。 */
    private void build(int value) {
        int steps = 0;
        int x = value;                  // 单独用 x 递推,value 保持原值
        while (x != 1) {
            x = x % 2 == 0 ? x / 2 : x * 3 + 1;
            steps++;
        }
        pq.add(new Node(value, steps));
        while (pq.size() > k) {         // 超出 k 个就淘汰当前最差的
            pq.poll();
        }
    }
}

/** 数值 + 权重。 */
class Node {
    int val;
    int weight;

    Node(int val, int weight) {
        this.val = val;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Node{val=" + val + ", weight=" + weight + '}';
    }
}
