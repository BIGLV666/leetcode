"""184. 部门工资最高的员工 —— 用 sqlite3 实跑校验 .sql 解法(借助 python/sql_harness.py)。"""

import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根

from python.sql_harness import run_sql_file

SQL_FILE = Path(__file__).resolve().parent / "department-highest-salary.sql"

SCHEMA = """
CREATE TABLE Employee (
    id INT,
    name VARCHAR(255),
    salary INT,
    departmentId INT
);
CREATE TABLE Department (
    id INT,
    name VARCHAR(255)
);
"""

# 官方示例数据:IT 的最高工资 90000(Jim),Sales 的最高工资 90000(Max)
DATA = {
    "Employee": [
        (1, "Joe", 70000, 1),
        (2, "Jim", 90000, 1),
        (3, "Henry", 80000, 2),
        (4, "Sam", 60000, 2),
        (5, "Max", 90000, 2),
    ],
    "Department": [(1, "IT"), (2, "Sales")],
}

EXPECTED_COLUMNS = ["Department", "Employee", "Salary"]
EXPECTED_ROWS = [("IT", "Jim", 90000), ("Sales", "Max", 90000)]


def main() -> None:
    results = run_sql_file(SQL_FILE, SCHEMA, DATA)

    assert len(results) == 1, f"应有 1 条语句, 实际 {len(results)} 条"
    columns, rows = results[0]
    assert columns == EXPECTED_COLUMNS, f"列名/顺序不符: 期望 {EXPECTED_COLUMNS}, 实际 {columns}"
    # 题目不限定行顺序,排序后比较
    assert sorted(rows, key=str) == sorted(EXPECTED_ROWS, key=str), (
        f"结果不符: 期望 {sorted(EXPECTED_ROWS, key=str)}, 实际 {sorted(rows, key=str)}"
    )

    print("All tests passed.")


if __name__ == "__main__":
    main()
