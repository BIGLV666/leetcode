import sys
import os
import copy

sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from nextPermutation.nextPermutation import Solution


def run(nums, expected):
    arr = copy.deepcopy(nums)
    Solution().nextPermutation(arr)
    assert arr == expected, f"nextPermutation({nums}) = {arr}, want {expected}"


def test_official_example_1():
    run([1, 2, 3], [1, 3, 2])


def test_official_example_2():
    run([3, 2, 1], [1, 2, 3])


def test_official_example_3():
    run([1, 1, 5], [1, 5, 1])


def test_descending_tail_swap():
    # 标准用例：交换 pivot 后需反转尾部
    run([1, 3, 2], [2, 1, 3])


def test_two_elements_ascending():
    run([1, 2], [2, 1])


def test_two_elements_descending():
    run([2, 1], [1, 2])


def test_single_element():
    run([1], [1])


def test_long_sequence():
    run([1, 2, 3, 4], [1, 2, 4, 3])


def test_suffix_already_max():
    # 尾部已是最大排列，需调整更前一位
    run([2, 3, 1], [3, 1, 2])


def test_all_equal():
    # 全部元素相同，无更大排列，返回自身
    run([5, 5, 5], [5, 5, 5])


def test_pivot_at_front():
    # 下降点在最前面，交换并反转剩余
    run([2, 1, 3], [2, 3, 1])


if __name__ == "__main__":
    failures = 0
    total = 0
    for name, fn in sorted(globals().items()):
        if name.startswith("test_") and callable(fn):
            total += 1
            try:
                fn()
                print(f"PASS {name}")
            except AssertionError as e:
                failures += 1
                print(f"FAIL {name}: {e}")
    if failures:
        sys.exit(f"{failures}/{total} test(s) failed")
    print(f"All {total} tests passed")