package calculate;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * <a href="https://leetcode.cn/problems/basic-calculator-ii/">227. 基本计算器 II</a>
 *
 * <p>求值含 {@code + - * /} 与空格、无非负整数的表达式;除法向零取整,不允许 eval。</p>
 *
 * <p>解法:双段处理——</p>
 * <ol>
 *   <li>扫描阶段:数字入栈;遇 {@code *} / {@code /} 立即与栈顶(前一个数)结算
 *       (乘除优先级高于加减,必须当场算);{@code +} / {@code -} 只压符号延后;</li>
 *   <li>收尾阶段:栈里只剩纯加法的带符号数序列,逐个弹栈累加。
 *       注意 {@code -} 压的是负数约定:最终求和时 {@code a - b} 表现为 {@code a + (-b)}。</li>
 * </ol>
 *
 * <p>复杂度:时间 O(n),空间 O(n)。</p>
 *
 * <p>已知局限:乘法结转用 int,极端用例(如 100000*100000 = 10^10)会 int 溢出;
 * 题目保证"中间结果在 [-2^31, 2^31-1]"故不触发,但通用计算器应用 long 承接。</p>
 */
class Solution {
    public int calculate(String s) {
        Deque<String> stack = new ArrayDeque<>();

        char[] chars = s.toCharArray();
        for (int i = 0; i < s.length(); ) {
            if (Character.isDigit(chars[i])) {
                Object[] temp = f(chars, i);            // 读完整数字(可能多位)
                stack.addLast((String) temp[0]);
                i = (int) temp[1];
            } else if (chars[i] == '+' || chars[i] == '-') {
                stack.addLast(String.valueOf(chars[i])); // 低优先级:只压符号,延后结算
                i++;
            } else if (chars[i] == '*') {
                int r;
                for (; i < s.length(); i++) {            // 跳过乘号后的空格
                    if (Character.isDigit(chars[i])) {
                        break;
                    }
                }
                Object[] temp = f(chars, i);

                r = Integer.parseInt((String) temp[0]);
                var l = Integer.parseInt(String.valueOf(stack.pollLast()));
                stack.addLast(String.valueOf((r * l)));  // 高优先级:与栈顶立即结算
                i = (int) temp[1];
            } else if (chars[i] == '/') {
                int r;
                for (; i < s.length(); i++) {
                    if (Character.isDigit(chars[i])) {
                        break;
                    }
                }
                Object[] temp = f(chars, i);

                r = Integer.parseInt((String) temp[0]);
                var l = Integer.parseInt(String.valueOf(stack.pollLast()));
                stack.addLast(String.valueOf((l / r)));  // Java 截断除法,恰为"向零取整"
                i = (int) temp[1];
            } else {
                i++;                                     // 空格直接跳过
                continue;
            }
        }

        // 收尾:栈内只剩带符号的数与 +/-,从左到右累加(同级左结合)
        while (stack.size() > 1) {
            Long l = Long.valueOf(stack.pollFirst());
            String m = stack.pollFirst();
            Long r = Long.valueOf(stack.pollFirst());
            switch (m) {
                case "+" -> stack.addFirst(String.valueOf(l + r));
                case "-" -> stack.addFirst(String.valueOf(l - r));
            }
        }
        return Integer.parseInt(stack.pop());

    }

    /** 从 i 起读连续数字,返回 {数字字符串, 下一位置}。 */
    private Object[] f(char[] c, int i) {
        StringBuilder s = new StringBuilder();
        for (; i < c.length; i++) {
            if (!Character.isDigit(c[i])) {
                break;
            }
            if (Character.isDigit(c[i])) s.append(c[i]);
        }
        return new Object[]{s.toString(), i};
    }

}