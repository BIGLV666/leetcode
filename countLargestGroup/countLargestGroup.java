package countLargestGroup;

import java.util.TreeMap;

class Solution {


    public int countLargestGroup(int n) {
        TreeMap<Integer,Integer>table=new TreeMap<>();
        for(int i=1;i<=n;i++){
            int sum=get(i);
            table.put(sum,table.getOrDefault(sum,0)+1);
        }
        int max=0;
        for(var map:table.entrySet()){
            max=Math.max(max,map.getValue());
        }
        int count=0;
        for(var map:table.entrySet())if(map.getValue()==max)count++;
        return count;
    }
    private int get(int n){
        int ans=0;
        while(n>0){
            ans+=n%10;
            n/=10;
        }
        return ans;
    }
}
