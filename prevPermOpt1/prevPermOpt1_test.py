import os, sys, unittest
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
from prevPermOpt1.prevPermOpt1 import Solution


class TestPrevPermOpt1(unittest.TestCase):
    def setUp(self):
        self.solution = Solution()

    # ===== 边界: 空数组 =====
    def test_empty_array(self):
        self.assertEqual(self.solution.prevPermOpt1([]), [])

    # ===== 边界: 单元素 =====
    def test_single_element(self):
        self.assertEqual(self.solution.prevPermOpt1([1]), [1])
        self.assertEqual(self.solution.prevPermOpt1([0]), [0])

    # ===== 边界: 两元素 =====
    def test_two_elements_increasing(self):
        # 增序无法交换出更小的排列
        self.assertEqual(self.solution.prevPermOpt1([1, 2]), [1, 2])

    def test_two_elements_decreasing(self):
        # 降序交换即可
        self.assertEqual(self.solution.prevPermOpt1([2, 1]), [1, 2])

    def test_two_elements_equal(self):
        # 两元素相等无需交换
        self.assertEqual(self.solution.prevPermOpt1([3, 3]), [3, 3])

    # ===== 边界: 全相同 =====
    def test_all_equal(self):
        # 无法通过交换变小
        self.assertEqual(self.solution.prevPermOpt1([1, 1, 1]), [1, 1, 1])
        self.assertEqual(self.solution.prevPermOpt1([5, 5]), [5, 5])

    # ===== 边界: 严格递增（无法减小） =====
    def test_strictly_increasing(self):
        self.assertEqual(self.solution.prevPermOpt1([1, 2, 3, 4, 5]), [1, 2, 3, 4, 5])
        self.assertEqual(self.solution.prevPermOpt1([1, 1, 5]), [1, 1, 5])

    # ===== 边界: 严格递减 =====
    def test_strictly_decreasing_3(self):
        # [3,2,1] 一次交换得到最大的更小排列是 [3,1,2]
        self.assertEqual(self.solution.prevPermOpt1([3, 2, 1]), [3, 1, 2])

    def test_strictly_decreasing_4(self):
        # [4,3,2,1] → [4,3,1,2]
        self.assertEqual(self.solution.prevPermOpt1([4, 3, 2, 1]), [4, 3, 1, 2])

    # ===== 标准用例 =====
    def test_standard_case(self):
        # [1,9,4,6,7] → [1,7,4,6,9]
        self.assertEqual(self.solution.prevPermOpt1([1, 9, 4, 6, 7]), [1, 7, 4, 6, 9])

    # ===== 边界: 重复值在中间 =====
    def test_duplicate_lesser_values(self):
        # [1,4,3,3,2] → [1,4,3,2,3]
        self.assertEqual(self.solution.prevPermOpt1([1, 4, 3, 3, 2]), [1, 4, 3, 2, 3])

    # ===== 边界: 尾部局部下降 =====
    def test_right_end_drop(self):
        # [5,3,4,4,3] → [5,3,4,3,4]
        self.assertEqual(self.solution.prevPermOpt1([5, 3, 4, 4, 3]), [5, 3, 4, 3, 4])

    # ===== 边界: 首元素与后段存在多个下降点 =====
    def test_multiple_drops(self):
        # [6,5,2,3,4] → [6,4,2,3,5]
        self.assertEqual(self.solution.prevPermOpt1([6, 5, 2, 3, 4]), [6, 4, 2, 3, 5])

    # ===== 边界: 首值与尾值相等中间存在更小值 =====
    def test_head_equal_to_tail_boundary(self):
        # [3,1,1,3] → [1,3,1,3]
        self.assertEqual(self.solution.prevPermOpt1([3, 1, 1, 3]), [1, 3, 1, 3])

    def test_tail_duplicate_3s(self):
        # [3,1,3,3] → [1,3,3,3]
        self.assertEqual(self.solution.prevPermOpt1([3, 1, 3, 3]), [1, 3, 3, 3])

    # ===== 边界: 最大值位于首位 =====
    def test_max_at_head(self):
        # 结果必须严格小于原数组且长度一致
        result = self.solution.prevPermOpt1([9, 1, 2, 3])
        self.assertLess(result, [9, 1, 2, 3])
        self.assertEqual(len(result), 4)

    # ===== 边界: 原地修改，数组长度保持不变 =====
    def test_in_place_swap_same_length(self):
        arr = [3, 2, 1]
        original_len = len(arr)
        result = self.solution.prevPermOpt1(arr)
        self.assertEqual(len(result), original_len)


if __name__ == '__main__':
    unittest.main()
