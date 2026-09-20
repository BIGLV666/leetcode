"""183. 从不订购的客户 —— 用 sqlite3 实跑校验 .sql 解法(借助 python/sql_harness.py)。"""

import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根

from python.sql_harness import run_sql_file

SQL_FILE = Path(__file__).resolve().parent / "customers-who-never-order.sql"

SCHEMA = """
CREATE TABLE Customers (
    id INT,
    name VARCHAR(255)
);
CREATE TABLE Orders (
    id INT,
    customerId INT
);
"""

# 官方示例数据
DATA = {
    "Customers": [(1, "Joe"), (2, "Henry"), (3, "Sam"), (4, "Max")],
    "Orders": [(1, 1), (2, 2)],
}

# 官方期望输出:从没在 Orders 里出现过的客户。题目不限定行顺序,故排序后比较
EXPECTED_COLUMNS = ["Customers"]
EXPECTED_ROWS = [("Max",), ("Sam",)]


def main() -> None:
    results = run_sql_file(SQL_FILE, SCHEMA, DATA)

    assert len(results) == 1, f"应有 1 条语句, 实际 {len(results)} 条"
    columns, rows = results[0]
    assert columns == EXPECTED_COLUMNS, f"列名/顺序不符: 期望 {EXPECTED_COLUMNS}, 实际 {columns}"
    assert sorted(rows, key=str) == sorted(EXPECTED_ROWS, key=str), (
        f"结果不符: 期望 {sorted(EXPECTED_ROWS, key=str)}, 实际 {sorted(rows, key=str)}"
    )

    print("All tests passed.")


if __name__ == "__main__":
    main()
