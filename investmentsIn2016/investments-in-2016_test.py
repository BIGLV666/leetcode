"""585. 2016年的投资 —— 用 sqlite3 实跑校验 .sql 解法(借助 python/sql_harness.py)。

保险投保人满足以下两个条件时计入求和:
  ① tiv_2015 与至少另一个投保人相同;
  ② (lat, lon) 与其他任何投保人都不相同(所在城市唯一)。
输出这些投保人 tiv_2016 的总和,保留 2 位小数。
"""

import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根

from python.sql_harness import run_sql_file

SQL_FILE = Path(__file__).resolve().parent / "investments-in-2016.sql"

SCHEMA = """
CREATE TABLE Insurance (
    pid INT,
    tiv_2015 FLOAT,
    tiv_2016 FLOAT,
    lat FLOAT,
    lon FLOAT
);
"""

# 官方示例数据:
#   tiv_2015=10 的有 pid 1/3/4(满足①);其中 pid3 的 (20,20) 与 pid2 重复(不满足②)
#   满足两个条件的只有 pid1(5) 和 pid4(40) -> 5 + 40 = 45
DATA = {
    "Insurance": [
        (1, 10, 5, 10, 10),
        (2, 20, 20, 20, 20),
        (3, 10, 30, 20, 20),
        (4, 10, 40, 40, 40),
    ],
}

EXPECTED_COLUMNS = ["tiv_2016"]
EXPECTED_VALUE = 45.0


def main() -> None:
    results = run_sql_file(SQL_FILE, SCHEMA, DATA)

    assert len(results) == 1, f"应有 1 条语句, 实际 {len(results)} 条"
    columns, rows = results[0]
    assert columns == EXPECTED_COLUMNS, f"列名不符: 期望 {EXPECTED_COLUMNS}, 实际 {columns}"
    assert len(rows) == 1 and len(rows[0]) == 1, f"应只返回 1 行 1 列, 实际 {rows}"
    got = rows[0][0]
    assert got is not None, "结果为 NULL"
    assert abs(got - EXPECTED_VALUE) < 1e-9, f"期望 {EXPECTED_VALUE}, 实际 {got}"

    print("All tests passed.")


if __name__ == "__main__":
    main()
