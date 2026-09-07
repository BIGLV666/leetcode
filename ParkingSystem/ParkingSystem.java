package ParkingSystem;

/**
 * <a href="https://leetcode.cn/problems/design-parking-system/">1603. 设计停车系统</a>
 *
 * <p>停车场有大(big)/中(medium)/小(small)三种车位,数量在构造时给定。
 * {@code addCar(carType)}:对应类型有空位则停入并返回 true,否则返回 false。</p>
 *
 * <p>解法:三个计数器,每种车位只需记「剩余数量」,停入即扣减;扣减前先判断是否 &gt; 0。</p>
 *
 * <p>复杂度:构造 O(1),addCar O(1),空间 O(1)。</p>
 */
class ParkingSystem {
    int big;
    int medium;
    int small;
    public ParkingSystem(int big, int medium, int small) {
        this.big = big;
        this.medium = medium;
        this.small = small;
    }

    public boolean addCar(int carType) {
        // 先比较后自减:剩余 > 0 才停入(占位),同时扣减计数
        return switch (carType){
            case 1 -> big-- > 0;
            case 2-> medium-->0;
            case 3-> small-->0;
            default -> throw new IllegalStateException("Unexpected value: " + carType);
        };
    }
}

/**
 * Your ParkingSystem object will be instantiated and called as such:
 * ParkingSystem obj = new ParkingSystem(big, medium, small);
 * boolean param_1 = obj.addCar(carType);
 */