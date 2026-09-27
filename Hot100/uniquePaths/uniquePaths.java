package Hot100.uniquePaths;
/**超时*/
class Solution {

    private int ans;
    public int uniquePaths(int m, int n) {
        ans=0;
        dfs(m-1,n-1);
        return ans;
    }

    private void dfs(int m,int n){
        if(m<=0&&n<=0){
            ans++;
            return;
        }
        if(m>0){
            dfs(m-1,n);
        }
        if(n>0){
            dfs(m,n-1);
        }

    }

}
