"""1148. 文章浏览 I —— 用 sqlite3 实跑校验 .sql 解法(借助 python/sql_harness.py)。"""

import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根

from python.sql_harness import run_sql_file

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

# 官方期望输出:浏览过自己文章的作者 id,题目要求按 id 升序(4 和 7 都看过自己的文章)
EXPECTED_COLUMNS = ["id"]
EXPECTED_ROWS = [(4,), (7,)]


def main() -> None:
    results = run_sql_file(SQL_FILE, SCHEMA, {"Views": VIEWS})

    assert len(results) == 1, f"应有 1 条语句, 实际 {len(results)} 条"
    columns, rows = results[0]
    assert columns == EXPECTED_COLUMNS, f"列名/顺序不符: 期望 {EXPECTED_COLUMNS}, 实际 {columns}"
    # 这题明确要求按 id 升序,所以直接断言精确顺序
    assert rows == EXPECTED_ROWS, f"结果不符: 期望 {EXPECTED_ROWS}, 实际 {rows}"

    print("All tests passed.")


if __name__ == "__main__":
    main()
