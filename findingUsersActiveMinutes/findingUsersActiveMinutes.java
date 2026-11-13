package findingUsersActiveMinutes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
class Solution {
    public int[] findingUsersActiveMinutes(int[][] logs, int k) {
        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (int[] user : logs) {
            Set<Integer> set = map.getOrDefault(user[0], new HashSet<Integer>());
            set.add(user[1]);
            map.put(user[0], set);
        }
        int[] ans = new int[k];
        for (var entry : map.entrySet()) {
            ans[entry.getValue().size() - 1]++;
        }
        return ans;
    }
}
