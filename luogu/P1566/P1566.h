//
// Created by biglv on 2026/10/7.
//
// 洛谷 P1566 加等式:多组数据,每组 n 个正整数,统计「加等式」的个数——
// 某个元素可以表示成集合内其他元素之和;3=1+2 与 3=2+1 视为同一个,方案按
// 「目标元素 + 一个子集」计,每个元素至多用一次。
//
// 实现:每组数据将数组降序排序后,对每个位置 i 从 i+1 起做子集和 DFS,
// 累计和恰等于 a[i] 时计数(只向后扩展,天然保证每个子集只被枚举一次)。
//
// 说明:main 直接写在头文件里,P1566.cpp 仅 include 本文件,因此无法用
// *_test.cpp include 复用;验证方式为编译 P1566.cpp 后按样例 stdin/stdout 运行。
//

#include<bits/stdc++.h>

using namespace std;

int ans,a[32],n;
bool cmp(const int a,const int b) {
    return a>b;
}
void dfs(int num,int sum,int index) {
    if (index>32)return;
    if (sum==num) {
        ans++;
        return;
    }
    for (int i=index+1;i<=n;i++)if(sum+a[i]<=num)dfs(num,sum+a[i],i);
    return;
}

int main() {
    ios::sync_with_stdio(false);
    int t;
    cin>>t;
    while (t--) {
        cin>>n;
        ans=0;
        for (int i=1;i<=n;i++) {
            cin>>a[i];
        }
        sort(a+1,a+n+1,cmp);
        for (int i=1;i<=n;i++)dfs(a[i],0,i);
        cout<<ans<<endl;
    }
    return 0;
}


