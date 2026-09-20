package TreeAncestor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TreeAncestor {


    private Map<Integer, List<Integer>> table;

    public TreeAncestor(int n, int[] parent) {
        table = new HashMap<>();
        for(int i=0;i<parent.length;i++) {
            int temp=parent[i];
            var list= table.getOrDefault(temp,new ArrayList<>());
            list=new ArrayList<>(list);
            list.add(i);
            table.put(i,list);
        }
        //map.remove(0);
    }

    public int getKthAncestor(int node, int k) {
        var  l=table.get(node);
        if(l==null) return -1;
        if(k>=l.size()) return -1;
        return l.get(l.size()-k);
    }
}

/**
 * Your TreeAncestor object will be instantiated and called as such:
 * TreeAncestor obj = new TreeAncestor(n, parent);
 * int param_1 = obj.getKthAncestor(node,k);
 */
