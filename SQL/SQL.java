package SQL;
import java.util.*;
import java.util.TreeMap;
class SQL {
    private Map<String, Integer> nameAndColumns;
    private Map<String, Integer> indexMap;
    private Map<String, Map<Integer, List<String>>> database;
    public SQL(List<String> names, List<Integer> columns) {
        nameAndColumns = new HashMap<>();
        database = new HashMap<>();
        for (int i = 0; i < names.size(); i++) {
            nameAndColumns.put(names.get(i), columns.get(i));
            database.put(names.get(i), new HashMap<>());
        }
        indexMap = new HashMap<>();
    }

    public boolean ins(String name, List<String> row) {
        if (!database.containsKey(name))
            return false;
        if (row.size() != nameAndColumns.get(name))
            return false;
        int index = indexMap.getOrDefault(name, 1);
        indexMap.put(name, indexMap.getOrDefault(name, 1) + 1);
        var data = database.get(name);
        data.put(index, row);
        database.put(name, data);
        return true;
    }

    public void rmv(String name, int rowId) {
        if (!database.containsKey(name))
            return;
        database.get(name).remove(rowId);
    }

    public String sel(String name, int rowId, int columnId) {
        if (!database.containsKey(name))
            return "<null>";
        var data = database.get(name);
        var list = data.get(rowId);
        if (list == null || list.isEmpty())
            return "<null>";
        if (columnId > nameAndColumns.get(name))
            return "<null>";
        // System.out.println(data);
        return list.get(columnId - 1);
    }

    public List<String> exp(String name) {
        var data = database.getOrDefault(name, new HashMap<>());
        if (data.isEmpty())
            return List.of();
        List<String> ans = new ArrayList<>();
        // exp 要求按 rowId 升序返回;HashMap 的迭代顺序不保证升序,这里用 TreeMap 固定顺序
        for (var entry : new TreeMap<>(data).entrySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(entry.getKey());
            for (int i = 0; i < entry.getValue().size(); i++) {
                sb.append(',');
                sb.append(entry.getValue().get(i));
            }
            ans.add(sb.toString());
        }
        return ans;
    }
}

/**
 * Your SQL object will be instantiated and called as such:
 * SQL obj = new SQL(names, columns);
 * boolean param_1 = obj.ins(name,row);
 * obj.rmv(name,rowId);
 * String param_3 = obj.sel(name,rowId,columnId);
 * List<String> param_4 = obj.exp(name);
 */
