package lanqiao.神奇算式;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public  static void main(String[] args) {
        int ans=0;
        for(int i=1;i<1000;i++){
            for(int j=1;j<1000;j++){
                if(i*j>=10000){
                    break;
                }
                if(i*j<1000)continue;
                ans+=check(i,j);
            }
        }
        System.out.println(ans/2);
    }
    static int check(int a,int b){

        int p = a * b;
        if (p < 1000 || p >= 10000) return 0;

        // 关键：两个因数一共必须正好 4 位
        if (digits(a) + digits(b) != 4) return 0;

        Set<Integer> set = new HashSet<>();
        add(a, set);
        add(b, set);
        if (set.size() != 4) return 0;

        Set<Integer> set1 = new HashSet<>();
        add(p, set1);

        return set1.equals(set) ? 1 : 0;
    }

    static int digits(int x) {
        int cnt = 0;
        while (x > 0) {
            cnt++;
            x /= 10;
        }
        return cnt;
    }

    static void add(int a,Set<Integer> set){
        while(a>0){
            set.add(a%10);
            a=a/10;
        }
    }
}
