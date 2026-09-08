import random
import sys
from itertools import groupby
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根,便于导入 python/ 与本包

from python.tree_node import build_tree

from verticalTraversal import Solution


def reference(values: list) -> list:
    """独立参考实现:DFS 收集 (col, row, val) 三元组,整体排序后按列分组。"""
    root = build_tree(values)
    items = []

    def dfs(node, row, col):
        if node is None:
            return
        items.append((col, row, node.val))
        dfs(node.left, row + 1, col - 1)
        dfs(node.right, row + 1, col + 1)

    if root is not None:
        dfs(root, 0, 0)
    items.sort()
    return [[v for _, _, v in group] for _, group in groupby(items, key=lambda x: x[0])]


if __name__ == "__main__":
    s = Solution()

    def check(values: list, expected: list) -> None:
        got = s.verticalTraversal(build_tree(values))
        assert got == expected, f"{values}: expected {expected}, got {got}"

    # 力扣官方示例
    check([3, 9, 20, None, None, 15, 7], [[9], [3, 15], [20], [7]])
    check([1, 2, 3, 4, 5, 6, 7], [[4], [2], [1, 5, 6], [3], [7]])
    check([1, 2, 3, 4, 6, 5, 7], [[4], [2], [1, 5, 6], [3], [7]])  # 同行同列:5 < 6

    # 边界情况
    check([1], [[1]])                    # 单节点
    check([1, 2], [[2], [1]])            # 只有左链(负列)
    check([1, None, 3], [[1], [3]])      # 只有右链(正列)
    check([1, 2, 3], [[2], [1], [3]])    # 三节点:col -1/0/1 各一
    check([3113, 1, 4, 0, 2, 1, 1], [[0], [1], [3113, 1, 2], [4], [1]])  # 同行同列:(2,0) 的 1、2 升序

    # 随机对拍:与 DFS 三元组排序的参考实现互验(含重复值,覆盖同行同列分支)
    random.seed(42)
    for _ in range(2000):
        n = random.randint(1, 15)
        values = [random.randint(0, 9)]  # 根非空
        values += [random.choice([random.randint(0, 9), None]) for _ in range(n - 1)]
        assert s.verticalTraversal(build_tree(values)) == reference(values), values
    print("All tests passed.")