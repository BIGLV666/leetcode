package entityParser;

import java.util.Random;

/** entityParser 的无框架测试。 */
public class Test {
    /** 题目规定的六个 HTML 实体；&amp; 排在最后处理。 */
    private static final String[] FORMS = {"&quot;", "&apos;", "&gt;", "&lt;", "&frasl;", "&amp;"};
    private static final String[] CHARS = {"\"", "'", ">", "<", "/", "&"};

    /** 随机用例的拼装片段，混合完整实体、实体前缀与普通字符。 */
    private static final String[] TOKENS = {
        "&amp;", "&quot;", "&apos;", "&gt;", "&lt;", "&frasl;",
        "&amp;quot;", "&amp;amp;", "&amp;lt;", "&quot;&amp;",
        "&", "&a", "&amp", "&quo", "amp;", "quot;", "apos;", "gt;", "lt;", "frasl;",
        ";", "x", " ", "\""
    };

    public static void main(String[] args) {
        Solution solution = new Solution();
        check(solution.entityParser("&amp; is an HTML entity but &ambassador; is not."),
                "& is an HTML entity but &ambassador; is not.", "官方示例1");
        check(solution.entityParser("and I quote: &quot;...&quot;"),
                "and I quote: \"...\"", "官方示例2");
        check(solution.entityParser("Stay home! Practice on Leetcode :)"),
                "Stay home! Practice on Leetcode :)", "官方示例3");
        check(solution.entityParser("&apos; &gt; &lt; &frasl; &amp; &quot;"),
                "' > < / & \"", "全部实体");
        check(solution.entityParser("&amp;quot; &quot;&amp;"),
                "&quot; \"&", "实体替换顺序");
        check(solution.entityParser(""), "", "空字符串");

        crossChecks(solution);
        randomChecks(solution);
        System.out.println("All tests passed.");
    }

    /** 与「一次扫描 + 实体表匹配」的独立参考实现对拍，并校验二次编码等易错用例。 */
    private static void crossChecks(Solution solution) {
        // 每条输入都先与参考实现比对，再校验手算的期望值
        String[][] cases = {
            {"&amp;quot;", "&quot;"},
            {"&amp;amp;", "&amp;"},
            {"&amp;lt;", "&lt;"},
            {"&amp;apos;", "&apos;"},
            {"&amp;gt;&amp;lt;", "&gt;&lt;"},
            {"&amp;amp;amp;", "&amp;amp;"},
            {"&quot;amp;", "\"amp;"},
            {"&am&lt;p;", "&am<p;"},
            {"&frasl;&amp;frasl;", "/&frasl;"},
            {"&quot;&quot;", "\"\""},
            {"&amp;&amp;", "&&"},
            {"&&amp;;", "&&;"},
            {"&&", "&&"},
            {"&", "&"},
            {"&amp", "&amp"},
            {"&quot", "&quot"},
            {"&gte;", "&gte;"},
            {"&GT;", "&GT;"},
            {"&#38;", "&#38;"},
            {"&unknown; &amp &quot &", "&unknown; &amp &quot &"},
            {"a&amp;b&lt;c&gt;d&quot;e&apos;f&frasl;g", "a&b<c>d\"e'f/g"},
            {"&amp;&quot;&apos;&gt;&lt;&frasl;", "&\"'></"}
        };
        for (int i = 0; i < cases.length; i++) {
            String input = cases[i][0];
            String expected = cases[i][1];
            check(reference(input), expected, "参考实现自检 " + input);
            check(solution.entityParser(input), expected, "对拍 " + input);
        }
    }

    /** 随机对拍：固定种子跑 2000 轮，用片段拼出随机文本逐条比对。 */
    private static void randomChecks(Solution solution) {
        final int rounds = 2000;
        Random random = new Random(20240924);
        int changedCases = 0;
        int doubleEncodedCases = 0;
        for (int round = 0; round < rounds; round++) {
            int tokenCount = 1 + random.nextInt(10);
            StringBuffer text = new StringBuffer();
            for (int i = 0; i < tokenCount; i++) {
                text.append(TOKENS[random.nextInt(TOKENS.length)]);
            }
            String input = text.toString();
            String actual = solution.entityParser(input);
            String expected = reference(input);
            check(actual, expected, "随机第" + round + "轮 \"" + input + "\"");
            if (!input.equals(actual)) {
                changedCases++;
            }
            if (input.indexOf("&amp;quot;") >= 0) {
                doubleEncodedCases++;
            }
        }
        if (changedCases == 0) {
            throw new AssertionError("随机用例没有产生任何替换");
        }
        if (doubleEncodedCases == 0) {
            throw new AssertionError("随机用例没有覆盖 &amp;quot; 这类二次编码");
        }
    }

    /**
     * 独立参考实现：一次从左到右扫描，遇到 '&' 就按实体表逐项匹配，
     * 命中即输出对应字符并跳过整个实体，未命中则原样输出 '&'。
     * 由于被替换出的 '&' 不会被重新扫描，等价于题面要求的「&amp; 最后替换」。
     */
    private static String reference(String text) {
        StringBuffer result = new StringBuffer();
        int i = 0;
        while (i < text.length()) {
            boolean matched = false;
            if (text.charAt(i) == '&') {
                for (int k = 0; k < FORMS.length; k++) {
                    if (text.startsWith(FORMS[k], i)) {
                        result.append(CHARS[k]);
                        i += FORMS[k].length();
                        matched = true;
                        break;
                    }
                }
            }
            if (!matched) {
                result.append(text.charAt(i));
                i++;
            }
        }
        return result.toString();
    }

    private static void check(String actual, String expected, String name) {
        if (!expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
