import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根,便于导入 python/ 与本包

from python.list_node import build_list, list_to_array

from reverseEvenLengthGroups import Solution  # 同目录的 reverseEvenLengthGroups.py


def reference(values: list[int]) -> list[int]:
    """独立参考实现:切片分组,偶数组用 [::-1] 反转后拼接。"""
    res = []
    index, x = 0, 1
    while index < len(values):
        size = min(x, len(values) - index)
        group = values[index:index + size]
        if size % 2 == 0:
            group = group[::-1]
        res.extend(group)
        index += x
        x += 1
    return res


if __name__ == "__main__":
    s = Solution()

    def check(values, expected):
        got = list_to_array(s.reverseEvenLengthGroups(build_list(values)))
        assert got == expected, f"{values}: expected {expected}, got {got}"

    # 力扣官方示例
    check([5, 2, 6, 3, 9, 1, 7, 3, 8, 4], [5, 6, 2, 3, 9, 1, 4, 8, 3, 7])
    check([1, 1, 0, 6], [1, 0, 1, 6])
    check([1, 1, 0, 6, 5], [1, 0, 1, 5, 6])
    check([2, 1], [2, 1])

    # 边界:单节点(组 1,不翻)
    check([7], [7])
    # 边界:两个节点(组 2 只剩 1 个,奇数不翻)
    check([1, 2], [1, 2])
    # 关键用例:末组不完整且为偶数长度(旧代码因 r 越界直接跳过 → 答案错)
    check(list(range(1, 15)), [1, 3, 2, 4, 5, 6, 10, 9, 8, 7, 14, 13, 12, 11])
    # 末组不完整且为奇数长度(无需反转)
    check(list(range(1, 14)), [1, 3, 2, 4, 5, 6, 10, 9, 8, 7, 11, 12, 13])
    # 末组恰好完整
    check(list(range(1, 11)), [1, 3, 2, 4, 5, 6, 10, 9, 8, 7])

    # 随机对拍
    import random

    random.seed(42)
    for _ in range(2000):
        n = random.randint(1, 60)
        vals = [random.randint(0, 9) for _ in range(n)]
        got = list_to_array(s.reverseEvenLengthGroups(build_list(vals)))
        assert got == reference(vals), vals
    print("All tests passed.")