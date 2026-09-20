package AllOne;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class AllOne {
    TreeMap<Integer, Map<String, Integer>> table;
    Map<String, Integer> countMap;

    public AllOne() {
        table = new TreeMap<>();
        countMap = new HashMap<>();
    }

    public void inc(String key) {
        if (countMap.containsKey(key)) {
            int count = countMap.get(key);
            countMap.put(key, count + 1);
            var temp = table.get(count);
            temp.remove(key);
            if (temp.isEmpty()) {
                table.remove(count);
            }
            var temp2 = table.getOrDefault(count + 1, new TreeMap<>());
            temp2.put(key, count + 1);
            table.put(count + 1, temp2);
            return;
        }
        countMap.put(key, 1);
        var temp = table.getOrDefault(1, new TreeMap<>());
        temp.put(key, 1);
        table.put(1, temp);

    }

    public void dec(String key) {
        int count = countMap.get(key);

        Map<String, Integer> temp = table.get(count);
        temp.remove(key);

        if (temp.isEmpty()) {
            table.remove(count);
        }

        if (count == 1) {
            // 计数从 1 变成 0，key 应该彻底删除
            countMap.remove(key);
            return;
        }

        countMap.put(key, count - 1);

        Map<String, Integer> temp2 = table.getOrDefault(count - 1, new HashMap<>());

        temp2.put(key, count - 1);
        table.put(count - 1, temp2);
    }

    public String getMaxKey() {
        var maxTable = !table.isEmpty() ? table.lastEntry() : null;
        if (maxTable == null) {
            return "";
        }
        return maxTable.getValue().entrySet().iterator().next().getKey();

    }

    public String getMinKey() {
        var minTable = !table.isEmpty() ? table.firstEntry() : null;
        if (minTable == null) {
            return "";
        }
        return minTable.getValue().entrySet().iterator().next().getKey();
    }
}

/**
 * Your AllOne object will be instantiated and called as such:
 * AllOne obj = new AllOne();
 * obj.inc(key);
 * obj.dec(key);
 * String param_3 = obj.getMaxKey();
 * String param_4 = obj.getMinKey();
 */
