class Solution:
    """3903. 最小稳定下标 I

    下标 i 的不稳定值 = max(nums[0..i]) - min(nums[i..n-1]),
    返回第一个不稳定值 <= k 的下标,不存在返回 -1。
    """

    def firstStableIndex(self, nums: list[int], k: int) -> int:
        for i in range(len(nums)):
            # 前缀最大值与后缀最小值之差即不稳定值,<= k 说明 i 稳定
            if max(nums[:i + 1]) - min(nums[i:]) <= k:
                return i
        return -1


if __name__ == "__main__":
    s = Solution()
    assert s.firstStableIndex([5, 0, 1, 4], 3) == 3   # 官方示例 1
    assert s.firstStableIndex([3, 2, 1], 1) == -1     # 官方示例 2
    assert s.firstStableIndex([0], 0) == 0            # 官方示例 3
    assert s.firstStableIndex([5, 0], 4) == -1        # 不稳定值恒为 5,无解
    assert s.firstStableIndex([5, 0], 5) == 0         # 不稳定值恰好等于 k
    assert s.firstStableIndex([4, 1, 2, 3], 2) == 2   # i=2 时 max=4, min=2
    assert s.firstStableIndex([1, 2, 3, 4], 2) == 0   # 递增序列:下标 0 即稳定
    print("All tests passed.")