class Solution:
    """1859. 整理句子

    句子由若干单词组成,每个单词末尾跟着它应有的位置数字(1 开始)。
    按位置数字把单词排回原顺序,去掉数字后用单空格连接返回。
    题目约束:单词数不超过 9,即位置数字为单个 1..9(word[-1] 必为一位数字)。
    """

    def sortSentence(self, s: str) -> str:
        words = s.split()
        # 位置数字恰好覆盖 1..len(words),直接按"桶下标"放置,无需排序 O(n)
        res = ["" for _ in range(len(words))]
        for word in words:
            res[int(word[-1]) - 1] = word[:-1]  # 末位是位置数字,去掉后放入对应桶
        return " ".join(res)


if __name__ == "__main__":
    s = Solution()
    assert s.sortSentence("is2 sentence4 This1 a3") == "This is a sentence"  # 官方示例
    assert s.sortSentence("Myself2 Me1 I4 and3") == "Me Myself and I"        # 官方示例
    assert s.sortSentence("Hello1") == "Hello"          # 单单词,位置 1
    assert s.sortSentence("A1 B2 C3") == "A B C"        # 已有序
    assert s.sortSentence("C3 A1 B2") == "A B C"        # 完全逆序输入
    assert s.sortSentence("i9 a1 c3 b2 e5 d4 f6 g7 h8") == "a b c d e f g h i"  # 位置到 9

    # 随机对拍:与 sorted 按位置数字排序的参考实现互验(单词数 ≤ 9,符合题目约束)
    import random

    random.seed(42)
    for _ in range(2000):
        n = random.randint(1, 9)
        order = list(range(1, n + 1))
        random.shuffle(order)
        shuffled = [f"{chr(ord('a') + i)}{order[i]}" for i in range(n)]
        random.shuffle(shuffled)
        inp = " ".join(shuffled)
        expected = " ".join(w[:-1] for w in sorted(shuffled, key=lambda w: int(w[-1])))
        assert s.sortSentence(inp) == expected, inp
    print("All tests passed.")
