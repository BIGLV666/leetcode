import random
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根,便于导入 python/ 与本包

from python.tree_node import build_tree, tree_to_array

from decorateRecord import Solution


def reference_iii(root) -> list:
    """III 的独立参考实现:正常 BFS 分层,奇数层(0-based)倒序。"""
    if root is None:
        return []
    res = []
    level = [root]
    idx = 0
    while level:
        vals = [n.val for n in level]
        res.append(vals[::-1] if idx % 2 == 1 else vals)
        nxt = []
        for n in level:
            if n.left:
                nxt.append(n.left)
            if n.right:
                nxt.append(n.right)
        level = nxt
        idx += 1
    return res


def main() -> None:
    s = Solution()

    # 官方示例(剑指 32-III: [3,9,20,null,null,15,7])
    root = build_tree([3, 9, 20, None, None, 15, 7])
    assert s.decorateRecord(root) == [3, 9, 20, 15, 7]
    assert s.decorateRecordii(root) == [[3], [9, 20], [15, 7]]
    assert s.decorateRecordiii(root) == [[3], [20, 9], [15, 7]]

    # 边界:空树
    assert s.decorateRecord(None) == []
    assert s.decorateRecordii(None) == []
    assert s.decorateRecordiii(None) == []

    # 边界:单节点
    single = build_tree([1])
    assert s.decorateRecord(single) == [1]
    assert s.decorateRecordii(single) == [[1]]
    assert s.decorateRecordiii(single) == [[1]]

    # 链状树(每层只有 1 个节点,III 的倒序不改变内容)
    chain = build_tree([1, 2, None, 3, None, 4])
    assert s.decorateRecordiii(chain) == [[1], [2], [3], [4]]

    # 随机对拍:与"先正常分层再倒序奇数层"参考实现互验
    random.seed(42)
    for _ in range(2000):
        n = random.randint(1, 15)
        values = [random.randint(0, 9)]
        values += [random.choice([random.randint(0, 9), None]) for _ in range(n - 1)]
        root = build_tree(values)

        # I:一维层序 == 独立 BFS(只含实体节点)
        ref = []
        if root:
            from collections import deque as _dq
            _q = _dq([root])
            while _q:
                nd = _q.popleft()
                ref.append(nd.val)
                if nd.left:
                    _q.append(nd.left)
                if nd.right:
                    _q.append(nd.right)
        assert s.decorateRecord(root) == ref, values

        # II:分层 == 按层展开
        levels = []
        level, idx = [root], 0
        while level:
            levels.append([x.val for x in level])
            nxt = []
            for x in level:
                if x.left:
                    nxt.append(x.left)
                if x.right:
                    nxt.append(x.right)
            level = nxt
        assert s.decorateRecordii(root) == levels, values

        # III:与参考实现一致
        assert s.decorateRecordiii(root) == reference_iii(root), values

    print("All tests passed.")


if __name__ == "__main__":
    main()