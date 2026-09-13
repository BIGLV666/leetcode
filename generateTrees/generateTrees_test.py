import random
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根,便于导入 python/ 与本包

from python.tree_node import tree_to_string

from generateTrees import Solution, SolutionDivide


def brute_count_structures(n: int) -> int:
    """独立参考:直接数 Catalan 数 C(n) = C(2n, n) / (n+1)。"""
    from math import comb
    return comb(2 * n, n) // (n + 1)


def normalize(trees) -> list[str]:
    """树列表 -> 排序后的序列化字符串列表(规避 LC 允许的任意顺序)。"""
    return sorted(tree_to_string(t) for t in trees)


def main() -> None:
    enum = Solution()
    divide = SolutionDivide()

    # 官方示例:n=1 与 n=3
    assert normalize(enum.generateTrees(1)) == ["[1]"]
    assert normalize(divide.generateTrees(1)) == ["[1]"]
    expected3 = ["[1,null,2,null,3]", "[1,null,3,2]", "[2,1,3]",
                 "[3,1,null,null,2]", "[3,2,null,1]"]
    assert normalize(enum.generateTrees(3)) == sorted(expected3)
    assert normalize(divide.generateTrees(3)) == sorted(expected3)

    # 边界:n=0 -> 空列表;n=2 -> 2 棵
    assert enum.generateTrees(0) == [] and divide.generateTrees(0) == []
    assert normalize(enum.generateTrees(2)) == ["[1,null,2]", "[2,1]"]
    assert normalize(divide.generateTrees(2)) == ["[1,null,2]", "[2,1]"]

    # 数量校验:必须是 Catalan 数(去重后无遗漏、无重复)
    for n in range(1, 9):
        trees = divide.generateTrees(n)
        assert len(trees) == brute_count_structures(n), f"n={n}: {len(trees)} != Catalan"
        assert len({tree_to_string(t) for t in trees}) == len(trees), f"n={n}: 有重复"

    # 两版互验:n=8 以内输出集合完全一致(排列法在 n=8 尚可运行)
    for n in range(1, 9):
        assert normalize(enum.generateTrees(n)) == normalize(divide.generateTrees(n)), f"n={n}"

    print("All tests passed.")


if __name__ == "__main__":
    main()