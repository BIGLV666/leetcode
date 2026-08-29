package powerfulIntegers;
import java.util.*;
import java.util.List;

public class Solution {
     public List<Integer> powerfulIntegers(int x, int y, int bound) {
        Set<Integer>res=new HashSet<>();
        for(int i=0;i<21;i++){
            if(Math.pow(x,i)>bound){
                break;
            }
            for(int j=0;j<21;j++){
                if(Math.pow(x,i)+Math.pow(y,j)<=bound){
                    res.add((int)(Math.pow(x,i)+Math.pow(y,j)));
                }
                else{
                    break;
                }
            }
        }
        return new ArrayList<>(res);      
    }
}
