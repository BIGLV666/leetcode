package Hot100.generateParenthesis;

import java.util.ArrayList;
import java.util.List;

class Solution {

    private static StringBuilder sb;
    private static List<String>ans;
    private  int n;
    public List<String> generateParenthesis(int n) {
        this.n=n;
        ans=new ArrayList<>();
        sb=new StringBuilder();
        dfs(n,n);
        return ans;
    }
    private void dfs(int left,int right){
        if(sb.length()==2*n) {
            ans.add(sb.toString());
            return;
        }
        if(left==right&&left==0)return;

        if(left>0){
            sb.append('(');
            dfs(left-1,right);
            sb.deleteCharAt(sb.length()-1);
        }
        if (left<right) {
            sb.append(')');
            dfs(left,right-1);
            sb.deleteCharAt(sb.length()-1);
        }

    }
}
