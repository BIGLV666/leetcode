package suggestedProducts;

import java.util.*;

class Solution {
    public List<List<String>> suggestedProducts(String[] products, String searchWord) {
        TrieNode trie = new TrieNode();
        trie.insertAll(products);
        List<List<String>> ans = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < searchWord.length(); i++) {
            sb.append(searchWord.charAt(i));
            ans.add(
                    trie.search(searchWord.charAt(i))
                            .stream()
                            .map(s -> sb + s)
                            .sorted()
                            .limit(3)
                            .toList()
            );

        }
        return ans;
    }
}


class TrieNode {
    static class Node {
        Node[] children;
        boolean isEnd;

        public Node() {
            children = new Node[26];
            isEnd = false;
        }
    }

    private final Node root;
    private Node lastNode;

    public TrieNode() {
        root = new Node();
        lastNode = root;
    }

    public void insert(String word) {
        Node node = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = getIndex(c);
            if (node.children[index] == null) {
                node.children[index] = new Node();
            }
            node = node.children[index];
        }
        node.isEnd = true;
    }

    public void insertAll(String[] words) {
        for (String word : words) {
            insert(word);
        }
    }

    public List<String> search(char c) {
        if (lastNode == null) {
            return List.of();
        }
        int index = getIndex(c);
        if (lastNode.children[index] == null) {
            lastNode = null;
            return List.of();
        }
        lastNode = lastNode.children[index];
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        dfs(lastNode, sb, result);
        return result;
    }

    private void dfs(Node node, StringBuilder sb, List<String> result) {
        if (node.isEnd) {
            result.add(sb.toString());
        }
        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) {
                sb.append((char) ('a' + i));
                dfs(node.children[i], sb, result);
                sb.deleteCharAt(sb.length() - 1);
            }
        }
    }

    private int getIndex(char c) {
        return c - 'a';
    }
}
