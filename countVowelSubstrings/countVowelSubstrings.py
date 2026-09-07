class Solution:
    """2062. 统计字符串中的元音子字符串数

    元音子字符串:仅由元音 a/e/i/o/u 组成、且五种元音全部出现的连续子串。
    返回 word 中元音子字符串的个数。
    """

    def countVowelSubstrings(self, word: str) -> int:
        n = len(word)
        res = 0
        vowelset = set("aeiou")   # 合法元音子字符串必须恰好等于该集合
        for i in range(n):
            # 枚举左端点
            charset = set()   # 当前子串 [i..j] 出现过的字符集合
            for j in range(i, n):
                # 右端点右移:加入新字符;一旦遇到辅音,charset 必不可能再等于 vowelset,
                # 但暴力法不做提前剪枝,继续扩大只是多比较、不影响正确性
                charset.add(word[j])
                if charset == vowelset:
                    res += 1  # [i..j] 恰好含全部五种元音且无辅音,计入答案
        return res


if __name__ == "__main__":
    s = Solution()
    assert s.countVowelSubstrings("aeiouu") == 2          # 官方示例:aeiou / aeiouu
    assert s.countVowelSubstrings("unicornarihan") == 0   # 无全部五种元音
    assert s.countVowelSubstrings("cuaieuouac") == 7      # 官方示例
    assert s.countVowelSubstrings("bbaeixoubb") == 0      # 混入辅音的都不算
    assert s.countVowelSubstrings("aeiou") == 1           # 恰好一个:整个串
    assert s.countVowelSubstrings("aeio") == 0            # 缺 u
    assert s.countVowelSubstrings("a") == 0               # 单字符不可能是元音子串
    assert s.countVowelSubstrings("xxaeiouxx") == 1       # 元音段夹在辅音之间

    # 随机对拍:与 O(n^3) 枚举子串逐一检查的暴力参考实现互验
    import random

    random.seed(42)
    for _ in range(2000):
        length = random.randint(1, 15)
        word = "".join(random.choice("aeioubcx") for _ in range(length))
        ref = sum(
            1
            for i in range(len(word))
            for j in range(i + 1, len(word) + 1)
            if set(word[i:j]) == set("aeiou")
        )
        assert s.countVowelSubstrings(word) == ref, word
    print("All tests passed.")

