class Solution:
    """686. 重复叠加字符串匹配

    求最小的重复次数 k,使 b 成为 a 重复 k 次后的子串;不存在返回 -1。
    """

    def repeatedStringMatch(self, a: str, b: str) -> int:
        # 关键上界:若 b 是某个 a^k 的子串,则 b 的起点落在某个 a 的拷贝内,
        # 所需拷贝数不超过 ceil(len(b)/len(a)) + 2(首尾各至多再补一份)。
        # 总长度上界取 2*(len(a)+len(b)) 是覆盖上述上界的宽松安全值。
        res = 1
        maxLen = 2 * (len(a) + len(b))
        s = a
        while len(s) < maxLen:
            if b in s:
                return res  # 先判后加:当前 s = a^res 已包含 b
            s += a
            res += 1
        return -1


if __name__ == "__main__":
    s = Solution()
    assert s.repeatedStringMatch("abcd", "cdabcdab") == 3  # 官方示例:第 3 遍才包含
    assert s.repeatedStringMatch("a", "aa") == 2           # 官方示例
    assert s.repeatedStringMatch("a", "a") == 1            # 官方示例:b 本身就是 a
    assert s.repeatedStringMatch("abc", "wxyz") == -1      # 官方示例:字符集不同
    assert s.repeatedStringMatch("abcd", "d") == 1         # b 是 a 的尾部
    assert s.repeatedStringMatch("abcd", "cdab") == 2      # b 跨两个拷贝的边界
    assert s.repeatedStringMatch("abab", "ba") == 1        # b 取自 a 内部偏移
    assert s.repeatedStringMatch("ababab", "ababababab") == 2  # b 跨多个拷贝的长串

    # 随机对拍:与"复制足够多遍 + find 起点枚举"的参考实现互验
    # 参考实现按理论答案枚举 k(1..len(b)//len(a)+2),不依赖固定长度上界
    import random
    import string

    random.seed(42)
    for _ in range(2000):
        a = "".join(random.choice("ab") for _ in range(random.randint(1, 4)))
        # 50% 概率从 a 的某个循环移位片段生成 b(保证大概率有解),否则纯随机
        if random.random() < 0.5:
            shift = random.randint(0, len(a) - 1)
            rotated = a[shift:] + a[:shift]
            b = (rotated * (random.randint(1, 3) + 1))[random.randint(0, len(a) - 1):]
            b = b[:random.randint(1, len(a) * 2)]
        else:
            b = "".join(random.choice("ab") for _ in range(random.randint(1, 6)))
        # 参考实现
        base = a * (len(b) // len(a) + 2)
        ref = -1
        for k in range(1, len(base) // len(a) + 1):
            if b in a * k:
                ref = k
                break
        assert s.repeatedStringMatch(a, b) == ref, f"a={a!r}, b={b!r}"
    print("All tests passed.")
