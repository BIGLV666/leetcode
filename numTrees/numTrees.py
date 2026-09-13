from functools import lru_cache
from typing import List


class Solution:
    """96. 不同的二叉搜索树

    返回由 1..n 构成的互异 BST 的数量(即卡特兰数 Catalan(n))。

    解法:区间递归 + lru_cache 记忆化。以 i 为根时,树数 = 左子树形态数 × 右子树形态数
    (笛卡尔积,勿写成相加);空区间返回 1(空树也是 1 种,基例若返回 0 会全盘归零)。
    同一 (start, end) 区间被指数次重复求值,cache 是消除重叠子问题的关键。

    复杂度:O(n^2) 个区间 × 区间内 O(n) 枚举 = O(n^3);空间 O(n^2)。
    (值域区间版;若按节点个数 g[k] = Σ g[i-1]*g[k-i] 递推可压到 O(n^2)/O(n),
    或直接用卡特兰闭式 math.comb(2n, n) // (n+1) 达 O(n)。)

    注意:cache 只该加在纯函数上——按参数哈希缓存,副作用/读外部状态的函数
    加缓存会把"第一次的结果"永久固化(见讨论:百万次 a+b 缓存版慢 3.8 倍,
    但重叠子问题场景是数万倍加速)。
    """

    def numTrees(self, n: int) -> int:
        @lru_cache(maxsize=None)
        def generateTreess(start: int, end: int) -> int:
            if start > end:
                return 1                          # 空树也是 1 种形态(基例!)
            all_node = 0
            for i in range(start, end + 1):       # 枚举根 i
                left = generateTreess(start, i - 1)   # 左值域形态数
                right = generateTreess(i + 1, end)    # 右值域形态数
                all_node += left * right          # 左右笛卡尔积(乘!)
            return all_node

        return generateTreess(1, n)


if __name__ == "__main__":
    s = Solution()
    # 力扣官方示例
    assert s.numTrees(3) == 5
    assert s.numTrees(1) == 1
    # 卡特兰数序列:1, 1, 2, 5, 14, 42, 132, 429, 1430, 4862, 16796
    expected = [1, 2, 5, 14, 42, 132, 429, 1430, 4862, 16796]  # Catalan(1..10)
    for n, want in enumerate(expected, start=1):
        assert s.numTrees(n) == want, f"n={n}: {s.numTrees(n)} != {want}"

    # 随机对拍:与「95 题分治构造后计数」这一独立参考实现互验
    import sys
    import importlib.util
    from pathlib import Path
    sys.path.insert(0, str(Path(__file__).resolve().parent.parent))
    spec = importlib.util.spec_from_file_location(
        'gt', Path(__file__).resolve().parent.parent / 'generateTrees' / 'generateTrees.py')
    gt = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(gt)
    counter = gt.SolutionDivide()
    import random

    random.seed(42)
    for _ in range(50):
        n = random.randint(1, 8)
        assert s.numTrees(n) == len(counter.generateTrees(n)), f"n={n}"
    print("All tests passed.")
