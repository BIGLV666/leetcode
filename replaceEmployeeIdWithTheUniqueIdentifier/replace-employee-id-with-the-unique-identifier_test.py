"""1378. 使用唯一标识码替换员工ID —— 用 sqlite3 实跑校验 .sql 解法

LeetCode 的 SQL 题没有现成的测试框架,这里用 Python 标准库 sqlite3:
建出题目给的 Employees / EmployeeUNI 两张表 -> 灌入官方示例数据 -> 执行同目录的 .sql -> 断言结果。

这样 SQL 解法也能像其他语言一样被自动化验证,不依赖任何外部数据库。
"""

import sqlite3
from pathlib import Path

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

# 官方期望输出:列顺序是 unique_id、name;行顺序不限,所以比较时排序
EXPECTED_COLUMNS = ["unique_id", "name"]
EXPECTED_ROWS = [
    (None, "Alice"),
    (None, "Bob"),
    (2, "Meir"),
    (3, "Winston"),
    (1, "Jonathan"),
]


def strip_comments(sql: str) -> str:
    """去掉 `--` 行注释,只把可执行语句交给 sqlite3。"""
    return "\n".join(
        line for line in sql.splitlines() if not line.strip().startswith("--")
    )


def run_sql(sql: str):
    """在内存库里建表、灌数据,执行 sql,返回 (列名列表, 结果行)。"""
    conn = sqlite3.connect(":memory:")
    try:
        conn.executescript(SCHEMA)
        conn.executemany("INSERT INTO Employees VALUES (?, ?)", EMPLOYEES)
        conn.executemany("INSERT INTO EmployeeUNI VALUES (?, ?)", EMPLOYEE_UNI)
        cur = conn.execute(strip_comments(sql))
        columns = [d[0] for d in cur.description]
        return columns, cur.fetchall()
    finally:
        conn.close()


def main() -> None:
    sql = SQL_FILE.read_text(encoding="utf-8")
    columns, rows = run_sql(sql)

    assert columns == EXPECTED_COLUMNS, f"列名/顺序不符: 期望 {EXPECTED_COLUMNS}, 实际 {columns}"
    # 题目允许任意行顺序,故按 str 排序后比较(元组里含 None,不能直接比大小)
    assert sorted(rows, key=str) == sorted(EXPECTED_ROWS, key=str), (
        f"结果不符: 期望 {sorted(EXPECTED_ROWS, key=str)}, 实际 {sorted(rows, key=str)}"
    )

    print("All tests passed.")


if __name__ == "__main__":
    main()
