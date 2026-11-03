package StreamOfCharacters;

import java.util.ArrayDeque;
import java.util.Map;

class StreamChecker {
    private final TrieNode trie;
    ArrayDeque<Character> stream;
    private final int maxLen;

    public StreamChecker(String[] words) {
        trie = new TrieNode();
        maxLen = trie.addReverseAllAndGetMaxLen(words);
        stream = new ArrayDeque<>(maxLen);
    }

    public boolean query(char letter) {
        stream.addFirst(letter);
        if(stream.size() > maxLen) {
            stream.pollLast();
        }
        return trie.search(stream);
    }
}


class TrieNode{
    static class Node{
        boolean isEnd;
        Map<Character, Node> children;
        public Node(){
            this.isEnd = false;
            this.children = new java.util.HashMap<>();
        }
    }


    private final Node root;
     public TrieNode(){
        root = new Node();
    }

    public void addWord(String word){
        Node node = root;
        for(char c: word.toCharArray()){
            if(!node.children.containsKey(c)){
                node.children.put(c, new Node());
            }
            node = node.children.get(c);
        }
        node.isEnd = true;
    }


    public int addReverseAllAndGetMaxLen(String[] words){
        int maxLen = 0;
        for(String word: words){
            addWord(new StringBuilder(word).reverse().toString());
            maxLen = Math.max(maxLen, word.length());
        }
        return maxLen;
    }

    public boolean search(ArrayDeque<Character> stream) {
        Node node = root;
        for(char c: stream){
            if(!node.children.containsKey(c)){
                return false;
            }
            node = node.children.get(c);
            if(node.isEnd)return true;
        }
        return node.isEnd;
    }

    public boolean search(String word){
        Node node = root;
        for(char c: word.toCharArray()){
            if(!node.children.containsKey(c)){
                return false;
            }
            node = node.children.get(c);
        }
        return node.isEnd;
    }


}