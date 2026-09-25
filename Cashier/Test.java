package Cashier;

import java.util.Random;

/** Cashier 的无框架测试。 */
public class Test {
    public static void main(String[] args) {
        Cashier cashier = new Cashier(3, 20, new int[] {1, 2, 3, 4}, new int[] {100, 200, 300, 400});
        check(cashier.getBill(new int[] {1, 2}, new int[] {1, 1}), 300.0, "第1笔不打折");
        check(cashier.getBill(new int[] {3, 4}, new int[] {1, 1}), 700.0, "第2笔不打折");
        check(cashier.getBill(new int[] {1, 4}, new int[] {2, 1}), 480.0, "第3笔打八折并重置");
        check(cashier.getBill(new int[] {2}, new int[] {3}), 600.0, "重置后的第1笔");

        Cashier single = new Cashier(1, 50, new int[] {7}, new int[] {99});
        check(single.getBill(new int[] {7}, new int[] {2}), 99.0, "n=1 每笔打折");
        check(single.getBill(new int[] {}, new int[] {}), 0.0, "空购物单");

        handComputedChecks();
        crossChecks();
        randomChecks();

        System.out.println("All tests passed.");
    }

    /** 手算结果的定点用例：覆盖 100% 折扣、0 折扣、n 大于总笔数等边界。 */
    private static void handComputedChecks() {
        Cashier halfOff = new Cashier(2, 100, new int[] {1, 2}, new int[] {50, 30});
        check(halfOff.getBill(new int[] {1}, new int[] {1}), 50.0, "100% 折扣前一笔原价");
        check(halfOff.getBill(new int[] {2}, new int[] {1}), 0.0, "第2笔 100% 折扣全免");
        check(halfOff.getBill(new int[] {1, 2}, new int[] {1, 1}), 80.0, "折扣轮重置后恢复原价");
        check(halfOff.getBill(new int[] {1}, new int[] {2}), 0.0, "第4笔再次全免");

        Cashier noDiscount = new Cashier(2, 0, new int[] {9}, new int[] {12});
        check(noDiscount.getBill(new int[] {9}, new int[] {3}), 36.0, "折扣为 0 时不减价");
        check(noDiscount.getBill(new int[] {9}, new int[] {1}), 12.0, "折扣为 0 的折扣轮同样原价");

        Cashier never = new Cashier(200, 30, new int[] {4}, new int[] {25});
        check(never.getBill(new int[] {4}, new int[] {1}), 25.0, "n 大于总笔数时始终原价");
        check(never.getBill(new int[] {4}, new int[] {2}), 50.0, "n 大于总笔数时始终原价2");
    }

    /** 用若干固定脚本对拍题解与参考实现。 */
    private static void crossChecks() {
        // 脚本1：n=1，每笔都是折扣轮
        crossCheck("脚本1", 1, 25, new int[] {3, 8}, new int[] {40, 10},
                new int[][] {{3}, {8, 3}, {8}},
                new int[][] {{1}, {2, 1}, {4}});

        // 脚本2：n=4，只有第 4、8 笔打折
        crossCheck("脚本2", 4, 15, new int[] {1, 2, 5}, new int[] {100, 7, 33},
                new int[][] {{1}, {2}, {5, 2}, {1, 5}, {2}, {5}},
                new int[][] {{1}, {3}, {1, 2}, {2, 1}, {5}, {1}});

        // 脚本3：n=2 且折扣为 0，折扣轮不影响金额
        crossCheck("脚本3", 2, 0, new int[] {6, 7}, new int[] {9, 11},
                new int[][] {{6, 7}, {7}, {6}},
                new int[][] {{1, 1}, {2}, {3}});

        // 脚本4：商品较多、同一笔内混合多种商品
        crossCheck("脚本4", 3, 33, new int[] {10, 20, 30, 40, 50}, new int[] {1, 2, 3, 4, 5},
                new int[][] {{10, 20, 30}, {40, 50}, {10, 50, 20}, {30}},
                new int[][] {{5, 2, 1}, {3, 3}, {1, 1, 1}, {7}});
    }

    /**
     * 把同一串购物单分别喂给题解与参考实现，逐笔比对。
     * 参考实现用数组存价格，并用「已服务顾客数 % n == 0」判断折扣轮。
     */
    private static void crossCheck(String name, int n, int discount, int[] products, int[] prices,
            int[][] billProducts, int[][] billAmounts) {
        Cashier solution = new Cashier(n, discount, products, prices);
        ReferenceCashier reference = new ReferenceCashier(n, discount, products, prices);
        for (int i = 0; i < billProducts.length; i++) {
            double actual = solution.getBill(billProducts[i], billAmounts[i]);
            double expected = reference.getBill(billProducts[i], billAmounts[i]);
            checkClose(actual, expected, name + " 第" + (i + 1) + "笔");
        }
    }

    /** 随机对拍：固定种子跑 2000 轮，每轮随机门店参数与购物单，并确认确实命中过折扣轮。 */
    private static void randomChecks() {
        final int rounds = 2000;
        Random random = new Random(20240924);
        int discountBills = 0;
        int plainBills = 0;
        for (int round = 0; round < rounds; round++) {
            int n = 1 + random.nextInt(5);
            int discount = random.nextInt(5) == 0 ? 0 : 1 + random.nextInt(100);
            int productKinds = 1 + random.nextInt(6);
            int[] products = new int[productKinds];
            int[] prices = new int[productKinds];
            boolean[] used = new boolean[201];
            for (int i = 0; i < productKinds; i++) {
                int id;
                do {
                    id = 1 + random.nextInt(200);
                } while (used[id]);
                used[id] = true;
                products[i] = id;
                prices[i] = 1 + random.nextInt(100);
            }

            Cashier solution = new Cashier(n, discount, products, prices);
            ReferenceCashier reference = new ReferenceCashier(n, discount, products, prices);
            int bills = 1 + random.nextInt(24);
            for (int t = 1; t <= bills; t++) {
                int itemKinds = 1 + random.nextInt(Math.min(4, productKinds));
                int[] product = new int[itemKinds];
                int[] amount = new int[itemKinds];
                for (int i = 0; i < itemKinds; i++) {
                    product[i] = products[random.nextInt(productKinds)];
                    amount[i] = 1 + random.nextInt(10);
                }

                double actual = solution.getBill(product, amount);
                double expected = reference.getBill(product, amount);
                checkClose(actual, expected, "随机第" + round + "轮第" + t + "笔");

                int plainTotal = 0;
                for (int i = 0; i < product.length; i++) {
                    plainTotal += priceOf(products, prices, product[i]) * amount[i];
                }
                if (t % n == 0) {
                    // 折扣只作用于第 n、2n、… 位顾客
                    discountBills++;
                    if (discount > 0 && !(actual < plainTotal)) {
                        throw new AssertionError("随机第" + round + "轮第" + t + "笔应打折: 原价 " + plainTotal
                                + ", 实收 " + actual);
                    }
                } else {
                    plainBills++;
                    if (Double.compare(actual, plainTotal) != 0) {
                        throw new AssertionError("随机第" + round + "轮第" + t + "笔不该打折: 原价 " + plainTotal
                                + ", 实收 " + actual);
                    }
                }
            }
        }
        if (discountBills == 0 || plainBills == 0) {
            throw new AssertionError("随机用例未覆盖折扣轮与原价轮: 折扣轮 " + discountBills + ", 原价轮 " + plainBills);
        }
    }

    /** 在已注册的商品表里查价格。 */
    private static int priceOf(int[] products, int[] prices, int id) {
        for (int i = 0; i < products.length; i++) {
            if (products[i] == id) {
                return prices[i];
            }
        }
        throw new AssertionError("随机用例使用了未注册的商品: " + id);
    }

    /** 独立参考实现：数组存价格，用「已服务顾客数 % n == 0」判断折扣轮。 */
    private static class ReferenceCashier {
        private final int n;
        private final int discount;
        private final int[] priceOfProduct;
        private int served;

        ReferenceCashier(int n, int discount, int[] products, int[] prices) {
            this.n = n;
            this.discount = discount;
            this.priceOfProduct = new int[201];
            this.served = 0;
            for (int i = 0; i < products.length; i++) {
                priceOfProduct[products[i]] = prices[i];
            }
        }

        double getBill(int[] product, int[] amount) {
            served++;
            int total = 0;
            for (int i = 0; i < product.length; i++) {
                total += priceOfProduct[product[i]] * amount[i];
            }
            if (discount != 0 && served % n == 0) {
                return total - discount * total / 100.0;
            }
            return total;
        }
    }

    private static void check(double actual, double expected, String name) {
        if (Double.compare(actual, expected) != 0) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }

    /** 浮点对拍用容差比较，避免同一实数因运算顺序不同产生末位差异。 */
    private static void checkClose(double actual, double expected, String name) {
        if (Math.abs(actual - expected) > 1e-9) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
