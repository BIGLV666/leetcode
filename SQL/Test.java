package SQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/** Design SQL(2408) 无框架测试:增删查导 + 非法参数 + 行号不复用/升序 + 随机操作对拍 + 压测。 */
public class Test {
    private static int checks = 0;

    static void eq(Object actual, Object expected, String msg) {
        checks++;
        if (!java.util.Objects.equals(actual, expected)) {
            throw new AssertionError(msg + ": expected=" + expected + ", actual=" + actual);
        }
    }

    /** 参考模型:行用 TreeMap 保存,天然保证 exp 按行号升序。 */
    static class Model {
        final Map<String, Integer> cols = new HashMap<>();
        final Map<String, TreeMap<Integer, List<String>>> db = new HashMap<>();
        final Map<String, Integer> next = new HashMap<>();

        Model(List<String> names, List<Integer> columns) {
            for (int i = 0; i < names.size(); i++) {
                cols.put(names.get(i), columns.get(i));
                db.put(names.get(i), new TreeMap<>());
                next.put(names.get(i), 1);
            }
        }

        boolean ins(String name, List<String> row) {
            if (!db.containsKey(name) || row.size() != cols.get(name)) {
                return false;
            }
            int id = next.get(name);
            next.put(name, id + 1);
            db.get(name).put(id, new ArrayList<>(row));
            return true;
        }

        void rmv(String name, int rowId) {
            if (db.containsKey(name)) {
                db.get(name).remove(rowId);
            }
        }

        String sel(String name, int rowId, int columnId) {
            if (!db.containsKey(name) || columnId > cols.get(name) || columnId < 1) {
                return "<null>";
            }
            List<String> row = db.get(name).get(rowId);
            return row == null ? "<null>" : row.get(columnId - 1);
        }

        List<String> exp(String name) {
            List<String> out = new ArrayList<>();
            TreeMap<Integer, List<String>> table = db.get(name);
            if (table == null) {
                return out;
            }
            for (Map.Entry<Integer, List<String>> e : table.entrySet()) {
                StringBuilder sb = new StringBuilder().append(e.getKey());
                for (String v : e.getValue()) {
                    sb.append(',').append(v);
                }
                out.add(sb.toString());
            }
            return out;
        }
    }

    public static void main(String[] args) {
        List<String> names = Arrays.asList("one", "two", "three");
        List<Integer> cols = Arrays.asList(2, 3, 1);

        // 基础流程:插入、查询、导出、删除、行号不复用
        SQL sql = new SQL(names, cols);
        eq(sql.ins("one", Arrays.asList("a", "b")), true, "插入第一行");
        eq(sql.ins("one", Arrays.asList("c", "d")), true, "插入第二行");
        eq(sql.sel("one", 1, 2), "b", "查询第一行第二列");
        eq(sql.exp("one"), Arrays.asList("1,a,b", "2,c,d"), "导出两行");
        sql.rmv("one", 1);
        eq(sql.sel("one", 1, 1), "<null>", "删除后查询");
        eq(sql.exp("one"), Arrays.asList("2,c,d"), "删除后导出");
        eq(sql.ins("one", Arrays.asList("e", "f")), true, "删除后继续插入");
        eq(sql.exp("one"), Arrays.asList("2,c,d", "3,e,f"), "行号不复用且升序");

        // 非法参数
        eq(sql.ins("missing", Arrays.asList("x")), false, "未知表插入");
        eq(sql.ins("two", Arrays.asList("x")), false, "列数不符");
        eq(sql.sel("missing", 1, 1), "<null>", "未知表查询");
        eq(sql.sel("one", 99, 1), "<null>", "未知行查询");
        eq(sql.sel("two", 1, 4), "<null>", "列号越界");
        sql.rmv("missing", 1);
        sql.rmv("one", 99);
        eq(sql.exp("missing"), Arrays.asList(), "未知表导出");
        eq(sql.exp("two"), Arrays.asList(), "空表导出");

        // exp 升序回归:大量插入/删除后,行号必须按升序返回
        SQL orderSql = new SQL(Arrays.asList("t"), Arrays.asList(1));
        for (int batch = 0; batch < 100; batch++) {
            for (int i = 0; i < 5; i++) {
                orderSql.ins("t", Arrays.asList("v"));
            }
            for (int id = batch * 5 + 1; id <= batch * 5 + 5; id++) {
                orderSql.rmv("t", id);
            }
        }
        // 此时 nextRowId=501,先插入 495..499 号,再删掉小的,制造行号跨越哈希桶
        List<String> expected = new ArrayList<>();
        for (int i = 0; i < 499; i++) {
            orderSql.ins("t", Arrays.asList("x" + i));
        }
        for (int id = 501; id <= 990; id++) {
            orderSql.rmv("t", id);
        }
        for (int id = 991; id <= 999; id++) {
            expected.add(id + ",x" + (id - 501));
        }
        eq(orderSql.exp("t"), expected, "跨越哈希桶后仍按行号升序");

        // 随机操作对拍
        Random rnd = new Random(2408);
        for (int t = 0; t < 300; t++) {
            List<String> n = Arrays.asList("a", "b", "c");
            List<Integer> c = Arrays.asList(1 + rnd.nextInt(3), 1 + rnd.nextInt(3), 1 + rnd.nextInt(3));
            SQL fast = new SQL(n, c);
            Model slow = new Model(n, c);
            for (int op = 0; op < 300; op++) {
                String name = n.get(rnd.nextInt(n.size()));
                int k = rnd.nextInt(10);
                if (k < 5) {
                    List<String> row = new ArrayList<>();
                    int width = 1 + rnd.nextInt(3);
                    for (int j = 0; j < width; j++) {
                        row.add("v" + rnd.nextInt(5));
                    }
                    eq(fast.ins(name, row), slow.ins(name, row), "随机 ins");
                } else if (k < 7) {
                    int id = 1 + rnd.nextInt(20);
                    fast.rmv(name, id);
                    slow.rmv(name, id);
                } else if (k < 9) {
                    int id = 1 + rnd.nextInt(20);
                    int col = 1 + rnd.nextInt(4);
                    eq(fast.sel(name, id, col), slow.sel(name, id, col), "随机 sel");
                } else {
                    eq(fast.exp(name), slow.exp(name), "随机 exp");
                }
            }
        }

        // 10^4 操作压测
        SQL big = new SQL(Arrays.asList("t"), Arrays.asList(3));
        Model ref = new Model(Arrays.asList("t"), Arrays.asList(3));
        long start = System.nanoTime();
        for (int i = 0; i < 10_000; i++) {
            if (i % 3 == 0) {
                List<String> row = Arrays.asList("a", "b", "c");
                eq(big.ins("t", row), ref.ins("t", row), "压测 ins");
            } else if (i % 3 == 1) {
                int id = 1 + rnd.nextInt(Math.max(1, i));
                big.rmv("t", id);
                ref.rmv("t", id);
            } else {
                eq(big.exp("t"), ref.exp("t"), "压测 exp i=" + i);
            }
        }
        long ms = (System.nanoTime() - start) / 1_000_000;
        if (ms > 4000) {
            throw new AssertionError("10^4 操作耗时过长: " + ms + "ms");
        }

        System.out.println("PASS SQL checks=" + checks + " stress=" + ms + "ms");
    }
}
