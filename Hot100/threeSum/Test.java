package Hot100.threeSum;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Test {
    private static final Solution solution = new Solution();

    public static void main(String[] args) {
        assertResult(
                new int[]{-1, 0, 1, 2, -1, -4},
                Set.of("-1,-1,2", "-1,0,1"));
        assertResult(new int[]{0, 0, 0, 0}, Set.of("0,0,0"));
        assertResult(new int[]{1, 2, -2, -1}, Set.of());
        assertResult(new int[]{0, 1, 1}, Set.of());
        assertResult(new int[]{-2, 0, 1, 1, 2}, Set.of("-2,0,2", "-2,1,1"));

        System.out.println("All threeSum tests passed.");
    }

    private static void assertResult(int[] nums, Set<String> expected) {
        Set<String> actual = new HashSet<>();
        for (List<Integer> triple : solution.threeSum(nums)) {
            actual.add(triple.get(0) + "," + triple.get(1) + "," + triple.get(2));
        }
        if (!actual.equals(expected)) {
            throw new AssertionError("actual=" + actual + ", expected=" + expected);
        }
    }
}
