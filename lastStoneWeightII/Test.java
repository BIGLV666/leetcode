import java.util.Arrays;

public class Test {
    private static final Solution solution = new Solution();
    private static final TestCase[] testCases = new TestCase[]{
        new TestCase(new int[]{2, 7, 4, 1, 8, 1}, 1),
        new TestCase(new int[]{31,26,33,21,40}, 5),
    };

    public static void main(String[] args) {
        for (TestCase testCase : testCases) {
            int actual = solution.lastStoneWeightII(testCase.stones);
            if (actual != testCase.expect) {
                System.out.println("stones=" + Arrays.toString(testCase.stones));
                System.out.println("actual=" + actual + " expect=" + testCase.expect);
            }
        }
         System.out.println("ok");
    }
     
}



class TestCase {
    int[] stones;
    int expect;

    TestCase(int[] stones, int expect) {
        this.stones = stones;
        this.expect = expect;
    }
}

