package pickIndex;

import java.util.concurrent.ThreadLocalRandom;

class Solution {

    private final int[]prefixSum;

    public Solution(int[] w) {
        prefixSum = new int[w.length];
        prefixSum[0] = w[0];
        for (int i = 1; i < w.length; i++) {
            prefixSum[i] = prefixSum[i - 1] + w[i];
        }
    }

    public int pickIndex() {
        ThreadLocalRandom random=ThreadLocalRandom.current();
        int target = random.nextInt(prefixSum[prefixSum.length - 1]) + 1;
        int lo = 0, hi = prefixSum.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (prefixSum[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }
}
