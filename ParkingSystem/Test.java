package ParkingSystem;

import java.util.Random;

/** ParkingSystem 的无框架测试。 */
public class Test {
    public static void main(String[] args) {
        // 力扣官方示例:["ParkingSystem","addCar","addCar","addCar","addCar"]
        //              [[1,1,0],[1],[2],[3],[1]] → [null,true,true,false,false]
        ParkingSystem ps = new ParkingSystem(1, 1, 0);
        check(ps, 1, true);
        check(ps, 2, true);
        check(ps, 3, false); // 小车位初始为 0
        check(ps, 1, false); // 大车位已被占满

        // 边界:全部为 0,任何车都停不进
        ParkingSystem empty = new ParkingSystem(0, 0, 0);
        check(empty, 1, false);
        check(empty, 2, false);
        check(empty, 3, false);

        // 边界:同一类型连续停满再溢出
        ParkingSystem big = new ParkingSystem(3, 0, 0);
        check(big, 1, true);
        check(big, 1, true);
        check(big, 1, true);
        check(big, 1, false); // 恰好停满后溢出

        // 各类型互不影响
        ParkingSystem mixed = new ParkingSystem(1, 1, 1);
        check(mixed, 1, true);
        check(mixed, 1, false);
        check(mixed, 2, true); // 大的满了不影响中
        check(mixed, 3, true);
        check(mixed, 3, false);

        // 随机数据:与「数组计数」参考实现对拍
        Random random = new Random(42);
        for (int round = 0; round < 2000; round++) {
            int b = random.nextInt(4), m = random.nextInt(4), s = random.nextInt(4);
            ParkingSystem sys = new ParkingSystem(b, m, s);
            int[] ref = {b, m, s}; // 参考实现的剩余计数
            for (int op = 0; op < 12; op++) {
                int carType = 1 + random.nextInt(3);
                boolean expected = --ref[carType - 1] >= 0;
                boolean got = sys.addCar(carType);
                if (expected != got) {
                    throw new AssertionError("round " + round + " op " + op + " addCar(" + carType
                            + "): expected " + expected + ", got " + got);
                }
            }
        }

        System.out.println("All tests passed.");
    }

    private static void check(ParkingSystem ps, int carType, boolean expected) {
        boolean got = ps.addCar(carType);
        if (got != expected) {
            throw new AssertionError("addCar(" + carType + "): expected " + expected + ", got " + got);
        }
    }
}