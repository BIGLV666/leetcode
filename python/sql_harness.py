"""SQL 题的通用 sqlite3 测试工具。

LeetCode 的 SQL 题判题环境是 MySQL,而本地可用的自动化验证工具是 Python 标准库
sqlite3。本模块负责把两者接起来:

    - 在内存库中建表、灌入官方示例数据;
    - 读取题目目录下的 .sql,拆成一条条语句(跳过注释行与空语句);
    - 做一小步方言转换:MySQL 的 DATE_ADD(x, INTERVAL n DAY) 与
      x + INTERVAL n DAY 翻译成 sqlite3 的 date(x, '+n day'),
      让涉及日期运算的题(如 550)也能跑;
    - 逐条执行,返回 (列名列表, 结果行列表),由各题目的测试自行断言。

用法(见各 SQL 题目录下的 *_test.py):

    results = run_sql_file(Path(__file__).parent / "xxx.sql", SCHEMA, DATA)
    for columns, rows in results:
        assert columns == EXPECTED_COLUMNS
        assert sorted(rows, key=str) == sorted(EXPECTED_ROWS, key=str)
"""

import re
import sqlite3


def _strip_comments(sql: str) -> str:
    """去掉 `--` 行注释(整行或行尾均可)。"""
    cleaned = []
    for line in sql.splitlines():
        idx = line.find("--")
        if idx >= 0:
            line = line[:idx]
        cleaned.append(line)
    return "\n".join(cleaned)


def _mysql_date_to_sqlite(sql: str) -> str:
    """MySQL 的日期加法 -> sqlite3 的 date() 写法。

    DATE_ADD(expr, INTERVAL n DAY)  ->  date(expr, '+n day')
    expr + INTERVAL n DAY           ->  date(expr, '+n day')

    只处理列名/派生表列这类简单表达式(不含嵌套括号),题解里的用法均满足。
    """
    sql = re.sub(
        r"DATE_ADD\(([^()]+?),\s*INTERVAL\s+(\d+)\s+DAY\)",
        lambda m: f"date({m.group(1).strip()}, '+{m.group(2)} day')",
        sql,
        flags=re.IGNORECASE,
    )
    sql = re.sub(
        r"([A-Za-z0-9_.]+)\s*\+\s*INTERVAL\s+(\d+)\s+DAY\b",
        lambda m: f"date({m.group(1)}, '+{m.group(2)} day')",
        sql,
        flags=re.IGNORECASE,
    )
    return sql


def statements(sql_text: str) -> list[str]:
    """把 SQL 文本拆成单条可执行语句(按分号切分,过滤空语句)。"""
    body = _mysql_date_to_sqlite(_strip_comments(sql_text))
    parts = [p.strip() for p in body.split(";")]
    return [p for p in parts if p]


def run_sql_file(sql_path, setup_sql: str, rows_by_table: dict) -> list:
    """在内存库中建表、灌数据,执行 sql_path 里的每条语句。

    rows_by_table: {"表名": [(col1, col2, ...), ...]},按字典顺序灌数据。
    返回 [(列名列表, 结果行列表), ...],文件里有几条语句就有几项,
    便于同一文件里的多种写法(如 550 的多解法合集)逐一验证。
    """
    with open(sql_path, encoding="utf-8") as f:
        sql_text = f.read()

    conn = sqlite3.connect(":memory:")
    try:
        conn.executescript(setup_sql)
        for table, rows in rows_by_table.items():
            if not rows:
                continue
            placeholders = ", ".join("?" for _ in rows[0])
            conn.executemany(f"INSERT INTO {table} VALUES ({placeholders})", rows)

        results = []
        for stmt in statements(sql_text):
            cur = conn.execute(stmt)
            results.append(([d[0] for d in cur.description], cur.fetchall()))
        return results
    finally:
        conn.close()
