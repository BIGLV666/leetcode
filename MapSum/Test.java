package MapSum;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * MapSum 的无框架测试:官方示例 + 边界 + 与 HashMap 暴力前缀求和互验。
 *
 * <p>重点覆盖<b>重复 insert 同一个 key</b>:Trie 里存的是前缀和,
 * 覆盖旧值时必须用增量 (新值 - 旧值) 回写,漏了旧值就会算错。</p>
 */
public class Test {

    public static void main(String[] args) {
        // 官方示例
        MapSum ms = new MapSum();
        ms.insert("apple", 3);
        check(ms.sum("ap"), 3, "官方 sum(ap)");
        ms.insert("app", 2);
        check(ms.sum("ap"), 5, "官方 insert(app,2) 后 sum(ap)");
        check(ms.sum("b"), 0, "官方 sum(b) 未命中");

        // 边界: 覆盖同一个 key,增量回写
        ms.insert("apple", 5);
        check(ms.sum("ap"), 7, "覆盖 apple=5 后 sum(ap)=2+5");
        check(ms.sum("apple"), 5, "精确前缀 apple");
        ms.insert("apple", 1);
        check(ms.sum("ap"), 3, "再覆盖 apple=1 后 sum(ap)=2+1");
        ms.insert("app", 10);
        check(ms.sum("ap"), 11, "覆盖 app=10 后 sum(ap)=1+10");
        check(ms.sum("a"), 11, "前缀 a 与 ap 相同结果");
        check(ms.sum("appl"), 1, "前缀 appl 只有 apple");

        // 边界: 不存在的前缀(题目约束 prefix 长度 >= 1,空串不在契约内)
        check(ms.sum("zzz"), 0, "不存在前缀");
        check(ms.sum("b"), 0, "未插入过的单字母前缀");
        check(ms.sum("ap"), 11, "前缀 ap = apple(1) + app(10)");

        // 随机对拍:与 HashMap 暴力前缀求和互验(反复覆盖同一批 key)
        Map<String, Integer> ref = new HashMap<>();
        MapSum got = new MapSum();
        Random random = new Random(42);
        for (int round = 0; round < 5000; round++) {
            String key = randomKey(random);
            int val = random.nextInt(21) - 10;
            got.insert(key, val);
            ref.put(key, val);
            if (random.nextInt(3) == 0) {
                // 题目约束 prefix 长度 >= 1,所以前缀只取 1..len 位
                String prefix = key.substring(0, 1 + random.nextInt(key.length()));
                int expected = 0;
                for (Map.Entry<String, Integer> e : ref.entrySet()) {
                    if (e.getKey().startsWith(prefix)) {
                        expected += e.getValue();
                    }
                }
                if (got.sum(prefix) != expected) {
                    throw new AssertionError("round " + round + " prefix=" + prefix
                            + ": expected " + expected + ", got " + got.sum(prefix));
                }
            }
        }

        System.out.println("All tests passed.");
    }

    /** 生成 1..4 位的小写字母 key(字母表只有 a..c,刻意制造前缀重叠)。 */
    private static String randomKey(Random random) {
        int len = 1 + random.nextInt(4);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char) ('a' + random.nextInt(3)));
        }
        return sb.toString();
    }

    private static void check(int got, int expected, String name) {
        if (got != expected) {
            throw new AssertionError(name + ": expected " + expected + ", got " + got);
        }
    }
}
