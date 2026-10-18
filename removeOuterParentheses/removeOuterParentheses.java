package removeOuterParentheses;

/**
 * LeetCode 1021. 删除最外层的括号(Easy)
 *
 * <p>按「原语」切分:用临时串收集当前原语,当左右括号计数再次相等且非空时说明一段
 * 原语结束,去掉首尾括号后拼进答案。等价写法是「深度 > 0 才输出」。</p>
 *
 * <p>时间复杂度 O(n),空间 O(n)(答案本身)。</p>
 */
class Solution {
    public String removeOuterParentheses(String s) {
        StringBuilder sb = new StringBuilder();
        StringBuilder ans=new StringBuilder();
        int l=0,r=0;
        for(char c:s.toCharArray()){

            if(l==r&& !sb.isEmpty()){
                l=0;
                r=0;
                ans.append(sb, 1, sb.length()-1);
                sb.delete(0, sb.length());
            }
            if(c=='('){
                l++;
                sb.append('(');
            }else  if(c==')'){
                sb.append(')');
                r++;
            }
        }
        if(l==r&& !sb.isEmpty()){
            ans.append(sb, 1, sb.length()-1);
        }
        return ans.toString();
    }
}
