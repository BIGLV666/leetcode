#include<bits/stdc++.h>
using namespace std;

class Solution {
public:
    int findLHS(vector<int>& nums) {
        map<int,int>table;
        for(int x : nums){
            table[x]++;
        }
        int res=0;
        for(auto x : table){
            if(table.count(x.first+1)){
                res=max(res,x.second+table[x.first+1]);
            }
            if(table.count(x.first-1)){
                res=max(res,x.second+table[x.first-1]);
            }
        }
        return res;
    }
};