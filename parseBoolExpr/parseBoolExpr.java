package parseBoolExpr;

import java.util.*;

/**
 * <a href="https://leetcode.cn/problems/parsing-a-boolean-expression/">1106. 布尔表达式解析</a>
 *
 * <p>表达式由 {@code t/f}、逗号、{@code & | !} 与括号组成,返回求值结果。
 * 语义:{@code &(...)} 全真才真、{@code |(...)} 有真即真、{@code !(x)} 取反;
 * {@code & |} 至少两个参数,{@code !} 恰一个。</p>
 *
 * <p>解法:单栈归约。非 {@code ')'} 字符直接入栈;遇 {@code ')'} 时不断弹栈收集参数
 * 直到遇见本层 {@code '('}(参数天然都在它之上),丢弃括号、取出紧邻其下的运算符,
 * 按语义求值后把子结果以 {@code t/f} 形式回栈当操作数。
 * 与"遇到 ) 把参数倒腾到临时栈恢复顺序"的写法相比:参数收进 Set 即可,
 * 因为 {@code & |} 满足交换/结合律,顺序无关。</p>
 *
 * <p>复杂度:每个字符至多入栈/出栈一次,时间 O(n);空间 O(n)。</p>
 */
 class Solution {
    public boolean parseBoolExpr(String expression) {
        Deque<Character> st = new ArrayDeque<>();
        for (char c : expression.toCharArray()) {
            if (c == ',') continue;
            if (c != ')') {
                st.push(c);
                continue;
            }   // 运算符/操作数/'(' 直接入栈
            Set<Boolean> args = new HashSet<>();       // 收集本层参数,顺序无关
            while (st.peek() != '(') {                 // 参数都在本层 '(' 之上,遇到即停
                args.add(st.pop() == 't');
            }
            st.pop();                                  // 丢弃本层 '('
            char op = st.pop();                        // 运算符紧邻 '(' 之下
            boolean res = switch (op) {
                case '&' -> !args.contains(false);     // 全真才真
                case '|' -> args.contains(true);       // 有真即真
                default -> !args.iterator().next();   // '!' 恰一个参数
            };
            st.push(res ? 't' : 'f');                  // 子结果回栈当操作数
        }
        return st.pop() == 't';
    }
}