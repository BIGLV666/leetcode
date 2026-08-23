#include <vector>
#include <list>
#include <set>
using namespace std;


/**
 * @brief 967. 相邻元素差为 K 的数字
 * @param n 数字的长度
 * @param k 相邻元素的差值
 * @return vector<int> 所有符合条件的数字
 * @link{https://leetcode.cn/problems/numbers-with-same-consecutive-differences/description/}
 * @note 本题使用广度优先搜索（BFS）来解决，从1到9开始，每次生成一个新数字，判断是否符合条件。
 * 如果符合条件，将其加入结果集合中。最后，将集合转换为列表返回。
 * @time O(9 * 2^N)
 */

class Solution {
public:

    int getNum(int num,int n){
        int sum=0;
        while(num>0){
            sum++;
            num/=10;
        }
        return sum==n;
    }

    vector<int> numsSameConsecDiff(int n, int k) {
        vector<int> res;
        set<int> s;
        for(int i=1;i<10;i++){
            list<int> tmp={i};
            for(int j=0;j<n-1;j++){
                int len=tmp.size();
                while(len>0){
                        int num=tmp.front();
                        tmp.pop_front();
                        int last=num%10;
                        if(last+k<10){
                            tmp.push_back(num*10+last+k);
                        }
                        if(last-k>=0){
                            tmp.push_back(num*10+last-k);
                        }
                        len--;
                    }
                }
                for(auto num:tmp){
                    if(getNum(num,n)){
                        s.insert(num);
                    }
                }
                tmp.clear();
            
        }
        res.assign(s.begin(),s.end());
        return res;
    }
};