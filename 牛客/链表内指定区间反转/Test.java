package 牛客.链表内指定区间反转;

import leetcode.*;

import java.util.Arrays;

public class Test {

    static class TestCase{
        ListNode head;
        int m;
        int n;
        int []e;
        public TestCase(int[]head,int m,int n){
            this.head=ListNode.buildList(head);
            this.m=m;
            this.n=n;
            this.e=head;
        }
    }



    public static void  main(String[] args) {
        check(new Solution(),test1,new int[]{1,4,3,2,5});
        check(new Solution(),test2,new int[]{5});
    }


    private static TestCase test1=new TestCase(new int[]{1,2,3,4,5},2,4);
    private static  TestCase test2=new TestCase(new int[]{5},1,1);


    private static void check(Solution solution,TestCase test,int[] want){
        ListNode got= solution.reverseBetween(test.head,test.m,test.n);
        if(!Arrays.equals(ListNode.listToArray(got), want)){
            throw new AssertionError("输入"+Arrays.toString(test.e)+ ": expected " + ListNode.listToString(got) + ", got " + Arrays.toString(want));
        }
    }


}
