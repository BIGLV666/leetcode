import random
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根,便于导入本包

from numberOfSets import Solution

MOD = 10 ** 9 + 7


def brute(n: int, k: int) -> int:
    """独立参考实现:枚举所有 k 条互不重叠(端点相接允许)的线段组合。"""
    intervals = [(l, r) for l in range(n) for r in range(l + 1, n)]
    intervals.sort()  # 按左端点排序,便于保证"与上一段不重叠"只需看 l 与 last_r
    count = 0

    def rec(idx: int, chosen: int, last_r: int) -> None:
        nonlocal count
        if chosen == k:
            count += 1
            return
        for i in range(idx, len(intervals)):
            l, r = intervals[i]
            if l < last_r:  # 与上一段内部重叠;l == last_r 表示端点相接,允许
                continue
            rec(i + 1, chosen + 1, r)

    rec(0, 0, 0)
    return count


def binom_mod(N: int, K: int) -> int:
    """独立参考实现:帕斯卡三角递推求 C(N, K) mod 1e9+7(与 math.comb 不同实现)。"""
    if K < 0 or K > N:
        return 0
    row = [1]
    for i in range(1, N + 1):
        nxt = [1] * (i + 1)
        for j in range(1, i):
            nxt[j] = (row[j - 1] + row[j]) % MOD
        row = nxt
    return row[K] % MOD


def main() -> None:
    s = Solution()

    # 力扣官方示例(已按组合式 C(n+k-1, 2k) 手工核对)
    assert s.numberOfSets(4, 2) == 5
    assert s.numberOfSets(3, 1) == 3
    assert s.numberOfSets(5, 3) == 7

    # 小规模:与暴力枚举互验(同时覆盖 k 过大导致 0 的情况)
    for n in range(1, 9):
        for k in range(1, min(4, n) + 1):
            got = s.numberOfSets(n, k)
            want = brute(n, k) % MOD
            assert got == want, f"n={n}, k={k}: expected {want}, got {got}"

    # 较大规模:与帕斯卡三角模运算互验(校验闭式解与取模)
    random.seed(42)
    for _ in range(500):
        n = random.randint(1, 60)
        k = random.randint(1, 20)
        got = s.numberOfSets(n, k)
        want = binom_mod(n + k - 1, 2 * k)
        assert got == want, f"n={n}, k={k}: expected {want}, got {got}"

    print("All tests passed.")


if __name__ == "__main__":
    main()
