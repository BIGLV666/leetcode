
from typing import List
#title{31.下一个排列}
#link{https://leetcode.cn/problems/next-permutation/?envType=study-plan-v2&envId=top-100-liked}
#
# 思路：三步
#   1) 从右往左找第一个满足 nums[i] < nums[i+1] 的位置 i（下降点 / pivot）。
#      它右侧的 nums[i+1:] 此时是单调降序（因为从末尾看一直是 >= 的）。
#   2) 在右侧找到第一个比 nums[i] 大的元素 nums[j]，与 nums[i] 交换，实现"某一位变大"。
#   3) 把 i 右侧的部分反转：交换后该段仍是降序（字典序最大），反转成升序（字典序最小）。
#      两者结合才能得到紧邻的下一个排列。
#   若 i < 0，说明整个数组已经是最大排列（全局降序），跳过步骤2，直接整体反转，得到最小排列。
#
# 时间复杂度：O(n)，步骤2/3 各一趟线性扫描，整体仍是线性
# 空间复杂度：O(1)，原地交换，仅常数额外空间

class Solution:
    def nextPermutation(self, nums: List[int]) -> None:
        # 步骤1：找到下降点 i（右侧是降序段）
        i = len(nums) - 2
        while i >= 0 and nums[i] >= nums[i + 1]:
            i -= 1

        # 步骤2：与右侧第一个大于 nums[i] 的元素交换（若 i>=0）
        if i >= 0:
            j = len(nums) - 1
            while j >= 0 and nums[i] >= nums[j]:
                j -= 1
            nums[i], nums[j] = nums[j], nums[i]

        # 步骤3：反转 i 右侧的段。
        # 为什么要反转：交换后该段(nums[i+1:])仍是降序(字典序最大)，
        # 但下一个排列中该段应取字典序最小形式，即升序。
        # 反转恰好把"最大降序"拨回"最小升序"，从而得到正确的后一个排列。
        left, right = i + 1, len(nums) - 1
        while left < right:
            nums[left], nums[right] = nums[right], nums[left]
            left += 1
            right -= 1
