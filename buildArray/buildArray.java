package buildArray;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://leetcode.cn/problems/build-an-array-with-stack-operations/">1441. 用栈操作构建数组</a>
 *
 * <p>给你一个目标数组 {@code target}(严格递增)和整数 {@code n}。空栈 + 数字流 1..n,
 * 每次可执行 {@code Push}(读入流中下一个数字并压栈)或 {@code Pop}(弹出栈顶)。
 * 返回能按下标逐一构建出 target 的操作序列;target 构建完成后停止读取即可。</p>
 *
 * <p>解法:双指针模拟。target 严格递增,按 1..n 顺序读数:是 target 当前需要的数就只 Push,
 * 不是就 Push+Pop 跳过;target 构建完成后提前结束。</p>
 *
 * <p>复杂度:时间 O(n)(读数到 target 末元素即停),额外空间 O(1)(不计输出)。</p>
 */
class Solution {
    public List<String> buildArray(int[] target, int n) {
        int top=0; // 下一个待构建的 target 下标
        List<String> ans=new ArrayList<>();
        for(int i=1;i<=n;i++){
            if(top>=target.length) // target 已全部构建,提前停止读取
                break;
            if(target[top]==i){ // 当前读到的数正是需要的,只压栈
                ans.add("Push");
                top++;
            }else { // 不是需要的数:压栈后再弹出跳过
                ans.add("Push");
                ans.add("Pop");
            }
        }
        return ans;
    }
}