package suggestedProducts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/** suggestedProducts 的无框架测试:官方示例 + 前缀消失边界 + 随机数据与排序过滤参考实现对拍。 */
public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();

        equal(solution.suggestedProducts(
                        new String[]{"mobile", "mouse", "moneypot", "monitor", "mousepad"},
                        "mouse"),
                List.of(
                        List.of("mobile", "moneypot", "monitor"),
                        List.of("mobile", "moneypot", "monitor"),
                        List.of("mouse", "mousepad"),
                        List.of("mouse", "mousepad"),
                        List.of("mouse", "mousepad")),
                "官方示例1");
        equal(solution.suggestedProducts(new String[]{"havana"}, "havana"),
                repeat(List.of("havana"), 6), "官方示例2");
        equal(solution.suggestedProducts(new String[]{"a"}, "ab"),
                List.of(List.of("a"), List.of()), "后续前缀无匹配");

        Random random = new Random(1268);
        for (int round = 0; round < 2000; round++) {
            int count = 1 + random.nextInt(20);
            Set<String> unique = new HashSet<>();
            while (unique.size() < count) {
                int len = 1 + random.nextInt(6);
                StringBuilder sb = new StringBuilder(len);
                for (int i = 0; i < len; i++) {
                    sb.append((char) ('a' + random.nextInt(4)));
                }
                unique.add(sb.toString());
            }
            String[] products = unique.toArray(new String[0]);
            StringBuilder word = new StringBuilder();
            int wordLength = 1 + random.nextInt(6);
            for (int i = 0; i < wordLength; i++) {
                word.append((char) ('a' + random.nextInt(4)));
            }
            String searchWord = word.toString();
            equal(solution.suggestedProducts(products, searchWord),
                    reference(products, searchWord),
                    "随机 round=" + round + " products=" + unique + " word=" + searchWord);
        }

        System.out.println("All suggestedProducts tests passed.");
    }

    /** 参考实现:对每个前缀过滤、排序后取前 3 个。 */
    private static List<List<String>> reference(String[] products, String searchWord) {
        List<String> sorted = new ArrayList<>(Arrays.asList(products));
        Collections.sort(sorted);
        List<List<String>> answer = new ArrayList<>();
        for (int i = 1; i <= searchWord.length(); i++) {
            String prefix = searchWord.substring(0, i);
            List<String> matches = new ArrayList<>();
            for (String product : sorted) {
                if (product.startsWith(prefix)) {
                    matches.add(product);
                    if (matches.size() == 3) {
                        break;
                    }
                }
            }
            answer.add(matches);
        }
        return answer;
    }

    private static List<List<String>> repeat(List<String> value, int times) {
        List<List<String>> result = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            result.add(value);
        }
        return result;
    }

    private static void equal(List<List<String>> actual, List<List<String>> expected, String name) {
        if (!actual.equals(expected)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
