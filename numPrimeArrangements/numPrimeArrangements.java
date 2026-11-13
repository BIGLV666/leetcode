package numPrimeArrangements;

class Solution {
    public int numPrimeArrangements(int n) {
        int ans=1;
        int mod=(int)1e9+7;
        int count=0;
        for(int i=2;i<=n;i++){
            if(isPrime(i)){
                count++;
            }
        }
        return (int)(factorial(count,mod)*factorial(n-count,mod)%mod);
    }


    private boolean isPrime(int n){
        for(int i=2;i*i<=n;i++){
            if(n%i==0)return false;
        }
        return true;
    }

    public long factorial(int x,int mod) {
        long ret = 1;
        for (int i = 1; i <= x; ++i) {
            ret *= i;
            ret %= mod;
        }
        return ret;
    }

}
