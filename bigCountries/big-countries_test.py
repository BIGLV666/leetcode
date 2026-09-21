import sqlite3
import unittest
from pathlib import Path


class BigCountriesTest(unittest.TestCase):
    def test_area_or_population_threshold(self):
        connection = sqlite3.connect(":memory:")
        connection.execute("CREATE TABLE World (name TEXT, continent TEXT, area INTEGER, population INTEGER, gdp INTEGER)")
        connection.executemany("INSERT INTO World VALUES (?, ?, ?, ?, ?)", [
            ("A", "Asia", 3000000, 1, 1),
            ("B", "Europe", 1, 25000000, 1),
            ("C", "Africa", 2999999, 24999999, 1),
        ])
        sql = Path(__file__).with_name("big-countries.sql").read_text(encoding="utf-8")
        self.assertEqual(connection.execute(sql).fetchall(), [("A", 1, 3000000), ("B", 25000000, 1)])
        connection.close()


if __name__ == "__main__":
    unittest.main()
