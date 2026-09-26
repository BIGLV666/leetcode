package evaluate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="https://leetcode.cn/problems/evaluate-the-bracket-pairs-of-a-string/">1807. 替换字符串中的括号内容</a>
 *
 * <p>把 s 中每个 {@code (key)} 替换成 knowledge 里 key 对应的值;key 不在 knowledge
 * 里则替换成 {@code ?}。括号保证成对合法,key 与 value 均为小写字母。</p>
 *
 * <p>两个实现结果一致:</p>
 * <ul>
 *   <li>{@link #evaluateI}:对每个 key 做一次正则整体替换,最后把剩余 {@code (小写串)}
 *       全部换成 {@code ?}。写法短,但每条替换都要扫全串,LC 上会超时,留作对照;</li>
 *   <li>{@link #evaluate}:knowledge 收进 HashMap 后一遍线性扫描,遇到 {@code (} 就吞到
 *       {@code )},用括号内内容查表,查不到给 {@code ?}。时间 O(|s| + Σ|knowledge|)。</li>
 * </ul>
 *
 * <p>复杂度:evaluate 时间 O(|s| + Σ|knowledge|)、空间 O(Σ|knowledge|)。</p>
 */
class Solution {

    /** 解法一:逐 key 正则替换(会超时,留作对照)。 */
    public String evaluateI(String s, List<List<String>> knowledge) {
        for (var list : knowledge) {
            s = s.replaceAll("\\(" + java.util.regex.Pattern.quote(list.get(0)) + "\\)",
                    java.util.regex.Matcher.quoteReplacement(list.get(1)));
        }
        return s.replaceAll("\\([a-z]*\\)", "?");
    }

    /** 解法二:一遍线性扫描 + 哈希查表(推荐)。 */
    public String evaluate(String s, List<List<String>> knowledge) {
        Map<String, String> table = new HashMap<>();
        for (var list : knowledge) {
            table.put(list.get(0), list.get(1));
        }

        StringBuilder sb = new StringBuilder();
        char[] chars = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            if (chars[i] == '(') {
                StringBuilder builder = new StringBuilder();
                while (chars[i] != ')') {           // 括号保证成对,不必担心越界
                    builder.append(chars[i]);       // 把 '(' 一起收进来,取 key 时再截掉
                    i++;
                }
                sb.append(table.getOrDefault(builder.substring(1), "?"));
            } else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }
}
