"""1378. 使用唯一标识码替换员工ID —— 用 sqlite3 实跑校验 .sql 解法(借助 python/sql_harness.py)。"""

import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根

from python.sql_harness import run_sql_file

SQL_FILE = Path(__file__).resolve().parent / "replace-employee-id-with-the-unique-identifier.sql"

SCHEMA = """
CREATE TABLE Employees (
    id INT,
    name VARCHAR(255)
);
CREATE TABLE EmployeeUNI (
    id INT,
    unique_id INT
);
"""

# 官方示例数据
EMPLOYEES = [
    (1, "Alice"),
    (7, "Bob"),
    (11, "Meir"),
    (90, "Winston"),
    (3, "Jonathan"),
]
EMPLOYEE_UNI = [
    (3, 1),
    (11, 2),
    (90, 3),
]

# 官方期望输出:列顺序是 unique_id、name;行顺序不限,比较时排序
EXPECTED_COLUMNS = ["unique_id", "name"]
EXPECTED_ROWS = [
    (None, "Alice"),
    (None, "Bob"),
    (2, "Meir"),
    (3, "Winston"),
    (1, "Jonathan"),
]


def main() -> None:
    results = run_sql_file(SQL_FILE, SCHEMA, {"Employees": EMPLOYEES, "EmployeeUNI": EMPLOYEE_UNI})

    assert len(results) == 1, f"应有 1 条语句, 实际 {len(results)} 条"
    columns, rows = results[0]
    assert columns == EXPECTED_COLUMNS, f"列名/顺序不符: 期望 {EXPECTED_COLUMNS}, 实际 {columns}"
    assert sorted(rows, key=str) == sorted(EXPECTED_ROWS, key=str), (
        f"结果不符: 期望 {sorted(EXPECTED_ROWS, key=str)}, 实际 {sorted(rows, key=str)}"
    )

    print("All tests passed.")


if __name__ == "__main__":
    main()
