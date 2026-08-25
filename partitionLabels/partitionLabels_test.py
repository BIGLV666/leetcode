import sys
import os

sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from partitionLabels.partitionLabels import Solution


def run(s, expected):
    got = Solution().partitionLabels(s)
    assert got == expected, f"partitionLabels({s!r}) = {got}, want {expected}"


def test_official_example():
    # 官方示例
    run("ababcbacadefegdehijhklij", [9, 7, 8])


def test_official_single_segment():
    # 官方示例：所有字母必须在一个片段内
    run("eccbbbbdec", [10])


def test_single_char():
    # 单个字符
    run("a", [1])


def test_all_distinct():
    # 每个字符只出现一次，每字符一个片段
    run("abc", [1, 1, 1])


def test_all_same():
    # 全部相同字符，整个串一个片段
    run("aaaa", [4])


def test_empty():
    # 空字符串
    run("", [])


def test_repeated_boundary():
    # a、b 各自仅出现一次，互不重叠，应切成两个单字符片段
    run("ab", [1, 1])


def test_concatenated_segments():
    # 两端各成一段：第一段在 index2 结束，第二段 index3 开始，互不交叉
    run("aabbccddee", [2, 2, 2, 2, 2])


def test_overlap_merges():
    # 后一字符的区间与前一区间相交，被迫合并为一段
    run("abba", [4])


def test_contained_segment():
    # 一个字符的区间完全包含在另一字符区间内
    run("abcacb", [6])


def test_multi_distinct_blocks():
    # 多个互不相交的字母块
    run("aaabbccd", [3, 2, 2, 1])


def test_already_separated_blocks():
    # 交错但最终能准确切分
    run("abcabcabc", [9])


def test_interleaved_boundary():
    # 边界字符恰好切分
    run("qiejxqfnqceocmy", [13, 1, 1], )


if __name__ == "__main__":
    # 收集并执行所有 test_ 用例
    failures = 0
    for name, fn in sorted(globals().items()):
        if name.startswith("test_") and callable(fn):
            try:
                fn()
                print(f"PASS {name}")
            except AssertionError as e:
                failures += 1
                print(f"FAIL {name}: {e}")
    if failures:
        sys.exit(f"{failures} test(s) failed")
    print(f"All {sum(1 for k in globals() if k.startswith('test_'))} tests passed")