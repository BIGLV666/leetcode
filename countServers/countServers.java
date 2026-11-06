package countServers;


import java.util.Arrays;
import java.util.Comparator;

class Solution {
    public int[] countServers(int n, int[][] logs, int x, int[] queries) {
        int[] result=new int[queries.length];
        int []cnt=new int[n+1];
        Arrays.sort(logs, Comparator.comparingInt(a -> a[1]));
        Integer[] ids=new Integer[queries.length];
        for(int i=0;i<ids.length;i++)ids[i]=i;
        Arrays.sort(ids,Comparator.comparingInt(value ->queries[value]));
        int l=0,r=0,count=0;
        for(int id:ids){
            int time=queries[id];
            while(r<logs.length&&logs[r][1]<=time){
                if(cnt[logs[r++][0]]++==0){
                    count++;
                }
            }
            while(l<r&&logs[l][1]<time-x){
                if(--cnt[logs[l++][0]]==0){
                    count--;
                }
            }
            result[id]=n-count;
        }
        return result;
    }
}


