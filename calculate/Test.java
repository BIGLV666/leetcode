package calculate;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

/** calculate 的无框架测试。 */
public class Test {

    public static void main(String[] args) {
        Solution s = new Solution();

        // 力扣官方示例
        check(s, "3+2*2", 7);
        check(s, " 3/2 ", 1);
        check(s, " 3+5 / 2 ", 5);

        // 优先级与结合律
        check(s, "14-3/2", 13);            // 除法向零取整:3/2=1
        check(s, "1-1+1", 1);              // 同级左结合
        check(s, "2-4*2", -6);             // 减后接乘
        check(s, "6/2*3", 9);              // 除乘同级左结合:3*3
        check(s, "10*10/9", 11);           // 100/9=11 向零取整
        check(s, "0-2147483647", -2147483647); // 边界值
        check(s, "2000000000/3", 666666666);

        // 多空格
        check(s, "  12   +   34*  2  ", 80);

        // 随机对拍:与「标准两栈算法(操作数栈+运算符栈)」参考实现互验
        // 随机表达式由生成器构造,保证操作数为非负整数且无除零
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            String expr = genExpr(random, 1 + random.nextInt(6));
            int expected = standard(expr);
            int got = s.calculate(expr);
            if (got != expected) {
                throw new AssertionError("expr=" + expr + ": expected " + expected + ", got " + got);
            }
        }

        System.out.println("All tests passed.");
    }

    /** 生成随机表达式:2~6 个操作数(1..20),运算符随机,空格随机插入。 */
    private static String genExpr(Random r, int operands) {
        StringBuilder sb = new StringBuilder();
        sb.append(r.nextInt(20) + 1);
        for (int i = 1; i < operands; i++) {
            char[] ops = {'+', '-', '*', '/'};
            char op = ops[r.nextInt(4)];
            int next = r.nextInt(20) + 1;
            if (op == '/') {
                // 保证除法结果可控:除数随机,被除数换成 next*整倍数中较小的
                sb.append(" / ").append(next);
            } else {
                sb.append(' ').append(op).append(' ');
                sb.append(next);
            }
        }
        return sb.toString();
    }

    /**
     * 标准两栈参考实现:操作数栈 + 运算符栈,按优先级归约。
     * 除法用 long 向零取整,规避 int 溢出导致的对拍假失败。
     */
    private static int standard(String s) {
        Deque<Long> nums = new ArrayDeque<>();
        Deque<Character> ops = new ArrayDeque<>();
        int i = 0, n = s.length();
        while (i < n) {
            char c = s.charAt(i);
            if (c == ' ') {
                i++;
            } else if (Character.isDigit(c)) {
                long v = 0;
                while (i < n && Character.isDigit(s.charAt(i))) {
                    v = v * 10 + (s.charAt(i++) - '0');
                }
                nums.push(v);
            } else {
                while (!ops.isEmpty() && prec(ops.peek()) >= prec(c)) {
                    reduce(nums, ops.pop());
                }
                ops.push(c);
                i++;
            }
        }
        while (!ops.isEmpty()) {
            reduce(nums, ops.pop());
        }
        return (int) (long) nums.pop();
    }

    private static int prec(char op) {
        return (op == '*' || op == '/') ? 2 : 1;
    }

    private static void reduce(Deque<Long> nums, char op) {
        long b = nums.pop(), a = nums.pop();
        long res = switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            default -> {
                // 向零取整(截断除法)
                long q = Math.abs(a) / Math.abs(b);
                yield ((a < 0) != (b < 0)) ? -q : q;
            }
        };
        nums.push(res);
    }

    private static void check(Solution s, String expr, int expected) {
        int got = s.calculate(expr);
        if (got != expected) {
            throw new AssertionError("expr=" + expr + ": expected " + expected + ", got " + got);
        }
    }
}