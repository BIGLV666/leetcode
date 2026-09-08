import heapq
import random
import sys
from collections import Counter
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))  # 仓库根,便于导入本包

from reorganizeString import Solution


def reference(s: str) -> str:
    """独立参考实现:贪心 + 大顶堆(每次取与上一个不同的最多字符)。"""
    if not s:
        return ""
    if max(Counter(s).values()) > (len(s) + 1) // 2:
        return ""
    heap = [(-cnt, ch) for ch, cnt in Counter(s).items()]
    heapq.heapify(heap)
    res = []
    prev_cnt, prev_ch = 0, ""
    while heap:
        cnt, ch = heapq.heappop(heap)
        res.append(ch)
        if prev_cnt < 0:  # 上一个字符还有剩余,塞回堆
            heapq.heappush(heap, (prev_cnt, prev_ch))
        prev_cnt, prev_ch = cnt + 1, ch  # 已用掉一个
    return "".join(res)


if __name__ == "__main__":
    s = Solution()

    def check(inp, expected):
        got = s.reorganizeString(inp)
        assert got == expected, f"{inp}: expected {expected!r}, got {got!r}"

    # 官方示例
    check("aab", "aba")
    check("aaab", "")

    # 边界:可行性由"最高频字符数 <= (n+1)//2"决定
    check("a", "a")            # 单字符
    check("aa", "")            # 两个相同,无解
    check("ab", "ab")          # 各一个
    check("aabb", "abab")      # 恰好可交错
    check("aaabbc", "ababac")  # a:3,b:2,c:1 → 3 <= 3 可行
    check("aaab", "")          # a:3 > 2 无解
    check("vvvlo", "vlvov")    # v:3,l:1,o:1 → 3 <= 3

    # 随机对拍:本题答案不唯一,不能逐字符比较。
    # 校验三件事:①可行性必须与堆贪心一致;②结果与原串字符计数完全相同;③无相邻重复
    random.seed(42)
    for _ in range(2000):
        n = random.randint(1, 10)
        word = "".join(random.choice("abc") for _ in range(n))
        got = s.reorganizeString(word)
        ref = reference(word)
        assert (got == "") == (ref == ""), f"{word}: 可行性不一致 got={got!r} ref={ref!r}"
        if got:
            assert len(got) == len(word) and Counter(got) == Counter(word), word
            assert all(got[i] != got[i - 1] for i in range(1, len(got))), f"{word}: got {got!r}"
    print("All tests passed.")