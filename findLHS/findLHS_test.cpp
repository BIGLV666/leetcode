#include<bits/stdc++.h>
using namespace std;
#include "findLHS.cpp"
struct Test{
    vector<int>nums;
    int expect;
};
int main(){
    Solution s;
    vector<Test>tests;
    tests.push_back({{1,3,2,2,5,2,3,7},5});
    tests.push_back({{1,2,3,4},2});
    tests.push_back({{1,1,1,1},0});
    for(auto x : tests){
        if(s.findLHS(x.nums)!=x.expect){
            cout<<"error"<<endl;
        }
    }
    return 0;
}