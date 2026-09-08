from typing import List


class Solution:
    """849. 到最近的人的最大距离

    座位表 seats(0 空 1 有人),选一个空位使得离最近的人最远,返回该最大距离。
    三类候选:两个 1 之间的 0 段(坐正中,长 d 贡献 d//2)、开头 0 段、末尾 0 段
    (坐端点,长 d 贡献 d —— 这是双扫描里最容易漏掉 //2 的地方)。

    解法:正向扫描结算「1 之间 + 末尾 0 段」,反向扫描结算「开头 0 段」。
    复杂度:时间 O(n),空间 O(1)。
    """

    def maxDistToClosest(self, seats: List[int]) -> int:
        left=0
        res=0
        for i in range(len(seats)):
            if seats[i]==1:
                if seats[left]==1:
                    res=max(res,(i-left)//2)  # 两 1 夹 0 段:坐正中,距离折半
                    left=i
                else:
                    left=i  # 从开头 0 段走到第一个 1:开头段留给反向扫描结算
        # 末尾 0 段:坐在最后一个空位,距离 = 整段长(端点不能 //2,否则 [1,0,0,0] 会错成 1)
        res=max(res,(len(seats)-1-left))
        left=len(seats)-1

        for i in range(len(seats)-1,-1,-1):
            if seats[i]==1:
                if seats[left]==1:
                    # 反向时 i<left,该项 <=0;中间段正向已结算,这里 max 无副作用
                    res=max(res,(i-left)//2)
                    left=i
                else:
                    left=i
        # 反向扫完 left = 最左侧 1 的下标 = 开头 0 段的长度(坐位置 0)
        res=max(res,left)
        return res