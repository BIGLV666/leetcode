package WordFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

class WordFilter {

    static class TrieNode{
        int index;
        boolean isEnd=false;
        Map<Character,TrieNode> map=new HashMap<>();
    }
    private TrieNode prefRoot;
    private TrieNode suffRoot;

    public WordFilter(String[] words) {
        prefRoot=new TrieNode();
        suffRoot=new TrieNode();

        int i=0;
        for(String str:words){
            insertPre(str,i);
            insertSuff(str,i);
            i++;
        }
    }


    public int f(String pref, String suff) {

        TreeSet<Integer>set=new TreeSet<>();
        var node=prefRoot;
        for(char c:pref.toCharArray()){
            if(!node.map.containsKey(c)){
                return -1;
            }
            node=node.map.get(c);
        }
        dfs(node,set);
        var node1=suffRoot;
        for(int i=suff.length()-1;i>=0;i--){
            if(!node1.map.containsKey(suff.charAt(i))){
                return -1;
            }
            node1=node1.map.get(suff.charAt(i));
        }
        return dfs2(node1,set);

    }

    private void dfs(TrieNode node, TreeSet<Integer> set) {
        if (node.isEnd) set.add(node.index);
        for (TrieNode child : node.map.values()) dfs(child, set);
    }

    private int dfs2(TrieNode node, Set<Integer> set) {
        int ans = -1;
        if (node.isEnd && set.contains(node.index)) ans = node.index;
        for (TrieNode child : node.map.values()) {
            ans = Math.max(ans, dfs2(child, set));
        }
        return ans;
    }


    private void insertPre(String s,int index) {
        var node=prefRoot;
        for (char c : s.toCharArray()) {
            if (!node.map.containsKey(c)) {
                node.map.put(c, new TrieNode());
            }
            node=node.map.get(c);
        }
        node.isEnd=true;
        node.index=index;
    }

    private void insertSuff(String s,int index) {
        var node=suffRoot;
        for(int i=s.length()-1;i>=0;i--){
            if (!node.map.containsKey(s.charAt(i))){
                node.map.put(s.charAt(i),new TrieNode());
            }
            node=node.map.get(s.charAt(i));
        }
        node.isEnd=true;
        node.index=index;
    }

}

/**
 * Your WordFilter object will be instantiated and called as such:
 * WordFilter obj = new WordFilter(words);
 * int param_1 = obj.f(pref,suff);
 */
