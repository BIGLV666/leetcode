package maxWidthOfVerticalArea;

import java.util.Arrays;
import java.util.Comparator;

class Solution {
    public int maxWidthOfVerticalArea(int[][] points) {
        int[] ans = {0, -1};
        Arrays.stream(points).sorted(Comparator.comparingInt(a -> a[0])).forEach(
                a -> {
                    if (ans[1] != -1) {
                        ans[0] = Math.max(ans[0], a[0] - ans[1]);
                    }
                    ans[1] = a[0];
                }
        );
        return ans[0];
    }
}
