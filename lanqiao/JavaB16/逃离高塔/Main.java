package lanqiao.JavaB16.逃离高塔;

public class Main {

    public static void main(String[] args){
        int ans=0;
        for(long i=1;i<2025;i++){
            if(i*i*i%10==3)ans++;
        }
        System.out.println(ans);
    }
}
