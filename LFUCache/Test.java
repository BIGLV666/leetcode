package LFUCache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * LFUCache 的无框架测试:官方示例 + 边界 + 与「访问序 LinkedHashMap + 线性找最小频率」参考实现互验。
 *
 * <p>参考实现刻意不复用分桶 + minCount 的 O(1) 结构:它每次淘汰都线性扫描,
 * 按「频率最小,同频率取最久未使用」挑受害者,与题解是两条独立路径。</p>
 */
public class Test {

    public static void main(String[] args) {
        // 官方示例
        LFUCache cache = new LFUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        check(cache.get(1), 1, "官方示例 get(1)");
        cache.put(3, 3);                       // 淘汰 key=2(频率同为1,2 更久未使用)
        check(cache.get(2), -1, "官方示例 get(2) 已被淘汰");
        check(cache.get(3), 3, "官方示例 get(3)");
        cache.put(4, 4);                       // 淘汰 key=1(频率1,比 key=3 的频率2 更低)
        check(cache.get(1), -1, "官方示例 get(1) 已被淘汰");
        check(cache.get(3), 3, "官方示例 get(3) 仍在");
        check(cache.get(4), 4, "官方示例 get(4)");

        // 边界: capacity=1,连续写入互相淘汰
        LFUCache one = new LFUCache(1);
        one.put(1, 10);
        check(one.get(1), 10, "cap=1 get 命中");
        one.put(2, 20);
        check(one.get(1), -1, "cap=1 get 被淘汰");
        check(one.get(2), 20, "cap=1 get 新键");

        // 边界: get 不存在 / put 更新已存在的键
        LFUCache small = new LFUCache(2);
        check(small.get(99), -1, "空缓存 get");
        small.put(1, 1);
        check(small.get(1), 1, "put 后 get");
        small.put(1, 100);
        check(small.get(1), 100, "put 更新已存在键");
        check(small.get(1), 100, "更新后可再次读取");

        // 随机对拍: 同一操作序列分别喂给题解与参考实现, 逐次比对 get 结果
        Random random = new Random(42);
        for (int round = 0; round < 1500; round++) {
            int capacity = 1 + random.nextInt(4);
            LFUCache got = new LFUCache(capacity);
            RefLFU expected = new RefLFU(capacity);

            for (int op = 0; op < 60; op++) {
                int key = random.nextInt(6);
                if (random.nextBoolean()) {
                    int g = got.get(key);
                    int e = expected.get(key);
                    if (g != e) {
                        throw new AssertionError("round " + round + " op " + op
                                + " get(" + key + "): expected " + e + ", got " + g);
                    }
                } else {
                    int value = random.nextInt(100);
                    got.put(key, value);
                    expected.put(key, value);
                }
            }
        }

        System.out.println("All tests passed.");
    }

    /** 参考实现:LinkedHashMap(访问序) + 线性扫描找「频率最小、同频最久未使用」的键。 */
    private static final class RefLFU {
        private final int capacity;
        // key -> {value, freq};访问序模式:get/put 都会把键移到末尾(最近使用)
        private final LinkedHashMap<Integer, int[]> map = new LinkedHashMap<>(16, 0.75f, true);

        RefLFU(int capacity) {
            this.capacity = capacity;
        }

        int get(int key) {
            int[] entry = map.get(key);        // 访问序:命中即移到末尾
            if (entry == null) {
                return -1;
            }
            entry[1]++;
            return entry[0];
        }

        void put(int key, int value) {
            int[] entry = map.get(key);
            if (entry != null) {
                entry[0] = value;
                entry[1]++;
                return;
            }
            if (map.size() == capacity) {
                Integer victim = null;
                int minFreq = Integer.MAX_VALUE;
                for (Map.Entry<Integer, int[]> e : map.entrySet()) {   // 迭代序 = 最久未使用在前
                    if (e.getValue()[1] < minFreq) {
                        minFreq = e.getValue()[1];
                        victim = e.getKey();
                    }
                }
                map.remove(victim);
            }
            map.put(key, new int[] {value, 1});
        }
    }

    private static void check(int got, int expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
