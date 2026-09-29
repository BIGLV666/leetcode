import random
import string
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根

from isInterleave import Solution


def reference(s1: str, s2: str, s3: str) -> bool:
    """独立参考实现:自底向上填 DP 表。

    dp[i][j] = s1[:i] 与 s2[:j] 能否交错拼出 s3[:i+j]。
    与题解的记忆化 DFS 是两条独立路径。
    """
    n, m = len(s1), len(s2)
    if n + m != len(s3):
        return False
    dp = [[False] * (m + 1) for _ in range(n + 1)]
    dp[0][0] = True
    for i in range(n + 1):
        for j in range(m + 1):
            if i > 0 and s1[i - 1] == s3[i + j - 1]:
                dp[i][j] = dp[i][j] or dp[i - 1][j]
            if j > 0 and s2[j - 1] == s3[i + j - 1]:
                dp[i][j] = dp[i][j] or dp[i][j - 1]
    return dp[n][m]


if __name__ == "__main__":
    s = Solution()

    def check(s1: str, s2: str, s3: str, expected: bool | None = None) -> None:
        got = s.isInterleave(s1, s2, s3)
        if expected is not None:
            assert got == expected, f"isInterleave({s1!r},{s2!r},{s3!r}): expected {expected}, got {got}"
        ref = reference(s1, s2, s3)
        assert got == ref, f"isInterleave({s1!r},{s2!r},{s3!r}): got {got}, 参考实现 = {ref}"

    # 力扣官方示例
    check("aabcc", "dbbca", "aadbbcbcac", True)
    check("aabcc", "dbbca", "aadbbbaccc", False)
    check("", "", "", True)

    # 边界
    check("a", "b", "ab", True)
    check("a", "b", "ba", True)
    check("a", "b", "aa", False)
    check("", "abc", "abc", True)
    check("abc", "", "abc", True)
    check("", "abc", "abd", False)
    check("abc", "def", "abcdefg", False)   # 长度对不上
    check("aaaa", "aaaa", "aaaaaaaa", True) # 全同字符

    # 与参考实现对拍(小字母表随机串)
    rng = random.Random(97)
    for _ in range(300):
        n1 = rng.randint(0, 8)
        n2 = rng.randint(0, 8)
        s1 = "".join(rng.choice("ab") for _ in range(n1))
        s2 = "".join(rng.choice("ab") for _ in range(n2))
        if rng.random() < 0.5:
            # 拼一个合法 s3:交错合并 s1、s2
            i1 = i2 = 0
            parts = []
            while i1 < n1 or i2 < n2:
                if i1 < n1 and (i2 >= n2 or rng.random() < 0.5):
                    parts.append(s1[i1]); i1 += 1
                else:
                    parts.append(s2[i2]); i2 += 1
            s3 = "".join(parts)
        else:
            s3 = "".join(rng.choice("ab") for _ in range(n1 + n2 + rng.choice((-1, 0, 1))))
        check(s1, s2, s3)

    # 稍大字母表的随机串
    for _ in range(100):
        n1 = rng.randint(1, 10)
        n2 = rng.randint(1, 10)
        s1 = "".join(rng.choice(string.ascii_lowercase) for _ in range(n1))
        s2 = "".join(rng.choice(string.ascii_lowercase) for _ in range(n2))
        s3 = "".join(rng.choice(string.ascii_lowercase) for _ in range(n1 + n2))
        check(s1, s2, s3)

    print("isInterleave_test: all passed")