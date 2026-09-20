"""584. 寻找用户推荐人 —— 用 sqlite3 实跑校验 .sql 解法(借助 python/sql_harness.py)。

重点覆盖经典的 NULL 陷阱:referee_id != 2 不会命中 NULL,
所以必须显式写 IS NULL 才能找回没有推荐人的客户。
"""

import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根

from python.sql_harness import run_sql_file

SQL_FILE = Path(__file__).resolve().parent / "find-customer-referee.sql"

SCHEMA = """
CREATE TABLE Customer (
    id INT,
    name VARCHAR(255),
    referee_id INT,
    referrer_id INT
);
"""

# 官方示例数据:Will/Jane/Bill 的 referee_id 为 NULL,Alex/Gal 的推荐人是 2,Zack 的是 1
DATA = {
    "Customer": [
        (1, "Will", None, None),
        (2, "Jane", None, None),
        (3, "Alex", 2, None),
        (4, "Bill", None, None),
        (5, "Zack", 1, None),
        (6, "Gal", 2, None),
    ],
}

EXPECTED_COLUMNS = ["name"]
# 没有推荐人(NULL)或推荐人不是 2 的客户
EXPECTED_ROWS = [("Bill",), ("Jane",), ("Will",), ("Zack",)]


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
