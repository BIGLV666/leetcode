"""577. 员工奖金 —— 用 sqlite3 实跑校验 .sql 解法(借助 python/sql_harness.py)。

题解文件里有两种写法(CTE 版与直接版),两种都要跑、结果必须一致。
"""

import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根

from python.sql_harness import run_sql_file

SQL_FILE = Path(__file__).resolve().parent / "employee-bonus.sql"

SCHEMA = """
CREATE TABLE Employee (
    empId INT,
    name VARCHAR(255),
    supervisor INT,
    salary INT
);
CREATE TABLE Bonus (
    empId INT,
    bonus INT
);
"""

# 官方示例数据:Brad / John 没有奖金记录(bonus 为 NULL),Dan 奖金 500
DATA = {
    "Employee": [
        (3, "Brad", None, 4000),
        (1, "John", 3, 1000),
        (2, "Dan", 3, 2000),
        (4, "Thomas", 3, 4000),
    ],
    "Bonus": [(2, 500), (4, 2000)],
}

EXPECTED_COLUMNS = ["name", "bonus"]
EXPECTED_ROWS = [("Brad", None), ("Dan", 500), ("John", None)]


def main() -> None:
    results = run_sql_file(SQL_FILE, SCHEMA, DATA)

    assert len(results) == 2, f"题解文件里有 2 种写法, 实际识别到 {len(results)} 条语句"

    for idx, (columns, rows) in enumerate(results, start=1):
        assert columns == EXPECTED_COLUMNS, (
            f"写法{idx} 列名/顺序不符: 期望 {EXPECTED_COLUMNS}, 实际 {columns}"
        )
        # 奖金 < 1000 或为 NULL 的员工;题目不限定行顺序,排序后比较
        assert sorted(rows, key=str) == sorted(EXPECTED_ROWS, key=str), (
            f"写法{idx} 结果不符: 期望 {sorted(EXPECTED_ROWS, key=str)}, 实际 {sorted(rows, key=str)}"
        )

    print("All tests passed.")


if __name__ == "__main__":
    main()
