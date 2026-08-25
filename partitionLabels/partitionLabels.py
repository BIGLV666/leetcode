from typing import List
#title{763.划分字母区间}
#link{https://leetcode.cn/problems/partition-labels/description/?envType=study-plan-v2&envId=top-100-liked}
#
# 思路：区间合并
#   1) 遍历 s，记录每个字符第一次和最后一次出现的下标，得到区间 [first, last]
#   2) 按区间起点排序后，从左到右合并相交的区间（当前终点 maxval > 下一区间起点即为相交）
#   3) 不相交处即为一个片段边界，片段长度为 (maxval - top + 1)
#
# 时间复杂度：O(n)，一趟记录字符区间 + 一趟合并；排序可省（首次出现下标天然递增）
# 空间复杂度：O(|Σ|)，Σ 为字符集大小，最坏 O(n)

class Solution:
    def partitionLabels(self, s: str) -> List[int]:
        table={}
        for i in range(0,len(s)):
            ch=s[i]
            if ch in table:
                table[ch]=[table[ch][0],i]
            else:
                table[ch]=[i]
        temp=list(table.values())
        res=[]
        i=0
        while i<len(temp):
            top=temp[i][0]
            maxval=temp[i][-1]
            b=True
            while i<len(temp) and maxval>temp[i][0]: 
                b=False
                maxval=max(maxval,temp[i][-1])
                i+=1
            res.append(maxval-top+1)
            if b:
                i+=1
        return res
