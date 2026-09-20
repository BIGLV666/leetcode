import importlib.util
import unittest
from pathlib import Path

_module_path = Path(__file__).resolve().with_name("smallestRepunitDivByK.py")
_spec = importlib.util.spec_from_file_location("smallestRepunitDivByK_solution", _module_path)
_module = importlib.util.module_from_spec(_spec)
_spec.loader.exec_module(_module)
Solution = _module.Solution


class TestSmallestRepunitDivByK(unittest.TestCase):
    def setUp(self):
        self.solution = Solution()

    def test_k_equals_1(self):
        self.assertEqual(self.solution.smallestRepunitDivByK(1), 1)

    def test_k_even_returns_minus1(self):
        for k in (2, 4, 6):
            self.assertEqual(self.solution.smallestRepunitDivByK(k), -1)

    def test_k_multiple_of_5_returns_minus1(self):
        for k in (5, 10):
            self.assertEqual(self.solution.smallestRepunitDivByK(k), -1)

    def test_k_equals_3(self):
        self.assertEqual(self.solution.smallestRepunitDivByK(3), 3)

    def test_k_equals_7(self):
        self.assertEqual(self.solution.smallestRepunitDivByK(7), 6)

    def test_k_equals_9(self):
        self.assertEqual(self.solution.smallestRepunitDivByK(9), 9)


if __name__ == '__main__':
    unittest.main()
