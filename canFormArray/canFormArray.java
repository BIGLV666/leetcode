package canFormArray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Solution {
    public boolean canFormArray(int[] arr, int[][] pieces) {
        Map<Integer,int[]>table=new HashMap<>();
        Arrays.stream(pieces).forEach(a->table.put(a[0],a));
        int i=0;
        while(i<arr.length){
            int num=arr[i];
            if(!table.containsKey(num))return false;
            for(int a:table.get(num)){
                if(a!=arr[i])return false;
                i++;
            }
        }
        return true;
    }
}
