from typing import List


class Solution:
    """78. 子集 —— 增量构造版(非回溯,双层循环)

    从空集 [[]] 出发,每引入一个新元素 x:复制当前每个子集、追加 x,作为新增子集。
    处理完 i 个元素后恰好拥有 2^i 个子集。

    关键:内层循环必须以进入时的 len(res) 快照为上界——若用 res.size() 动态判断,
    本轮新追加的子集会被再次遍历,无限翻倍。

    复杂度:时间 O(n · 2^n),空间 O(n · 2^n)(输出本身该量级,额外 O(1))。
    """

    def subsets(self, nums: List[int]) -> List[List[int]]:
        res = [[]]  # 空集是幂集的一员,也是增量构造的种子
        for x in nums:                     # 外层:逐个引入元素
            size = len(res)                # 快照!只扩展"不含 x 的旧子集"
            for i in range(size):
                res.append(res[i] + [x])   # 旧子集 + x = 新子集(res[i] 本身不被修改)
        return res


if __name__ == "__main__":
    s = Solution()

    def check(nums: List[int]) -> None:
        got = s.subsets(nums)
        # ① 数量恰为 2^n;② 无重复;③ 每个子集都是 nums 的子序列
        n = len(nums)
        assert len(got) == (1 << n), f"{nums}: 数量 {len(got)} != {1 << n}"
        assert len({tuple(sub) for sub in got}) == len(got), f"{nums}: 有重复子集"
        for sub in got:
            it = iter(nums)
            assert all(v in it for v in sub), f"{nums}: {sub} 不是子序列"

    # 官方示例
    assert sorted(map(tuple, s.subsets([1, 2, 3]))) == sorted(
        [(), (1,), (2,), (3,), (1, 2), (1, 3), (2, 3), (1, 2, 3)])
    assert sorted(map(tuple, s.subsets([0]))) == sorted([(), (0,)])

    # 边界
    check([5])
    check([-1, 2])
    check([1, 2, 3, 4])

    # 随机对拍:与位掩码参考实现互验(nums 必须互不相同,符合题面约束)
    import random

    random.seed(42)
    for _ in range(2000):
        n = random.randint(1, 8)
        nums = random.sample(range(-10, 11), n)  # sample 保证互不相同
        expected = [[nums[i] for i in range(n) if mask >> i & 1]
                    for mask in range(1 << n)]
        got = s.subsets(nums)
        assert sorted(map(tuple, got)) == sorted(map(tuple, expected)), nums
        check(nums)
    print("All tests passed.")


