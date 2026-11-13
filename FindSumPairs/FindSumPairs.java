package FindSumPairs;
import java.util.HashMap;
import java.util.Map;
class FindSumPairs {
    private int[] nums1;
    private int[] nums2;
    private Map<Integer, Integer> table;
    public FindSumPairs(int[] nums1, int[] nums2) {
        this.nums1 = nums1;
        this.nums2 = nums2;
        table = new HashMap<>();
        for (int num : nums2) {
            table.put(num, table.getOrDefault(num, 0) + 1);
        }
    }

    public void add(int index, int val) {
        int old = nums2[index];
        nums2[index] += val;
        table.put(old, table.get(old) - 1);
        table.put(nums2[index], table.getOrDefault(nums2[index], 0) + 1);
    }

    public int count(int tot) {
        int count = 0;
        for (int num : nums1) {
            count += table.getOrDefault(tot - num, 0);
        }
        return count;
    }
}

/**
 * Your FindSumPairs object will be instantiated and called as such:
 * FindSumPairs obj = new FindSumPairs(nums1, nums2);
 * obj.add(index,val);
 * int param_2 = obj.count(tot);
 */
