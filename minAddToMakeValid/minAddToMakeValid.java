package minAddToMakeValid;

/**
 * LeetCode 921. 使括号有效的最少添加(Medium)
 *
 * 一遍贪心计数:cnt 记录尚未配对的 '(' 数量;遇到 ')' 时若 cnt>0 则消耗一个,
 * 否则这个 ')' 永远无法配对,需要添加一个 '(',计入 ans。
 * 扫描结束后还剩 cnt 个落单的 '(' 需要添加 ')',答案为 ans + cnt。
 */
class Solution {
    public int minAddToMakeValid(String s) {
        int ans=0;
        int cnt=0;
        for(char c:s.toCharArray()){
            if(c=='('){
                cnt++;
            }else {
                if(cnt>0){
                    cnt--;
                }else {
                    ans++;
                }
            }
        }
        return ans+cnt;
    }
}
