class Solution:
    def lastSubstring(self, s: str) -> str:
        # 双指针比较候选后缀，均摊 O(n)；答案一定以最大字符开头
        i, j, k = 0, 1, 0
        n = len(s)
        while j + k < n:
            if s[i + k] == s[j + k]:
                k += 1
            elif s[i + k] < s[j + k]:
                i = max(i + k + 1, j)
                j = i + 1
                k = 0
            else:
                j = j + k + 1
                k = 0
        return s[i:]
