package Trie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * 字典树（前缀树）：LeetCode 208 的基础三件套 + 常用扩展
 *
 * 结构：把单词拆成字符，每个字符走一条边，节点代表"从根走到它的那条路径字符串"。
 *       root 代表空串；节点上挂两个计数：
 *         · count     —— 有几个单词正好在这里结束（>0 就说明它是一个完整的单词）
 *         · prefixCnt —— 有几个单词"经过"这里，即有多少单词以该节点代表的字符串为前缀
 *
 * 复杂度（m = 单词长度）：insert / search / startsWith / delete /
 *       countWordsEqualTo / countWordsStartingWith 都是 O(m)；
 *       getAllWords O(节点数)；longestCommonPrefix O(公共前缀长度)；空间 O(总字符数)
 *
 * 字符集：只支持小写 a~z（int[26] 的孩子表最省内存也最快）。
 *         如果出现大写/数字/中文，把 next 换成 HashMap<Character, TrieNode>，
 *         并把所有 `ch - 'a'` 改成 `ch` 当 key。
 *
 * equals 的两层语义：
 *       · Trie.equals     比"内容"：两个树存着完全相同的单词多重集合就算相等（与插入顺序无关）
 *       · TrieNode.equals 比"结构"：子树完全相同才算相等（count、prefixCnt、26 个孩子递归比较）
 *       本实现的删除会剪枝、不留死节点，所以这两种语义在正常情况下是一致的。
 */
class Trie {

    /**
     * 树上的一个节点 = 一个前缀字符串（根节点代表空串 ""）。
     * 注意：节点本身不存字符，字符是"父 → 子"这条边上的信息。
     */
    static class TrieNode {
        /** 26 个小写字母的孩子指针，null = 没有这条边 */
        TrieNode[] next = new TrieNode[26];

        /**
         * 有几个单词正好在这个节点结束。
         * 用计数而不是 boolean end：天然支持"同一个词插入多次"，
         * 而且 count > 0 ⟺ 它是单词结尾，只留一处状态就不会出现
         * "count 减了但 end 忘了清"这种不一致。
         */
        int count;

        /** 有多少个单词经过这个节点（即以它的字符串为前缀的单词数，含它自身是单词的情况） */
        int prefixCnt;

        /** 这个节点是不是某个单词的结尾 */
        boolean isEnd() {
            return count > 0;
        }

        /**
         * 结构相等：本节点的两个计数相同，并且 26 个孩子一一对应地递归相等。
         * Arrays.equals 对元素调用的就是本方法，所以这一句就是完整的深度比较。
         * 这是给调试/校验用的：可以检查两棵树是不是长得一模一样。
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof TrieNode)) {
                return false;
            }
            TrieNode other = (TrieNode) o;
            return count == other.count
                    && prefixCnt == other.prefixCnt
                    && Arrays.equals(next, other.next);
        }

        /** 与 equals 保持一致：把整棵子树的计数和结构都算进哈希 */
        @Override
        public int hashCode() {
            return Objects.hash(count, prefixCnt, Arrays.hashCode(next));
        }

        /**
         * 打印这个节点以及它下面能拼出的单词（相对路径；节点自身是单词结尾时记作 ""）。
         * 例：{"a","ab"} 的根节点 → TrieNode{count=0, prefixCnt=2, subWords=[a, ab]}
         *     'a' 那个节点      → TrieNode{count=1, prefixCnt=2, subWords=["", b]}
         */
        @Override
        public String toString() {
            return "TrieNode{count=" + count
                    + ", prefixCnt=" + prefixCnt
                    + ", subWords=" + collectFrom(this) + "}";
        }
    }

    private final TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    /* ==================== 基础三件套 ==================== */

    /**
     * 插入一个单词（同一个词插入多次会记成多份，见 count）。
     * 沿途把每个节点的 prefixCnt 加 1 —— 这些节点代表的字符串都是 word 的前缀，
     * 以后 countWordsStartingWith 只要走到对应节点读 prefixCnt 就知道了。
     */
    public void insert(String word) {
        TrieNode node = root;
        node.prefixCnt++;                        // root.prefixCnt == 单词总数
        for (int i = 0; i < word.length(); i++) {
            int idx = word.charAt(i) - 'a';
            if (node.next[idx] == null) {
                node.next[idx] = new TrieNode(); // 这条路还没人走过，建新节点
            }
            node = node.next[idx];
            node.prefixCnt++;
        }
        node.count++;                            // 在终点节点记一笔"这里是一个单词"
    }

    /**
     * 查询 word 是否在树里（必须是完整单词，不能只是别人的前缀）。
     * 走不到 → false；走到了但该节点不是任何单词的结尾
     * （比如树里只有 "apple"，查 "app"）→ 也 false。
     */
    public boolean search(String word) {
        TrieNode node = walk(word);
        return node != null && node.isEnd();
    }

    /**
     * 是否存在以 prefix 开头的单词。
     * 和 search 的唯一区别：不需要节点是单词结尾，只要"这条路走得通、下面还确实有单词"。
     * prefixCnt > 0 顺带把空前缀也处理对了：startsWith("") 等价于 !isEmpty()。
     */
    public boolean startsWith(String prefix) {
        TrieNode node = walk(prefix);
        return node != null && node.prefixCnt > 0;
    }

    /* ==================== 删除 ==================== */

    /**
     * 删除一个单词（只删一份；插入过多次的话其余份还在）。
     * 返回 true 表示确实删掉了一份，false 表示树里本来就没有这个词。
     *
     * 两个要点：
     *   ① 计数更新必须落在"word 的终点节点"上，而不是它的父节点。
     *   ② 删完要剪掉变成垃圾的节点（否则结构比较会失真、内存也白占），
     *      判断标准是"没有单词经过它"（prefixCnt == 0），这时整棵子树都没用了。
     */
    public boolean delete(String word) {
        if (erase(root, word, 0)) {
            root.prefixCnt--;                    // root 的 prefixCnt 就是总单词数
            return true;
        }
        return false;
    }

    /**
     * @param node 当前节点，代表 word[0..i-1]
     * @param i    下一个要走的字符下标；i == word.length() 时 node 就是 word 的终点节点
     * @return 是否成功删掉了一份
     */
    private boolean erase(TrieNode node, String word, int i) {
        if (i == word.length()) {
            if (node.count == 0) {
                return false;                    // 这个词根本不在树里，什么都不改
            }
            node.count--;                        // 只减一份；isEnd() 随 count 一起变化
            return true;
        }
        int idx = word.charAt(i) - 'a';
        TrieNode child = node.next[idx];
        if (child == null || !erase(child, word, i + 1)) {
            return false;                        // 路径不存在 / 本来就没这个词
        }
        child.prefixCnt--;                       // 回溯时沿路更新
        if (child.prefixCnt == 0) {
            node.next[idx] = null;               // 这棵子树已经没有单词了，整棵摘掉
        }
        return true;
    }

    /* ==================== 常用扩展 ==================== */

    /** 有多少个单词正好等于 word（重复插入会累加） */
    public int countWordsEqualTo(String word) {
        TrieNode node = walk(word);
        return node == null ? 0 : node.count;
    }

    /** 有多少个单词以 prefix 开头（重复插入会累加）—— 靠 prefixCnt，O(m) */
    public int countWordsStartingWith(String prefix) {
        TrieNode node = walk(prefix);
        return node == null ? 0 : node.prefixCnt;
    }

    /** 树里一共有多少个单词（含重复插入的份数） */
    public int size() {
        return root.prefixCnt;
    }

    /** 树里有没有单词 */
    public boolean isEmpty() {
        return root.prefixCnt == 0;
    }

    /**
     * 取出所有单词，按字典序返回（重复插入的只出现一次）。
     * 字典序是"先序遍历 + 孩子按 a~z 顺序"自然得到的。
     */
    public List<String> getAllWords() {
        List<String> res = new ArrayList<>();
        collectSorted(root, new StringBuilder(), res);
        return res;
    }

    private static void collectSorted(TrieNode node, StringBuilder path, List<String> res) {
        if (node.isEnd()) {
            res.add(path.toString());
        }
        for (int c = 0; c < 26; c++) {
            if (node.next[c] != null) {
                path.append((char) ('a' + c));
                collectSorted(node.next[c], path, res);
                path.deleteCharAt(path.length() - 1);   // 回溯，撤销这一层
            }
        }
    }

        /** 和 collectSorted 一样，但重复插入的词按份数重复列出（给 toString 用） */
        private static void collectWithCount(TrieNode node, StringBuilder path, List<String> res) {
            if (node.isEnd()) {
                String word = path.toString();
                for (int i = 0; i < node.count; i++) {
                    res.add(word);
                }
            }
            for (int c = 0; c < 26; c++) {
                if (node.next[c] != null) {
                    path.append((char) ('a' + c));
                    collectWithCount(node.next[c], path, res);
                    path.deleteCharAt(path.length() - 1);
                }
            }
        }

    /**
     * 所有单词的最长公共前缀。
     * 从根往下走，只要"当前节点不是单词结尾，且只有一个孩子"就继续延伸；
     * 一旦分叉、或当前节点本身就是一个单词的结尾（再延伸就不是公共前缀了）就停。
     */
    public String longestCommonPrefix() {
        StringBuilder sb = new StringBuilder();
        TrieNode node = root;
        while (node.count == 0) {
            int only = -1;
            int childCount = 0;
            for (int c = 0; c < 26; c++) {
                if (node.next[c] != null) {
                    childCount++;
                    only = c;
                }
            }
            if (childCount != 1) {
                break;                           // 分叉了或者没有了
            }
            sb.append((char) ('a' + only));
            node = node.next[only];
        }
        return sb.toString();
    }

    /* ==================== equals / hashCode / toString ==================== */

    /**
     * 内容相等：两个 Trie 存着完全相同的单词多重集合就算相等。
     * 只比较"单词 → 份数"这本账，所以与插入顺序无关，插过又删掉的词也不计入。
     * 例：先插 "ab" 再插 "a" 的树，和顺序反过来的树相等。
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trie)) {
            return false;
        }
        return wordCounts().equals(((Trie) o).wordCounts());
    }

    /** 与 equals 保持一致：用"单词 → 份数"这本账算哈希 */
    @Override
    public int hashCode() {
        return wordCounts().hashCode();
    }

    /**
     * 打印整棵树的内容：单词总数 + 按字典序列出的单词（重复插入的词按份数重复列出）。
     * 例：{"a","ab","ab"} → Trie{size=3, words=[a, ab, ab]}
     */
    @Override
    public String toString() {
        List<String> words = new ArrayList<>();
            collectWithCount(root, new StringBuilder(), words);
        StringBuilder sb = new StringBuilder("Trie{size=").append(root.prefixCnt).append(", words=[");
        for (int i = 0; i < words.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(words.get(i));
        }
        return sb.append("]}").toString();
    }

    /* ==================== 内部工具 ==================== */

    /** 沿 word 走到底，返回终点节点；中途走不通就返回 null */
    private TrieNode walk(String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            node = node.next[word.charAt(i) - 'a'];
            if (node == null) {
                return null;
            }
        }
        return node;
    }

    /** "单词 → 份数"这本账，equals / hashCode 就靠它 */
    private Map<String, Integer> wordCounts() {
        Map<String, Integer> map = new TreeMap<>();
        countInto(root, new StringBuilder(), map);
        return map;
    }

    private static void countInto(TrieNode node, StringBuilder path, Map<String, Integer> map) {
        if (node.isEnd()) {
            map.merge(path.toString(), node.count, Integer::sum);
        }
        for (int c = 0; c < 26; c++) {
            if (node.next[c] != null) {
                path.append((char) ('a' + c));
                countInto(node.next[c], path, map);
                path.deleteCharAt(path.length() - 1);
            }
        }
    }

    /**
     * 从某个节点出发，收集它下面能拼出的单词（相对路径；节点自身是单词结尾时记作 ""）。
     * 给 TrieNode.toString 用。
     */
    private static List<String> collectFrom(TrieNode node) {
        List<String> res = new ArrayList<>();
        collectRelative(node, new StringBuilder(), res);
        return res;
    }

    private static void collectRelative(TrieNode node, StringBuilder path, List<String> res) {
        if (node.isEnd()) {
            res.add(path.toString());
        }
        for (int c = 0; c < 26; c++) {
            if (node.next[c] != null) {
                path.append((char) ('a' + c));
                collectRelative(node.next[c], path, res);
                path.deleteCharAt(path.length() - 1);
            }
        }
    }
}