package toGoatLatin;

import java.util.Random;

/** toGoatLatin 无框架测试:官方示例 + 单词边界 + 随机句子对拍 + 长句压测。 */
public class Test {
    private static int checks = 0;

    static void eq(Object actual, Object expected, String msg) {
        checks++;
        if (!java.util.Objects.equals(actual, expected)) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    static boolean isVowel(char c) {
        return "aeiouAEIOU".indexOf(c) >= 0;
    }

    /** 参考实现:逐词按规则拼接,后缀 a 的数量等于单词序号。 */
    static String ref(String sentence) {
        String[] words = sentence.split(" ");
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String w = words[i];
            StringBuilder sb = new StringBuilder();
            if (isVowel(w.charAt(0))) {
                sb.append(w);
            } else {
                sb.append(w.substring(1)).append(w.charAt(0));
            }
            sb.append("ma");
            for (int j = 0; j <= i; j++) {
                sb.append('a');
            }
            if (i > 0) {
                out.append(' ');
            }
            out.append(sb);
        }
        return out.toString();
    }

    public static void main(String[] args) {
        Solution s = new Solution();

        // 官方示例 1
        eq(s.toGoatLatin("I speak Goat Latin"),
                "Imaa peaksmaaa oatGmaaaa atinLmaaaaa", "示例1");
        // 官方示例 2 用参考实现校验
        String ex2 = "The quick brown fox jumped over the lazy dog";
        eq(s.toGoatLatin(ex2), ref(ex2), "示例2");
        // 短词边界
        eq(s.toGoatLatin("a"), "amaa", "单个元音");
        eq(s.toGoatLatin("b"), "bmaa", "单个辅音");
        eq(s.toGoatLatin("A"), "Amaa", "大写元音");
        eq(s.toGoatLatin("Z"), "Zmaa", "大写辅音");
        eq(s.toGoatLatin("apple"), "applemaa", "元音开头");

        // 随机句子对拍:词长 1..8,元音/辅音混合
        Random rnd = new Random(824);
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int t = 0; t < 3000; t++) {
            int words = 1 + rnd.nextInt(12);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < words; i++) {
                if (i > 0) {
                    sb.append(' ');
                }
                int len = 1 + rnd.nextInt(8);
                for (int j = 0; j < len; j++) {
                    sb.append(letters.charAt(rnd.nextInt(letters.length())));
                }
            }
            String sentence = sb.toString();
            eq(s.toGoatLatin(sentence), ref(sentence), "随机句子: " + sentence);
        }

        // 长句压测:1000 个词,验证词数、首词与末词后缀
        String[] many = new String[1000];
        for (int i = 0; i < many.length; i++) {
            many[i] = "a";
        }
        String longSentence = String.join(" ", many);
        long start = System.nanoTime();
        String result = s.toGoatLatin(longSentence);
        long ms = (System.nanoTime() - start) / 1_000_000;
        eq(result.split(" ").length, 1000, "长句词数");
        eq(result.startsWith("amaa "), true, "长句首词");
        eq(result.endsWith("ama"), false, "长句末词不应缺少 a");
        if (ms > 2000) {
            throw new AssertionError("1000 词耗时过长: " + ms + "ms");
        }

        System.out.println("PASS toGoatLatin checks=" + checks + " longSentence=" + ms + "ms");
    }
}
