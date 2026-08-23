import unittest
from smallestRepunitDivByK.smallestRepunitDivByK import Solution


class TestSmallestRepunitDivByK(unittest.TestCase):
    def setUp(self):
        self.solution = Solution()

    def test_k_equals_1(self):
        # 1 % 1 == 0，长度为1的1即可整除
        self.assertEqual(self.solution.smallestRepunitDivByK(1), 1)

    def test_k_even_returns_minus1(self):
        # 偶数不可能被全1数整除
        self.assertEqual(self.solution.smallestRepunitDivByK(2), -1)
        self.assertEqual(self.solution.smallestRepunitDivByK(4), -1)
        self.assertEqual(self.solution.smallestRepunitDivByK(6), -1)

    def test_k_multiple_of_5_returns_minus1(self):
        # 5的倍数不可能被全1数整除
        self.assertEqual(self.solution.smallestRepunitDivByK(5), -1)
        self.assertEqual(self.solution.smallestRepunitDivByK(10), -1)

    def test_k_equals_3(self):
        # 111 % 3 == 0，返回3
        self.assertEqual(self.solution.smallestRepunitDivByK(3), 3)

    def test_k_equals_7(self):
        # 111111 % 7 == 0，返回6
        self.assertEqual(self.solution.smallestRepunitDivByK(7), 6)

    def test_k_equals_9(self):
        # 111111111 % 9 == 0，返回9
        self.assertEqual(self.solution.smallestRepunitDivByK(9), 9)


if __name__ == '__main__':
    unittest.main()
