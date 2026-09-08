import random
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根,便于导入 python/ 与本包

from python.tree_node import build_tree, tree_to_array

from pruneTree import Solution


def reference(values: list) -> list:
    """独立参考实现:后序递归剪枝(先剪子树再判断自身),返回层序数组。"""
    def prune(node):
        if node is None:
            return None
        node.left = prune(node.left)
        node.right = prune(node.right)
        if node.val == 0 and node.left is None and node.right is None:
            return None
        return node

    return tree_to_array(prune(build_tree(values)))


if __name__ == "__main__":
    s = Solution()

    def check(values: list, expected: list) -> None:
        got = tree_to_array(s.pruneTree(build_tree(values)))
        assert got == expected, f"{values}: expected {expected}, got {got}"

    # 力扣官方示例
    check([1, None, 0, 0, 1], [1, None, 0, None, 1])
    check([1, 0, 1, 0, 0, 0, 1], [1, None, 1, None, 1])
    check([1, 1, 0, 1, 1, 0, 1, 0], [1, 1, 0, 1, 1, None, 1])

    # 边界情况
    check([0], [])                    # 单个 0:整棵树剪空
    check([1], [1])                   # 单个 1
    check([0, 0, 0], [])              # 全 0 树
    check([1, None, 0, None, 0], [1]) # 右链级联剪:0 叶 -> 0 -> 只剩根
    check([1, 1, 1, 1, 1], [1, 1, 1, 1, 1])  # 全 1:不动
    check([1, 0, 1], [1, None, 1])    # 左 0 叶剪掉,右子树保留

    # 随机对拍:与后序递归参考实现互验
    random.seed(42)
    for _ in range(2000):
        n = random.randint(1, 15)
        values = [random.choice([0, 1])]  # 根非空(题目保证 n>=1)
        values += [random.choice([0, 1, None]) for _ in range(n - 1)]
        expected = reference(values)
        got = tree_to_array(s.pruneTree(build_tree(values)))
        assert got == expected, f"{values}: expected {expected}, got {got}"
    print("All tests passed.")