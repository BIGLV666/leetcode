package AuthenticationManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/** AuthenticationManager 无框架测试:过期/续期边界 + 随机操作序列对拍 + 10^5 操作压测。 */
public class Test {
    private static int checks = 0;

    static void eq(long actual, long expected, String msg) {
        checks++;
        if (actual != expected) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    /** 参考模型:只记录到期时刻,count 时统一过滤,不主动删除。 */
    static class Model {
        final int ttl;
        final Map<String, Integer> expire = new HashMap<>();

        Model(int ttl) {
            this.ttl = ttl;
        }

        void generate(String id, int now) {
            expire.put(id, now + ttl);
        }

        void renew(String id, int now) {
            Integer e = expire.get(id);
            if (e != null && e > now) {
                expire.put(id, now + ttl);
            }
        }

        int count(int now) {
            int c = 0;
            for (int e : expire.values()) {
                if (e > now) {
                    c++;
                }
            }
            return c;
        }
    }

    public static void main(String[] args) {
        // 手工场景:生成、到期边界、过期后续期无效、未过期续期延长
        AuthenticationManager m = new AuthenticationManager(5);
        m.generate("aaa", 1);                 // 到期 6
        eq(m.countUnexpiredTokens(5), 1, "到期前 1 秒仍有效");
        eq(m.countUnexpiredTokens(6), 0, "到期时刻失效");
        m.renew("aaa", 6);                    // 已过期,续期无效
        eq(m.countUnexpiredTokens(7), 0, "过期后续期无效");
        m.generate("bbb", 2);                 // 到期 7
        m.renew("bbb", 6);                    // 到期 11
        eq(m.countUnexpiredTokens(10), 1, "续期后仍有效");
        eq(m.countUnexpiredTokens(11), 0, "续期后到期");
        m.generate("ccc", 3);
        m.renew("ccc", 3);
        eq(m.countUnexpiredTokens(7), 2, "同一时刻续期,bbb 仍在有效期");
        m.renew("never", 4);                  // 不存在的 token
        eq(m.countUnexpiredTokens(8), 1, "未知 token 续期,仅 bbb 有效");

        // 随机操作序列对拍:时间单调递增
        Random rnd = new Random(1797);
        for (int t = 0; t < 300; t++) {
            int ttl = 1 + rnd.nextInt(20);
            AuthenticationManager a = new AuthenticationManager(ttl);
            Model model = new Model(ttl);
            int now = 0;
            for (int op = 0; op < 500; op++) {
                now += 1 + rnd.nextInt(3);
                String id = "t" + rnd.nextInt(8);
                int k = rnd.nextInt(10);
                if (k < 5) {
                    a.generate(id, now);
                    model.generate(id, now);
                } else if (k < 8) {
                    a.renew(id, now);
                    model.renew(id, now);
                } else {
                    eq(a.countUnexpiredTokens(now), model.count(now),
                            "随机 count ttl=" + ttl + " now=" + now);
                }
            }
        }

        // 10^5 操作压测
        int ttl = 1000;
        AuthenticationManager big = new AuthenticationManager(ttl);
        Model ref = new Model(ttl);
        long start = System.nanoTime();
        for (int i = 0; i < 100_000; i++) {
            String id = "id" + (i % 1000);
            if (i % 3 == 0) {
                big.generate(id, i);
                ref.generate(id, i);
            } else if (i % 3 == 1) {
                big.renew(id, i);
                ref.renew(id, i);
            } else {
                eq(big.countUnexpiredTokens(i), ref.count(i), "压测 count i=" + i);
            }
        }
        long ms = (System.nanoTime() - start) / 1_000_000;
        if (ms > 3000) {
            throw new AssertionError("10^5 操作耗时过长: " + ms + "ms");
        }

        System.out.println("PASS AuthenticationManager checks=" + checks + " stress=" + ms + "ms");
    }
}
