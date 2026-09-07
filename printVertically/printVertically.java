package printVertically;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://leetcode.cn/problems/print-words-vertically/">1424. 按列打印单词</a>
 *
 * <p>把字符串 s 按空格拆成单词后逐个竖排(第 i 个单词占第 i 行),然后按列从左到右读出;
 * 每列末尾的空格要去掉,单词之间位置的空格保留为分隔。返回每列构成的字符串列表。</p>
 *
 * <p>解法:直接模拟。先求最长单词长度 maxCount(总列数),对每一列拼出所有单词在该列的字符
 * (越界的单词补空格),再去掉行尾空格。</p>
 *
 * <p>复杂度:时间 O(maxCount × 单词数 + |s|),空间 O(|s|)。</p>
 */
class Solution {
    public List<String> printVertically(String s) {
        String[] words=s.split(" ");

        List<String> list=new ArrayList<>();
        int maxCount=0;
        for(String word:words){
            maxCount=Math.max(maxCount,word.length()); // 总列数 = 最长单词长度
        }
        int index=0;

        while(index<maxCount){
            StringBuilder sb=new StringBuilder();

            for(String word:words){

                if(index>=word.length()){
                    sb.append(" "); // 该单词已用尽,此列位置补空格
                }else {
                    sb.append(word.charAt(index));
                }
            }
            // 去掉本列行尾空格(不会全为空:index < maxCount 保证至少一个单词贡献字符)
            while (sb.charAt(sb.length()-1)==' '){
                sb.deleteCharAt(sb.length()-1);
            }
            list.add(sb.toString());
            index++;
        }
        return list;
    }
}