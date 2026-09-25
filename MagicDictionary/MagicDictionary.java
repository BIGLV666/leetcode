package MagicDictionary;

import java.util.HashMap;
import java.util.Map;

class MagicDictionary {


    static class TrieNode {
        boolean isEnd=false;
        Map<Character, TrieNode> map=new HashMap<>();
    }
    private TrieNode root;

    public MagicDictionary() {
        root=new TrieNode();
    }

    public void buildDict(String[] dictionary) {
        for(String word:dictionary){
            add(word);
        }
    }

    public boolean search(String searchWord) {
        return search(searchWord,root,0,false);
    }


    private boolean search(String searchWord, TrieNode node, int index , boolean diff){
        if(index==searchWord.length()){
            return diff&&node.isEnd;
        }
        char c=searchWord.charAt(index);
        if(node.map.containsKey(c)){
            if(search(searchWord,node.map.get(c),index+1,diff)){
                return true;
            }
        }
        if(!diff){
            for(Map.Entry<Character, TrieNode> entry:node.map.entrySet()){
                if(entry.getKey()!=c){
                    if(search(searchWord,entry.getValue(),index+1,true)){
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private void add(String s){
        var node=root;
        for(char c:s.toCharArray()){
            if(!node.map.containsKey(c)){
                node.map.put(c,new TrieNode());
            }
            node=node.map.get(c);
        }
        node.isEnd=true;
    }


}

/**
 * Your MagicDictionary object will be instantiated and called as such:
 * MagicDictionary obj = new MagicDictionary();
 * obj.buildDict(dictionary);
 * boolean param_2 = obj.search(searchWord);
 */
