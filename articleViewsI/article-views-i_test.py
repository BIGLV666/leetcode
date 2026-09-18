"""1148. 文章浏览 I —— 用 sqlite3 实跑校验 .sql 解法

LeetCode 的 SQL 题没有现成的测试框架,这里用 Python 标准库 sqlite3:
建出题目给的 Views 表 -> 灌入官方示例数据 -> 执行同目录的 .sql -> 断言结果。

这样 SQL 解法也能像其他语言一样被自动化验证,不依赖任何外部数据库。
"""

import sqlite3
from pathlib import Path

SQL_FILE = Path(__file__).resolve().parent / "article-views-i.sql"

SCHEMA = """
CREATE TABLE Views (
    article_id INT,
    author_id INT,
    viewer_id INT,
    view_date DATE
);
"""

# 官方示例数据
VIEWS = [
    (1, 3, 5, "2019-08-01"),
    (1, 3, 6, "2019-08-02"),
    (2, 7, 7, "2019-08-01"),
    (2, 7, 6, "2019-08-02"),
    (4, 7, 1, "2019-08-01"),
    (3, 4, 4, "2019-08-01"),
    (3, 4, 4, "2019-08-02"),
]

# 官方期望输出:浏览过自己文章的作者 id,按 id 升序(4 和 7 都看过自己的文章)
EXPECTED_COLUMNS = ["id"]
EXPECTED_ROWS = [(4,), (7,)]


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
        conn.executemany("INSERT INTO Views VALUES (?, ?, ?, ?)", VIEWS)
        cur = conn.execute(strip_comments(sql))
        columns = [d[0] for d in cur.description]
        return columns, cur.fetchall()
    finally:
        conn.close()


def main() -> None:
    sql = SQL_FILE.read_text(encoding="utf-8")
    columns, rows = run_sql(sql)

    assert columns == EXPECTED_COLUMNS, f"列名/顺序不符: 期望 {EXPECTED_COLUMNS}, 实际 {columns}"
    assert rows == EXPECTED_ROWS, f"结果不符: 期望 {EXPECTED_ROWS}, 实际 {rows}"

    print("All tests passed.")


if __name__ == "__main__":
    main()
