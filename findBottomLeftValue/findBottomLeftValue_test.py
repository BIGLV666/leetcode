import random
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根,便于导入 python/ 与本包

from python.tree_node import build_tree

from findBottomLeftValue import Solution


def reference(root) -> int:
    """独立参考实现:逐层 BFS,返回最后一层的第一个节点值。"""
    level = [root]
    ans = root.val
    while level:
        ans = level[0].val
        nxt = []
        for node in level:
            if node.left:
                nxt.append(node.left)
            if node.right:
                nxt.append(node.right)
        level = nxt
    return ans


def main() -> None:
    s = Solution()

    # 官方示例
    assert s.findBottomLeftValue(build_tree([2, 1, 3])) == 1
    assert s.findBottomLeftValue(build_tree([1, 2, 3, 4, None, 5, 6, None, None, 7])) == 7

    # 边界: 单节点
    assert s.findBottomLeftValue(build_tree([0])) == 0
    # 边界: 全左斜,最底层最左就是链尾
    assert s.findBottomLeftValue(build_tree([1, 2, None, 3])) == 3
    # 边界: 左子树更深,最底层最左来自左侧分支
    assert s.findBottomLeftValue(build_tree([1, 2, 3, 4])) == 4
    # 边界: 最底层只有右侧一个节点时,它同时也是该层最左
    assert s.findBottomLeftValue(build_tree([1, 2, 3, None, None, None, 4])) == 4

    # 随机对拍:与独立 BFS 参考实现互验
    random.seed(42)
    for _ in range(2000):
        n = random.randint(1, 20)
        values = [random.randint(0, 9)]
        values += [random.choice([random.randint(0, 9), None]) for _ in range(n - 1)]
        root = build_tree(values)
        assert s.findBottomLeftValue(root) == reference(root), values

    print("All tests passed.")


if __name__ == "__main__":
    main()
