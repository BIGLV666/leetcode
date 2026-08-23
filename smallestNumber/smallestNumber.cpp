
using namespace std;

class Solution {
public:
    int getDigit(int n) {
        int res=1;
        while (n > 0) {
            res*=n%10;
            n /= 10;
        }
        return res; 
    }

    int smallestNumber(int n, int t) {
        for(int i=n;;i++){
            if(getDigit(i)%t==0){
                return i;
            }
        }
    }
};