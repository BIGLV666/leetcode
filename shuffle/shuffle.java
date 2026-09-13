package shuffle;

import java.util.Arrays;
import java.util.Random;

/**
 * <a href="https://leetcode.cn/problems/shuffle-an-array/">384. 打乱数组</a>
 *
 * <p>reset() 还原原始数组;shuffle() 返回随机打乱结果,**所有 n! 种排列等概率**。</p>
 *
 * <p>解法:Fisher–Yates 洗牌。copy 一份后从左往右:位置 i 与
 * {@code [i, n-1]} 中随机一个位置交换。每个前缀位置都从"剩余元素"中等概率抽取,
 * 可归纳证明 n! 种排列各以 1/n! 概率出现——这是**均匀洗牌**的充要写法;
 * 若把随机范围写成 {@code [0, n-1]}(i 之前也换)则分布有偏。</p>
 *
 * <p>复杂度:reset/shuffle 均 O(n);空间 O(n)(保存原始副本)。</p>
 */
class Solution {

    private final int[]t;

    public Solution(int[] nums) {
        t=nums;
    }

    public int[] reset() {
        return t;
    }

    public int[] shuffle() {
        Random rand = new Random();
        int []res=new int[t.length];
        System.arraycopy(t,0,res,0,t.length); // 在副本上洗,不动原始数组(reset 依赖它)
        for(int i=0;i<t.length;i++){
            int j=i+rand.nextInt(t.length-i); // 关键:范围 [i, n-1],而非 [0, n-1]
            int temp=res[i];
            res[i]=res[j];
            res[j]=temp;
        }
        return res;

    }
}

/**
 * Your Solution object will be instantiated and called as such:
 * Solution obj = new Solution(nums);
 * int[] param_1 = obj.reset();
 * int[] param_2 = obj.shuffle();
 */