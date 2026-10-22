package 牛客.练习赛158.赛琳的对线特训;

import com.sun.source.tree.ArrayAccessTree;
import leetcode.ListNode;

import java.io.IOException;
import java.util.Arrays;

public class Test {

    public static  void main(String[] args) throws IOException {
        check(new long[]{2,3 ,1 ,4},9,3);
        check(new long[]{4,0,1},8,1);
        check(new long[]{1,2,1},9,-1);
        check(new long[]{1000000000 ,1000000000},1000000000000000L,-1);
        check(new long[]{1000000000 ,1000000000},2100000000L,2);
    }
    private static void check(long []arr,long k,long want){
        int ans=Main.func(arr, k);
        if(ans!=want){
            throw new AssertionError("输入"+ Arrays.toString(arr)+ ": expected " + ans + ", got " + want);
        }
    }

}
/*
6
4 9
2 3 1 4
3 8
4 0 1
3 9
1 2 1
4 12
1 2 3 3
3 8
3 2 0
3 1
0 0 0
输出
复制
3
1
-1
4
2
-1
 */
