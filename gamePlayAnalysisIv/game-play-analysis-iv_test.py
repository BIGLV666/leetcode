"""550. 游戏玩法分析 IV —— 用 sqlite3 实跑校验 .sql 解法(借助 python/sql_harness.py)。

校验对象:
  - game-play-analysis-iv.sql:主解法的两种写法;
  - game-play-analysis-iv-methods.sql:多解法合集(5 种方法 + 1 个对照写法)。
每个文件的每条语句都必须算出官方答案 0.33。

另外补两个极端数据集:全员次日回归 -> 1.0,无人次日回归 -> 0.0。

说明:题解里的日期加法是 MySQL 语法(DATE_ADD(x, INTERVAL 1 DAY) / x + INTERVAL 1 DAY),
sql_harness 会把它翻译成 sqlite3 等价的 date(x, '+1 day') 再执行。
"""

import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根

from python.sql_harness import run_sql_file

MAIN_SQL = Path(__file__).resolve().parent / "game-play-analysis-iv.sql"
METHODS_SQL = Path(__file__).resolve().parent / "game-play-analysis-iv-methods.sql"

SCHEMA = """
CREATE TABLE Activity (
    player_id INT,
    device_id INT,
    event_date DATE,
    games_played INT
);
"""

EXPECTED_COLUMNS = ["fraction"]
EXPECTED_VALUE = 0.33

# 官方示例:3 名玩家,只有玩家 1 在首次登录的次日又登录了 -> 1/3 = 0.33
OFFICIAL = {
    "Activity": [
        (1, 2, "2016-03-01", 5),
        (1, 2, "2016-03-02", 6),
        (2, 3, "2017-06-25", 1),
        (3, 1, "2016-03-02", 0),
        (3, 4, "2018-07-03", 5),
    ],
}

# 所有人首次登录的次日都回来了 -> 1.0
ALL_RETURN = {
    "Activity": [
        (1, 2, "2020-01-01", 1),
        (1, 2, "2020-01-02", 1),
        (2, 3, "2020-05-05", 1),
        (2, 3, "2020-05-06", 1),
    ],
}

# 没有人次日回来(两个玩家都只登录一天) -> 0.0
NONE_RETURN = {
    "Activity": [
        (1, 2, "2020-01-01", 1),
        (2, 3, "2020-01-03", 1),
    ],
}


def check_one(name: str, columns: list, rows: list) -> None:
    assert columns == EXPECTED_COLUMNS, f"{name}: 列名不符, 期望 {EXPECTED_COLUMNS}, 实际 {columns}"
    assert len(rows) == 1 and len(rows[0]) == 1, f"{name}: 应只返回 1 行 1 列, 实际 {rows}"
    got = rows[0][0]
    assert got is not None, f"{name}: 结果为 NULL"
    assert abs(got - EXPECTED_VALUE) < 1e-9, f"{name}: 期望 {EXPECTED_VALUE}, 实际 {got}"


def check_dataset(name: str, data: dict, expected: float) -> None:
    for label, path in (("主解法", MAIN_SQL), ("方法合集", METHODS_SQL)):
        results = run_sql_file(path, SCHEMA, data)
        assert results, f"{name}/{label}: 没有识别到任何语句"
        for idx, (columns, rows) in enumerate(results, start=1):
            assert columns == EXPECTED_COLUMNS, (
                f"{name}/{label} 第{idx}条: 列名不符, 实际 {columns}"
            )
            assert len(rows) == 1 and len(rows[0]) == 1, (
                f"{name}/{label} 第{idx}条: 应只返回 1 行 1 列, 实际 {rows}"
            )
            got = rows[0][0]
            assert got is not None, f"{name}/{label} 第{idx}条: 结果为 NULL"
            assert abs(got - expected) < 1e-9, (
                f"{name}/{label} 第{idx}条: 期望 {expected}, 实际 {got}"
            )


def main() -> None:
    # 官方示例
    check_dataset("官方示例", OFFICIAL, 0.33)
    # 极端情况
    check_dataset("全员次日回归", ALL_RETURN, 1.0)
    check_dataset("无人次日回归", NONE_RETURN, 0.0)

    print("All tests passed.")


if __name__ == "__main__":
    main()
