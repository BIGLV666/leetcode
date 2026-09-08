package parseBoolExpr;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** parseBoolExpr 的无框架测试。 */
public class Test {

    public static void main(String[] args) {
        Solution s = new Solution();

        // 力扣官方示例
        check(s.parseBoolExpr("&(|(f,f),!(t))"), false);
        check(s.parseBoolExpr("|(f,f,f,t)"), true);
        check(s.parseBoolExpr("!(&(f,t))"), true);

        // 边界:原子与单参数
        check(s.parseBoolExpr("t"), true);
        check(s.parseBoolExpr("f"), false);
        check(s.parseBoolExpr("!(t)"), false);
        check(s.parseBoolExpr("!(f)"), true);

        // 边界:多重 & / |
        check(s.parseBoolExpr("&(t,t)"), true);
        check(s.parseBoolExpr("&(t,f)"), false);
        check(s.parseBoolExpr("|(f,f)"), false);
        check(s.parseBoolExpr("|(t,f)"), true);

        // 嵌套:!(f)=true; &(t,|(f,t))=true; & 之 → true
        check(s.parseBoolExpr("&(!(f),&(t,|(f,t)))"), true);
        // 嵌套取反传导:!(&(f,|(t,t))) → 内层 false → true
        check(s.parseBoolExpr("!(&(f,|(t,t)))"), true);

        // 随机对拍:递归生成随机表达式,生成时同步求出真值,再验证解析器
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            boolean[] out = new boolean[1];
            String expr = build(random, 3, out);
            check(s.parseBoolExpr(expr), out[0]);
        }

        System.out.println("All tests passed.");
    }

    private static void check(boolean got, boolean expected) {
        if (got != expected) {
            throw new AssertionError("expected " + expected + ", got " + got);
        }
    }

    /**
     * 递归生成符合题面语法的随机表达式,真值写入 out[0]。
     * & | 取 2~4 个参数,! 恰 1 个,深度随递归衰减保证终止。
     */
    private static String build(Random r, int depth, boolean[] out) {
        if (depth == 0 || r.nextInt(4) == 0) {
            boolean v = r.nextBoolean();
            out[0] = v;
            return v ? "t" : "f";
        }
        char op = "&|!".charAt(r.nextInt(3));
        int n = (op == '!') ? 1 : 2 + r.nextInt(3);
        List<String> parts = new ArrayList<>();
        List<Boolean> vals = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            boolean[] child = new boolean[1];
            parts.add(build(r, depth - 1, child));
            vals.add(child[0]);
        }
        boolean res = switch (op) {
            case '&' -> !vals.contains(false);
            case '|' -> vals.contains(true);
            default -> !vals.get(0);
        };
        out[0] = res;
        return op + "(" + String.join(",", parts) + ")";
    }
}