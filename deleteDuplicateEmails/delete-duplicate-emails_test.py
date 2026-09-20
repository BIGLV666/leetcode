"""196. 删除重复的电子邮箱 —— 用 sqlite3 实跑校验 .sql 解法(借助 python/sql_harness.py)。

题解是 DELETE 语句,验证方式是:执行后回查 Person 表,
确认 email 重复的行被删掉、每组 email 只留下 id 最小的那一行。
"""

import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根

import sqlite3

from python.sql_harness import statements

SQL_FILE = Path(__file__).resolve().parent / "delete-duplicate-emails.sql"

SCHEMA = """
CREATE TABLE Person (
    id INT,
    email VARCHAR(255)
);
"""

# 官方示例数据:john@example.com 出现两次,只保留 id=1 的那行
DATA = {"Person": [(1, "john@example.com"), (2, "bob@example.com"), (3, "john@example.com")]}

EXPECTED_AFTER = [(1, "john@example.com"), (2, "bob@example.com")]


def main() -> None:
    sql_text = SQL_FILE.read_text(encoding="utf-8")
    stmts = statements(sql_text)
    assert len(stmts) == 1, f"应有 1 条语句, 实际 {len(stmts)} 条"

    conn = sqlite3.connect(":memory:")
    try:
        conn.executescript(SCHEMA)
        conn.executemany("INSERT INTO Person VALUES (?, ?)", DATA["Person"])
        conn.execute(stmts[0])

        rows = conn.execute("SELECT id, email FROM Person ORDER BY id").fetchall()
        assert rows == EXPECTED_AFTER, (
            f"删除后的表不符:\n  期望 {EXPECTED_AFTER}\n  实际 {rows}"
        )
    finally:
        conn.close()

    print("All tests passed.")


if __name__ == "__main__":
    main()
