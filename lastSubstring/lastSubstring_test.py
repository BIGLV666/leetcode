import random
import unittest

from lastSubstring.lastSubstring import Solution


class LastSubstringTest(unittest.TestCase):
    def setUp(self):
        self.solution = Solution()

    def test_examples(self):
        cases = {
            "abab": "bab",
            "leetcode": "tcode",
        }
        for s, want in cases.items():
            with self.subTest(s=s):
                self.assertEqual(self.solution.lastSubstring(s), want)

    def test_single_and_repeated_character(self):
        for ch in "abcxyz":
            with self.subTest(ch=ch):
                self.assertEqual(self.solution.lastSubstring(ch), ch)
                self.assertEqual(self.solution.lastSubstring(ch * 1000), ch * 1000)

    def test_random_against_brute_force(self):
        rnd = random.Random(1163)
        for _ in range(5000):
            n = rnd.randint(1, 12)
            s = "".join(rnd.choice("abc") for _ in range(n))
            expected = max(s[i:] for i in range(n))
            with self.subTest(s=s):
                self.assertEqual(self.solution.lastSubstring(s), expected)

    def test_large_inputs(self):
        n = 400_000
        self.assertEqual(self.solution.lastSubstring("a" * n), "a" * n)
        self.assertEqual(self.solution.lastSubstring("b" + "a" * (n - 1)), "b" + "a" * (n - 1))
        self.assertEqual(self.solution.lastSubstring("a" * (n - 1) + "b"), "b")
        self.assertEqual(self.solution.lastSubstring("ab" * (n // 2)), ("ab" * (n // 2))[1:])


if __name__ == "__main__":
    unittest.main()
