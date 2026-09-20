"""511. 游戏玩法分析 I —— 用 sqlite3 实跑校验 .sql 解法(借助 python/sql_harness.py)。"""

import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根

from python.sql_harness import run_sql_file

SQL_FILE = Path(__file__).resolve().parent / "game-play-analysis-i.sql"

SCHEMA = """
CREATE TABLE Activity (
    player_id INT,
    device_id INT,
    event_date DATE,
    games_played INT
);
"""

# 官方示例数据:每个玩家取最早的一次登录日期
DATA = {
    "Activity": [
        (1, 2, "2016-03-01", 5),
        (1, 2, "2016-03-02", 6),
        (2, 3, "2017-06-25", 1),
        (3, 1, "2016-03-02", 0),
        (3, 4, "2018-07-03", 5),
    ],
}

EXPECTED_COLUMNS = ["player_id", "first_login"]
EXPECTED_ROWS = [(1, "2016-03-01"), (2, "2017-06-25"), (3, "2016-03-02")]


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
