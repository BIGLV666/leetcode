import sys
from array import array


def main():
    data = sys.stdin.buffer.read()
    L = len(data)
    i = 0
    while i < L and data[i] <= 32:          # 跳过空白找到 N
        i += 1
    n = 0
    while i < L and data[i] > 32:
        n = n * 10 + (data[i] - 48)
        i += 1
    # 剩下部分删掉所有空白,得到 n*n 个 '0'/'1'
    # (这样 "01100" 和 "0 1 1 0 0" 两种写法都能读)
    digits = data[i:].translate(None, b' \t\n\r\x0b\x0c')

    # 网格补一圈哨兵 2 后拍平成一维,访问四邻居时不用判边界
    w = n + 2
    total = w * w
    edge = b'\x02' * w
    g = b''.join([edge]
                 + [b'\x02' + digits[r * n:(r + 1) * n] + b'\x02' for r in range(n)]
                 + [edge])
    del digits

    # vis 的哨兵外圈预先置 1
    vis = bytearray(b''.join([b'\x01' * w]
                             + [b'\x01' + b'\x00' * n + b'\x01' for _ in range(n)]
                             + [b'\x01' * w]))

    # 分层 BFS:同值边权 0(并入本层闭包),异值边权 1(作为下一层种子)。
    # 每个格子只入队一次,复杂度 O(n^2)。用一个 array('i') 分两段存:
    # 前段是当前层闭包的栈(自增),后段是下一层的种子(自减),省内存也省一次拷贝。
    buf = array('i', [0]) * total
    start = w + 1
    target = n * w + n
    vis[start] = 1
    buf[0] = start
    sp = 1                  # 闭包栈的栈顶
    qp = total              # 种子区从末尾往前写
    level = 0
    while True:
        while sp:
            sp -= 1
            u = buf[sp]
            if u == target:
                sys.stdout.write(str(level) + "\n")
                return
            gu = g[u]
            v = u + 1
            if not vis[v]:
                vis[v] = 1
                if g[v] == gu:
                    buf[sp] = v
                    sp += 1
                else:
                    if v == target:
                        sys.stdout.write(str(level + 1) + "\n")
                        return
                    qp -= 1
                    buf[qp] = v
            v = u - 1
            if not vis[v]:
                vis[v] = 1
                if g[v] == gu:
                    buf[sp] = v
                    sp += 1
                else:
                    if v == target:
                        sys.stdout.write(str(level + 1) + "\n")
                        return
                    qp -= 1
                    buf[qp] = v
            v = u + w
            if not vis[v]:
                vis[v] = 1
                if g[v] == gu:
                    buf[sp] = v
                    sp += 1
                else:
                    if v == target:
                        sys.stdout.write(str(level + 1) + "\n")
                        return
                    qp -= 1
                    buf[qp] = v
            v = u - w
            if not vis[v]:
                vis[v] = 1
                if g[v] == gu:
                    buf[sp] = v
                    sp += 1
                else:
                    if v == target:
                        sys.stdout.write(str(level + 1) + "\n")
                        return
                    qp -= 1
                    buf[qp] = v
        if qp == total:                     # 网格必然连通,正常不会走到这里
            break
        cnt = total - qp
        buf[:cnt] = buf[total - cnt:total]  # 把种子搬到前段,作为下一层闭包
        sp = cnt
        qp = total
        level += 1
    sys.stdout.write(str(level) + "\n")


main()
