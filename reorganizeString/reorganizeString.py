import collections
from functools import cmp_to_key



class Solution:
    """767. 重构字符串

    重新排布 s 使相邻字符互不相同,返回任意可行排列;不可行返回 ""。
    思路:按出现次数【降序】排字符(次数最多的要摊得最开),先填偶数下标再填奇数下标;
    最后校验一遍,不可行自然会出现相邻相同。
    """

    def reorganizeString(self, s: str) -> str:
        table = collections.Counter(s)
        res = ["" for _ in range(len(s))]
        chars = [x for x in s]

        # cmp_to_key 自定义比较器:频次降序(多的先摊开),频次相同时按字符升序。
        # 返回负数表示 x 排在前;展开数组排序后天然"同字符成块、高频块在前"
        chars.sort(key=cmp_to_key(lambda x,y:table[y]-table[x] if table[x]!=table[y] else ord(x)-ord(y)))

        top = 0
        for i in range(2):  # 先填偶数下标(0,2,4...),再填奇数下标(1,3,5...)
            index = i
            while index < len(chars):
                res[index] = chars[top]
                top += 1
                index += 2

        # 若最高频字符超过 (n+1)//2,这里必然出现相邻相同 → 不可行返回 ""
        for i in range(1, len(res)):
            if res[i] == res[i - 1]:
                return ""
        return "".join(res)
