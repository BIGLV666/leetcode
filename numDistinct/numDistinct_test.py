import random
import string
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根

from numDistinct import Solution


def reference(s: str, t: str) -> int:
    """独立参考实现:自底向上 DP 表,与题解的记忆化搜索是两条路径。

    dp[i][j] = t[j:] 在 s[i:] 中作为子序列的出现次数。
    """
    n, m = len(s), len(t)
    dp = [[0] * (m + 1) for _ in range(n + 1)]
    for i in range(n + 1):
        dp[i][m] = 1
    for i in range(n - 1, -1, -1):
        for j in range(m - 1, -1, -1):
            dp[i][j] = dp[i + 1][j]
            if s[i] == t[j]:
                dp[i][j] += dp[i + 1][j + 1]
    return dp[0][0]


if __name__ == "__main__":
    s = Solution()

    def check(s_: str, t_: str, expected: int) -> None:
        got = s.numDistinct(s_, t_)
        assert got == expected, f"numDistinct({s_!r}, {t_!r}): expected {expected}, got {got}"
        assert got == reference(s_, t_), f"reference mismatch for ({s_!r}, {t_!r})"

    # 力扣官方示例
    check("rabbbit", "rabbit", 3)
    check("babgbag", "bag", 5)

    # 边界
    check("a", "a", 1)
    check("a", "b", 0)
    check("abc", "", 1)      # t 为空:空串是任何串的子序列
    check("", "a", 0)
    check("", "", 1)
    check("aaa", "a", 3)
    check("aaa", "aa", 3)
    check("aaaa", "aaa", 4)  # C(4,3)

    # 与参考实现对拍(小字母表随机串)
    rng = random.Random(115)
    for _ in range(300):
        s_ = "".join(rng.choice("abc") for _ in range(rng.randint(0, 12)))
        t_ = "".join(rng.choice("abc") for _ in range(rng.randint(0, 4)))
        got = s.numDistinct(s_, t_)
        ref = reference(s_, t_)
        assert got == ref, f"numDistinct({s_!r}, {t_!r}): expected {ref}, got {got}"

    # 大字母表随机串
    for _ in range(100):
        s_ = "".join(rng.choice(string.ascii_lowercase) for _ in range(rng.randint(1, 30)))
        t_ = "".join(rng.choice(s_) for _ in range(rng.randint(1, 5)))
        got = s.numDistinct(s_, t_)
        ref = reference(s_, t_)
        assert got == ref, f"numDistinct({s_!r}, {t_!r}): expected {ref}, got {got}"

    print("numDistinct_test: all passed")
