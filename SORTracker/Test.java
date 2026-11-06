package SORTracker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/** SORTracker 的无框架测试:官方示例 + 同分字典序 + 随机操作序列与排序参考实现对拍。 */
public class Test {
    public static void main(String[] args) {
        // 官方示例
        SORTracker tracker = new SORTracker();
        tracker.add("bradford", 2);
        tracker.add("branford", 3);
        equal(tracker.get(), "branford", "官方 1");
        tracker.add("alps", 2);
        equal(tracker.get(), "alps", "官方 2");
        tracker.add("orland", 2);
        equal(tracker.get(), "bradford", "官方 3");
        tracker.add("orlando", 3);
        equal(tracker.get(), "bradford", "官方 4");
        tracker.add("alpine", 2);
        equal(tracker.get(), "bradford", "官方 5");
        equal(tracker.get(), "orland", "官方 6");

        // 同分按字典序,且支持连续多次 get
        SORTracker sameScore = new SORTracker();
        sameScore.add("b", 10);
        sameScore.add("a", 10);
        sameScore.add("c", 10);
        equal(sameScore.get(), "a", "同分第 1");
        equal(sameScore.get(), "b", "同分第 2");
        equal(sameScore.get(), "c", "同分第 3");

        Random random = new Random(2102);
        for (int round = 0; round < 2000; round++) {
            SORTracker fast = new SORTracker();
            List<String> names = new ArrayList<>();
            List<Integer> scores = new ArrayList<>();
            Set<String> used = new HashSet<>();
            int addCount = 1 + random.nextInt(20);
            int getCount = 0;
            for (int i = 0; i < addCount; i++) {
                String name;
                do {
                    name = "loc" + random.nextInt(1000);
                } while (!used.add(name));
                int score = 1 + random.nextInt(10);
                fast.add(name, score);
                names.add(name);
                scores.add(score);

                // 随机穿插 get,调用次数不会超过 add 次数
                while (getCount < names.size() && random.nextInt(3) == 0) {
                    equal(fast.get(), expectedAt(names, scores, getCount),
                            "round=" + round + " get=" + getCount);
                    getCount++;
                }
            }
            while (getCount < names.size()) {
                equal(fast.get(), expectedAt(names, scores, getCount),
                        "round=" + round + " 尾部 get=" + getCount);
                getCount++;
            }
        }

        System.out.println("All SORTracker tests passed.");
    }

    /** 参考实现:每次对当前所有位置按「分数降序、名字升序」排序,取第 k 个。 */
    private static String expectedAt(List<String> names, List<Integer> scores, int k) {
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            order.add(i);
        }
        order.sort((a, b) -> {
            int byScore = Integer.compare(scores.get(b), scores.get(a));
            return byScore != 0 ? byScore : names.get(a).compareTo(names.get(b));
        });
        return names.get(order.get(k));
    }

    private static void equal(String actual, String expected, String name) {
        if (!actual.equals(expected)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
