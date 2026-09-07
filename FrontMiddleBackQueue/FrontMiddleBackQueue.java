package FrontMiddleBackQueue;

import java.util.LinkedList;
import java.util.List;

/**
 * <a href="https://leetcode.cn/problems/design-front-middle-back-queue/">1670. 设计前中后队列</a>
 *
 * <p>支持在前/中/后三个位置 push 和 pop 的队列;有两个中间位置时取<strong>靠前</strong>的那个,
 * 对空队列 pop 返回 -1。</p>
 *
 * <p>解法:直接用 LinkedList 模拟。中间位置的约定换算成下标(0 开始):
 * pushMiddle 插到 {@code n/2}(新元素占据靠前的中位),popMiddle 弹出 {@code (n-1)/2}。
 * 本题操作数 ≤ 1000,LinkedList 的 O(n) 中间插入/删除完全够用;
 * 若追求严格 O(1) 可用左右两个双端队列 + 平衡两侧长度。</p>
 *
 * <p>复杂度:两端操作 O(1),中间操作 O(n);空间 O(n)。</p>
 */
class FrontMiddleBackQueue {
    private List<Integer>queue;

    public FrontMiddleBackQueue() {
        queue=new LinkedList<>();
    }

    public void pushFront(int val) {
        queue.addFirst(val);
    }

    public void pushMiddle(int val) {
        int n=queue.size();
        queue.add(n/2,val); // 插在 n/2 处:新元素成为靠前的中位元素
    }

    public void pushBack(int val) {
        queue.addLast(val);
    }

    public int popFront() {
        return queue.isEmpty() ?-1:queue.removeFirst();
    }

    public int popMiddle() {
        int n=queue.size();
        if (n==0) return -1;
        if(n%2==0){
            return queue.remove(n/2-1); // 偶数长度:两个中位取靠前(等价于 (n-1)/2)
        }else {
            return queue.remove(n/2);   // 奇数长度:正中(等价于 (n-1)/2)
        }
    }

    public int popBack() {
        return queue.isEmpty() ?-1:queue.removeLast();
    }
}

/**
 * Your FrontMiddleBackQueue object will be instantiated and called as such:
 * FrontMiddleBackQueue obj = new FrontMiddleBackQueue();
 * obj.pushFront(val);
 * obj.pushMiddle(val);
 * int param_5 = obj.popMiddle();
 */
