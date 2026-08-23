from typing import List

# 1283.使结果不超过阈值的最小除数
# @link{https://leetcode.cn/problems/find-the-smallest-divisor-given-a-threshold/description/}
#
# 思路：二分查找除数
#   - 将 nums 中每个元素除以 d 的上取整求和，记为 total：(x-1)//d + 1 即 ceil(x/d)
#   - 对除数 d 二分：total 随 d 增大而单调递减
#   - 当 total <= threshold 时 d 可行，尝试更小的 d（r=mid-1）；否则 d 太小需增大（l=mid+1）
#   - 搜索范围 [1, max(nums)]，记录满足条件的最小除数 ans
#
# 时间复杂度：O(n·log(max(nums)))，每次二分需 O(n) 求和
# 空间复杂度：O(1)，仅使用常数级变量
class Solution:
    def smallestDivisor(self, nums: List[int], threshold: int) -> int:
        l,r,ans=1,max(nums)+1,-1
        while l<=r:
            mid=(l+r)//2
            total=sum((x-1)//mid+1 for x in nums)
            if threshold>=total:
               ans=mid
               r=mid-1
            else:
               l=mid+1
        return ans
