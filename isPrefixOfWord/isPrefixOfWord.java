package isPrefixOfWord;

/**
 * <a href="https://leetcode.cn/problems/check-if-a-word-occurs-as-a-prefix-of-any-word-in-a-sentence/">1455. 检查单词是否为句中其他单词的前缀</a>
 *
 * <p>给定句子 sentence(小写单词 + 单空格分隔)和查询单词 searchWord,
 * 返回句子中<strong>第一个</strong>以 searchWord 为前缀的单词的下标(1 开始);
 * 不存在返回 -1。</p>
 *
 * <p>解法:按空格切词逐个比对。前缀检查就是逐字符比较前 searchWord.length() 位
 * (等价于 {@code word.startsWith(searchWord)},此处手写展开)。</p>
 *
 * <p>复杂度:时间 O(|sentence|),空间 O(|sentence|)(切词数组)。</p>
 */
class Solution {
    public int isPrefixOfWord(String sentence, String searchWord) {
        String[] words = sentence.split(" ");
        for(int i=0;i<words.length;i++){
            if(isPrefix(words[i],searchWord)){
                return i+1; // 题目要求 1 开始的下标,首个命中直接返回
            }
        }
        return -1;
    }


    /** 判断 searchWord 是否为 word 的前缀:长度不够或任一位不同即失败。 */
    public boolean isPrefix(String sentence, String searchWord) {
        int l1=sentence.length();
        int l2=searchWord.length();
        if(l2>l1){
            return false; // 查询词比单词还长,不可能是前缀
        }
        for(int i=0;i<l2;i++){
            if(sentence.charAt(i)!=searchWord.charAt(i)){
                return false;
            }
        }
        return true;
    }
}
