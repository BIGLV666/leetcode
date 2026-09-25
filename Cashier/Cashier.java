package Cashier;

import java.util.HashMap;
import java.util.Map;

/** 按固定顾客周期计算账单，并在周期末应用折扣。 */
class Cashier {

    private final int n;
    private final int discount;
    private final Map<Integer,Integer> price;
    private int count;

    /** 记录商品价格，并将当前顾客计数初始化为零。 */
    public Cashier(int n, int discount, int[] products, int[] prices) {
        this.n = n;
        this.discount = discount;
        this.price=new HashMap<>();
        this.count = 0;
        for(int i=0;i<products.length;i++){
            price.put(products[i],prices[i]);
        }
    }

    /** 计算本单总价；每第 n 位顾客享受折扣，结算后重新开始计数。 */
    public double getBill(int[] product, int[] amount) {
        count++;
        double total = 0;
        for (int i = 0; i < product.length; i++) {
            total +=price.get(product[i]) * amount[i];
        }
        if(count==n){
            count=0;
            return total- (discount * total) /100.0;
        }

        return total;
    }
}

/**
 * Your Cashier object will be instantiated and called as such:
 * Cashier obj = new Cashier(n, discount, products, prices);
 * double param_1 = obj.getBill(product,amount);
 */
