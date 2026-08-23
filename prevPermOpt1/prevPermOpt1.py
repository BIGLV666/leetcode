
from typing import List

# 1053.交换一次的先前排列
# @link{https://leetcode.cn/problems/previous-permutation-with-one-swap/submissions/744180163/}
#
# 思路：交换一次得到字典序中比当前排列小的最大排列
#   - 从右向左找到第一个满足 arr[i-1] > arr[i] 的位置 i，作为交换点（枢轴）
#   - 在枢轴右侧从右向左寻找比 arr[i-1] 小且最大的元素与之交换（遇到重复值跳过，保证交换结果字典序最大）
#   - 若不存在这样的下降点，说明当前排列已是字典序最小，原样返回
#
# 时间复杂度：O(n^2)，外层 O(n) × 内层扫描 O(n)
# 空间复杂度：O(1)，原地交换，仅使用常数额外空间

class Solution:
    def prevPermOpt1(self, arr: List[int]) -> List[int]:
        n = len(arr)
        for i in range(n - 1, 0, -1):
            if arr[i - 1] > arr[i]:
                for j in range(n - 1, i - 1, -1):
                    if arr[j] < arr[i - 1] and arr[j] != arr[j - 1]:
                        arr[i - 1], arr[j] = arr[j], arr[i - 1]
                        return arr
        return arr

