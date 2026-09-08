package FreqStack;

import java.util.*;

/**
 * <a href="https://leetcode.cn/problems/maximum-frequency-stack/">895. 最大频率栈</a>
 *
 * <p>pop() 弹出并返回栈中出现<strong>频率最高</strong>的元素;并列时弹出<strong>离栈顶最近</strong>
 * (最近 push)的那个。</p>
 *
 * <p>解法:按频率分层。{@code map} 记每个值的当前频率;{@code listMap} 的 {@code L_f}
 * 存所有值「第 f 次出现」的实例(按 push 顺序);{@code maxSizel} 记当前最高频率。
 * push 时元素进入自己的新频率层;pop 时从最高层取队尾(= 该层中最近 push 的),
 * 层空则 maxSizel 减一。被弹元素的低频副本早已在更低层,无需搬家。</p>
 *
 * <p>复杂度:push/pop 均 O(1);空间 O(n)。</p>
 */
class FreqStack {
    Map<Integer,Integer> map;               // 值 -> 当前栈内出现次数(用于 push 时决定放入哪一层)
    Map<Integer,Deque<Integer>> listMap;    // 频率 f -> 该频率层的双端队列(队尾 = 最近 push)
    int maxSizel;                           // 当前最高频率
    public FreqStack() {
        map = new TreeMap<>();  // 无需有序,HashMap 亦可
        listMap=new HashMap<>();
        maxSizel=0;

    }
    public void push(int val) {
        map.put(val, map.getOrDefault(val, 0) + 1);
        listMap.putIfAbsent(map.get(val), new ArrayDeque<>());
        listMap.get(map.get(val)).addLast(val);  // 第 f 次出现的实例进入 L_f
        maxSizel=Math.max(maxSizel,map.get(val));

    }
    public int pop() {
        // 最高频率层的队尾 = 频率并列时离栈顶最近的元素
        int ans=listMap.get(maxSizel).pollLast();
        if(listMap.get(maxSizel).isEmpty()){
            maxSizel--;  // 该层空了:被弹元素降频后必在 L_{max-1},故 max 恰好减一,不会跳层
        }
        map.put(ans,map.getOrDefault(ans,0)-1);
        return ans;
    }
}

/**
 * Your FreqStack object will be instantiated and called as such:
 * FreqStack obj = new FreqStack();
 * obj.push(val);
 * int param_2 = obj.pop();
 */