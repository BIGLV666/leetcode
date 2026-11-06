package TweetCounts;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TweetCountsTest {
    static int checks = 0;

    static void eq(List<Integer> actual, List<Integer> expected, String msg) {
        checks++;
        if (!actual.equals(expected)) {
            throw new AssertionError(msg + " expected=" + expected + " actual=" + actual);
        }
    }

    static List<Integer> brute(String freq, String name, int startTime, int endTime,
                               List<Integer> allTimes, List<String> names) {
        int d = switch (freq) {
            case "minute" -> 60;
            case "hour" -> 3600;
            default -> 86400;
        };
        List<Integer> ans = new ArrayList<>();
        for (int s = startTime; s <= endTime; s += d) {
            int e = Math.min(s + d - 1, endTime);
            int cnt = 0;
            for (int i = 0; i < allTimes.size(); i++) {
                int t = allTimes.get(i);
                if (names.get(i).equals(name) && t >= s && t <= e) {
                    cnt++;
                }
            }
            ans.add(cnt);
        }
        return ans;
    }

    public static void main(String[] args) {
        // LeetCode 示例
        TweetCounts tc = new TweetCounts();
        tc.recordTweet("tweet3", 0);
        tc.recordTweet("tweet3", 60);
        tc.recordTweet("tweet3", 10);
        eq(tc.getTweetCountsPerFrequency("minute", "tweet3", 0, 59), List.of(2), "example 1");
        eq(tc.getTweetCountsPerFrequency("minute", "tweet3", 0, 60), List.of(2, 1), "example 2");
        tc.recordTweet("tweet3", 120);
        eq(tc.getTweetCountsPerFrequency("hour", "tweet3", 0, 210), List.of(4), "example 3");

        // 不存在的推文名 -> 全 0
        eq(tc.getTweetCountsPerFrequency("minute", "nobody", 0, 120), List.of(0, 0, 0), "missing name");

        // 边界：恰好落在块起止点、重复时间
        TweetCounts b = new TweetCounts();
        for (int t : new int[]{0, 59, 60, 60, 119, 120, 10000}) {
            b.recordTweet("a", t);
        }
        eq(b.getTweetCountsPerFrequency("minute", "a", 60, 60), List.of(2), "single second");
        eq(b.getTweetCountsPerFrequency("minute", "a", 59, 120), List.of(3, 2), "block boundary");
        eq(b.getTweetCountsPerFrequency("minute", "a", 0, 10000),
                brute("minute", "a", 0, 10000,
                        List.of(0, 59, 60, 60, 119, 120, 10000),
                        List.of("a", "a", "a", "a", "a", "a", "a")), "full range");

        // 跨天/超大时间戳:[0, 10^9] 按天切成 11575 块,首块含 0、末块含 10^9
        TweetCounts big = new TweetCounts();
        big.recordTweet("x", 1_000_000_000);
        big.recordTweet("x", 0);
        int dayChunks = 1_000_000_000 / 86400 + 1;
        List<Integer> dayExpected = new ArrayList<>();
        for (int i = 0; i < dayChunks; i++) {
            dayExpected.add(0);
        }
        dayExpected.set(0, 1);
        dayExpected.set(dayChunks - 1, 1);
        eq(big.getTweetCountsPerFrequency("day", "x", 0, 1_000_000_000), dayExpected, "day chunk");

        // 随机压测：与暴力结果逐项对比
        Random rnd = new Random(20261106);
        for (int round = 0; round < 200; round++) {
            TweetCounts fast = new TweetCounts();
            List<Integer> allTimes = new ArrayList<>();
            List<String> names = new ArrayList<>();
            int ops = 400;
            for (int op = 0; op < ops; op++) {
                if (rnd.nextInt(3) != 0) {
                    String name = "u" + rnd.nextInt(4);
                    int time = rnd.nextInt(10001); // 控制在 1e4 范围便于暴力
                    fast.recordTweet(name, time);
                    allTimes.add(time);
                    names.add(name);
                } else {
                    String name = "u" + rnd.nextInt(5);
                    String freq = switch (rnd.nextInt(3)) {
                        case 0 -> "minute";
                        case 1 -> "hour";
                        default -> "day";
                    };
                    int s = rnd.nextInt(10001);
                    int e = Math.min(10000, s + rnd.nextInt(10001 - s));
                    eq(fast.getTweetCountsPerFrequency(freq, name, s, e),
                            brute(freq, name, s, e, allTimes, names),
                            "random round=" + round + " op=" + op);
                }
            }
        }

        // 大规模性能：10000 条记录 + 10000 次查询
        TweetCounts perf = new TweetCounts();
        List<Integer> times = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            times.add(i * 1000);
        }
        long t0 = System.nanoTime();
        for (int t : times) {
            perf.recordTweet("p", t);
        }
        for (int i = 0; i < 10000; i++) {
            perf.getTweetCountsPerFrequency("minute", "p", 0, 10000);
        }
        long ms = (System.nanoTime() - t0) / 1_000_000;
        System.out.println("checks=" + checks + " perf=" + ms + "ms");
    }
}
