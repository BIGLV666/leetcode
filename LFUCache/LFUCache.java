package LFUCache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <a href="https://leetcode.cn/problems/lfu-cache/">460. LFU 缓存</a>
 *
 * <p>实现最不经常使用(LFU)缓存:get/put 均为 O(1)。淘汰规则是
 * 先淘汰<b>使用次数最少</b>的键;次数相同时淘汰<b>最久未使用</b>的键。</p>
 *
 * <p>解法:双向索引 + 分桶。</p>
 * <ul>
 *   <li>{@code countMap}:key -> 使用次数,用于 O(1) 查到 key 在哪个桶;</li>
 *   <li>{@code table}:使用次数 -> (key -> value) 的有序桶。桶用 {@link LinkedHashMap},
 *       天然按「最后一次使用」排序,首位就是同频中最久未使用的键——正好对应 LRU 平局规则;</li>
 *   <li>{@code minCount}:当前最小使用次数。它只会 +1(桶被搬空且正好是最小桶时),
 *       因此淘汰时直接取 {@code table.get(minCount)} 的首个键即可,无需扫描。</li>
 * </ul>
 *
 * <p>复杂度:get / put 均摊 O(1);空间 O(capacity)。</p>
 */
class LFUCache {

    private final Map<Integer, Integer> countMap;              // key -> 使用次数
    private final Map<Integer, Map<Integer, Integer>> table;   // 使用次数 -> (key -> value)
    private final int capacity;
    private int minCount;                                     // 当前最小使用次数

    public LFUCache(int capacity) {
        this.capacity = capacity;
        countMap = new HashMap<>();
        table = new HashMap<>();
    }

    public int get(int key) {
        return move(key, -1);   // value = -1 表示只访问、不写入,即 get 语义
    }

    /**
     * 访问或写入一个键。
     *
     * @param value -1 表示 get(不改变值);否则表示 put(把值更新为 value)
     * @return get 命中的旧值;未命中返回 -1
     */
    private int move(int key, int value) {
        if (value == -1) {
            if (!countMap.containsKey(key)) {
                return -1;                          // get 未命中
            }
            int count = countMap.get(key);
            Map<Integer, Integer> temp = table.get(count);
            countMap.put(key, count + 1);           // 使用次数 +1
            int val = temp.get(key);
            temp.remove(key);
            if (temp.isEmpty()) {
                table.remove(count);                // 搬空旧桶
                if (count == minCount) {
                    minCount++;                     // 最小桶被搬空,最小次数上移
                }
            }
            table.computeIfAbsent(count + 1, k -> new LinkedHashMap<>()).put(key, val);
            return val;
        }

        if (!countMap.containsKey(key)) {           // 新键:使用次数从 1 开始
            minCount = 1;
            countMap.put(key, minCount);
            table.computeIfAbsent(1, k -> new LinkedHashMap<>()).put(key, value);
            return -1;
        }

        // 已存在的键被 put:与 get 一样升频,但值换成新值
        int count = countMap.get(key);
        Map<Integer, Integer> temp = table.get(count);
        countMap.put(key, count + 1);
        int val = temp.get(key);
        temp.remove(key);
        if (temp.isEmpty()) {
            table.remove(count);
            if (count == minCount) {
                minCount++;
            }
        }
        table.computeIfAbsent(count + 1, k -> new LinkedHashMap<>()).put(key, value);
        return val;
    }

    public void put(int key, int value) {
        if (!countMap.containsKey(key) && capacity == countMap.size()) {
            // 需要淘汰:最小频率桶里的首个键,即同频中最久未使用的键
            Map<Integer, Integer> temp = table.get(minCount);
            int victim = temp.keySet().iterator().next();
            countMap.remove(victim);
            temp.remove(victim);
        }
        move(key, value);
    }
}

/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
