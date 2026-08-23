#include <vector>
#include<unordered_map>
using namespace std;
/**
 * @brief 两数之和
 * @param nums 输入数组
 * @param target 目标值
 * @return vector<int> 两个数的索引
 */
vector<int> twoSum(vector<int>& nums, int target){
    vector<int> res;
    unordered_map<int,int> mp;
    for (int i = 0; i < nums.size(); i++){
        if (mp.find(target-nums[i]) != mp.end()){
            res.push_back(mp[target-nums[i]]);
            res.push_back(i);
            return res;
        }
        mp[nums[i]] = i;
    }
    return res;
}

