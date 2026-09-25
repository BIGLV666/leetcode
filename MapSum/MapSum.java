package MapSum;

import java.util.HashMap;
import java.util.Map;

class MapSum {
    static class Node{
        Map<Character,Node> map=new HashMap<>();
        int val=0;
        Node(){}
    }
    private final Node root;
    private Map<String,Integer> map;
    public MapSum() {
        root = new Node();
        map=new HashMap<>();
    }

    public void insert(String key, int val) {
        var node=root;
        int d=val-map.getOrDefault(key,0);
        map.put(key,val);

       for(char c : key.toCharArray()){
           if(!node.map.containsKey(c)){
               node.map.put(c,new Node());
           }
           node=node.map.get(c);
           node.val+=d;
       }
    }

    public int sum(String prefix) {
       var node=root;
       for(char c : prefix.toCharArray()){
           if(!node.map.containsKey(c)){
               return 0;
           }
           node=node.map.get(c);
       }
        return node.val;
    }
}

/**
 * Your MapSum object will be instantiated and called as such:
 * MapSum obj = new MapSum();
 * obj.insert(key,val);
 * int param_2 = obj.sum(prefix);
 */
