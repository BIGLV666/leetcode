import unittest
from reformatDate.reformatDate import Solution


class ReformatDateTest(unittest.TestCase):
    def setUp(self):
        self.solution = Solution()

    def test_examples_and_boundaries(self):
        cases = {
            "20th Oct 2052": "2052-10-20",
            "6th Jun 1933": "1933-06-06",
            "26th May 1960": "1960-05-26",
            "1st Jan 2000": "2000-01-01",
        }
        for date, want in cases.items():
            with self.subTest(date=date):
                self.assertEqual(self.solution.reformatDate(date), want)


if __name__ == "__main__":
    unittest.main()
