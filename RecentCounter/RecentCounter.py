from collections import deque


class RecentCounter:
    """933. 最近的请求次数

    ping(t) 在时刻 t 增加一个请求(t 严格递增),返回 [t-3000, t] 内的请求数(含两端)。
    解法:单调队列。t 递增保证队列中时间戳有序,每次 ping 先入队,
    再从队首弹出所有 < t-3000 的过期请求;队内剩余个数即答案。

    复杂度:每个请求至多入队/出队一次,均摊 O(1);空间 O(W),W 为 3000ms 内的请求数。
    """

    def __init__(self):
        self.recent_counter = deque()

    def ping(self, t: int) -> int:
        self.recent_counter.append(t)
        low = t - 3000  # 有效下界:时间戳 >= low 的请求都算在内(含恰好等于 low 的)
        while self.recent_counter[0] < low:
            self.recent_counter.popleft()  # 队首已过期,永久弹出
        return len(self.recent_counter)


# Your RecentCounter object will be instantiated and called as such:
# obj = RecentCounter()
# param_1 = obj.ping(t)

if __name__ == "__main__":
    # 官方示例
    q = RecentCounter()
    assert q.ping(1) == 1
    assert q.ping(100) == 2
    assert q.ping(3001) == 3   # low=1:时刻 1 恰在下界上,保留(闭区间)
    assert q.ping(3002) == 3   # low=2:时刻 1 过期弹出,剩 100/3001/3002

    # 边界:恰在下界 → 保留
    q2 = RecentCounter()
    assert q2.ping(1) == 1
    assert q2.ping(3001) == 2  # low=1,时刻 1 恰好不过期

    # 边界:恰好过期(low 比最后请求大 1)
    q3 = RecentCounter()
    assert q3.ping(10000) == 1
    assert q3.ping(13001) == 1  # low=10001,时刻 10000 过期

    # 边界:长间隔清空
    q4 = RecentCounter()
    assert q4.ping(10) == 1
    assert q4.ping(5000) == 1  # low=2000,10 已过期

    # 随机对拍:与每次线性扫描的暴力参考实现互验(t 严格递增)
    import random

    random.seed(42)
    for _ in range(500):
        brute = []
        q = RecentCounter()
        t = 1
        for _ in range(50):
            t += random.randint(0, 800)
            brute.append(t)  # 先入 brute(与 ping 的入队一致)
            expected = sum(1 for x in brute if t - 3000 <= x <= t)
            assert q.ping(t) == expected, f"t={t}"
    print("All tests passed.")
