package minInsertions;

/**
 * LeetCode 1541. 平衡括号字符串的最少插入次数(Medium)
 *
 * <p>本题中一个 '(' 需要两个连续的 ')' 才能配平。一遍扫描:cnt 记录待配平的 '(' 个数。
 * 遇 '(' 则 cnt++;遇 ')' 时,若能凑出「两个连续 ')'」就消耗一个 '('(cnt--),否则补
 * 一个 ')' 或一个 '(' 并把本次 ')' 记账(ans++)。扫描结束后每个落单的 '(' 各还需一个
 * ')',故答案为 ans + cnt * 2。</p>
 *
 * <p>时间复杂度 O(n),空间 O(1)。</p>
 */
class Solution {
    public int minInsertions(String s) {
        int ans=0;
        int cnt=0;
        int index=0;
        while(index<s.length()){
            char ch=s.charAt(index);
            if(ch=='('){
                cnt++;
                index++;
            }else {
                if(cnt>0){
                    cnt--;
                }else {
                    ans++;
                }
                if(index< s.length()-1&&s.charAt(index+1)==')'){
                    index+=2;
                }else {
                    ans++;
                    index++;
                }
            }
        }
        return ans+cnt*2;
    }
}
