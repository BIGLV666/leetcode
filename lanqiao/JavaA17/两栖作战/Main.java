package lanqiao.JavaA17.两栖作战;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
// 1:无需package
// 2: 类名必须Main, 不可修改

public class Main {
    public static void main(String[] args) throws IOException {
        InputStream in = System.in;
        byte[] ib = new byte[1 << 16];
        int ip = 0, il = 0, c;

        // ---- 读 N ----
        while (true) {
            if (ip == il) { il = in.read(ib); ip = 0; if (il <= 0) return; }
            c = ib[ip++] & 0xFF;
            if (c > ' ') break;
        }
        int n = c - '0';
        while (true) {
            if (ip == il) { il = in.read(ib); ip = 0; if (il <= 0) break; }
            c = ib[ip++] & 0xFF;
            if (c <= ' ') break;
            n = n * 10 + (c - '0');
        }

        // ---- 读 N 行 01 串,填进 (n+2)x(n+2) 的扁平网格,外圈填 2 作哨兵 ----
        int w = n + 2, total = w * w;
        byte[] g = new byte[total];
        Arrays.fill(g, (byte) 2);
        for (int r = 1; r <= n; r++) {
            int base = r * w + 1;
            for (int col = 0; col < n; col++) {
                while (true) {
                    if (ip == il) { il = in.read(ib); ip = 0; if (il <= 0) throw new IOException("EOF"); }
                    c = ib[ip++];
                    if (c > ' ') break;
                }
                g[base + col] = (byte) c;
            }
        }

        // ---- vis 的哨兵外圈预先置 1,这样访问四个邻居时无需做边界判断 ----
        byte[] vis = new byte[total];
        for (int k = 0; k < w; k++) {
            vis[k] = 1;
            vis[total - 1 - k] = 1;
            vis[k * w] = 1;
            vis[k * w + w - 1] = 1;
        }

        // ---- 分层 BFS:同值边权 0,异值边权 1 ----
        // 每层只处理一种地形的整块连通区域(闭包),其异值邻居就是下一层种子。
        // 每个格子只会入队一次,故 O(n^2);用一个 int[] 分成两段存放(闭包栈从前往后,
        // 下一层种子从后往前),避免装箱与额外内存。
        int start = w + 1, target = n * w + n;
        int[] buf = new int[total];
        vis[start] = 1;
        buf[0] = start;
        int sp = 1, qp = total, level = 0, ans = 0;
        FastWriter fw = new FastWriter();

        outer:
        while (true) {
            while (sp > 0) {
                int u = buf[--sp];
                if (u == target) { ans = level; break outer; }
                byte gu = g[u];
                int v = u + 1;
                if (vis[v] == 0) {
                    vis[v] = 1;
                    if (g[v] == gu) { buf[sp++] = v; }
                    else { if (v == target) { ans = level + 1; break outer; } buf[--qp] = v; }
                }
                v = u - 1;
                if (vis[v] == 0) {
                    vis[v] = 1;
                    if (g[v] == gu) { buf[sp++] = v; }
                    else { if (v == target) { ans = level + 1; break outer; } buf[--qp] = v; }
                }
                v = u + w;
                if (vis[v] == 0) {
                    vis[v] = 1;
                    if (g[v] == gu) { buf[sp++] = v; }
                    else { if (v == target) { ans = level + 1; break outer; } buf[--qp] = v; }
                }
                v = u - w;
                if (vis[v] == 0) {
                    vis[v] = 1;
                    if (g[v] == gu) { buf[sp++] = v; }
                    else { if (v == target) { ans = level + 1; break outer; } buf[--qp] = v; }
                }
            }
            if (qp == total) break;                 // 无路可走(网格必然连通,不会发生)
            int cnt = total - qp;
            for (int i = 0; i < cnt; i++) buf[i] = buf[total - 1 - i];
            sp = cnt;
            qp = total;
            level++;
        }
        fw.println(ans);
        fw.flush();
    }
}

class FastReader {
    private final InputStream in = System.in;
    private final byte[] buf = new byte[1 << 16]; // 64KB 缓冲
    private int len = 0, ptr = 0;

    /** 读一个字节，返回 -1 表示 EOF。 */
    private int read() throws IOException {
        if (ptr >= len) {
            len = in.read(buf);
            ptr = 0;
            if (len <= 0) return -1;
        }
        return buf[ptr++];
    }

    /** 读一个 int，自动跳过空白、支持负号。 */
    int nextInt() throws IOException {
        int c = read();
        while (c != -1 && c <= ' ') c = read();
        int sign = 1;
        if (c == '-') {
            sign = -1;
            c = read();
        }
        int val = 0;
        while (c > ' ') {                 // 数字字符都 > ' '
            val = val * 10 + (c - '0');
            c = read();
        }
        return val * sign;
    }

    /** 读一个 long，逻辑同 nextInt，只是把 int 换成 long。 */
    long nextLong() throws IOException {
        int c = read();
        while (c != -1 && c <= ' ') c = read();
        long sign = 1;
        if (c == '-') {
            sign = -1;
            c = read();
        }
        long val = 0;
        while (c > ' ') {
            val = val * 10 + (c - '0');
            c = read();
        }
        return val * sign;
    }

    /** 读一个以空白分隔的字符串（token）。 */
    String next() throws IOException {
        int c = read();
        while (c != -1 && c <= ' ') c = read();
        StringBuilder sb = new StringBuilder();
        while (c > ' ') {
            sb.append((char) c);
            c = read();
        }
        return sb.toString();
    }

    /** 读一整行（不含换行符），会跳过行首残留的换行。 */
    String nextLine() throws IOException {
        int c = read();
        while (c == '\r' || c == '\n') c = read();
        StringBuilder sb = new StringBuilder();
        while (c != -1 && c != '\n' && c != '\r') {
            sb.append((char) c);
            c = read();
        }
        return sb.toString();
    }
}

/** 快速输出:自带 64KB 缓冲,整数直接写字节,不走 PrintStream。 */
class FastWriter {
    private final java.io.OutputStream out = System.out;
    private final byte[] buf = new byte[1 << 16]; // 64KB 缓冲
    private final byte[] tmp = new byte[20];      // 整数转字节的临时区
    private int ptr = 0;

    private void raw(int b) throws IOException {
        if (ptr == buf.length) flush();
        buf[ptr++] = (byte) b;
    }

    void write(int x) throws IOException {
        if (x == 0) {
            raw('0');
            return;
        }
        long v = x;                    // 用 long 接住 Integer.MIN_VALUE
        if (v < 0) {
            raw('-');
            v = -v;
        }
        int p = 0;
        while (v > 0) {
            tmp[p++] = (byte) ('0' + v % 10);
            v /= 10;
        }
        while (p > 0) raw(tmp[--p]);
    }

    void write(long x) throws IOException {
        if (x == 0) {
            raw('0');
            return;
        }
        if (x < 0) {
            raw('-');
            int p = 0;
            while (x < 0) {                       // 用负数取模,规避 Long.MIN_VALUE 取反溢出
                tmp[p++] = (byte) ('0' - (x % 10));
                x /= 10;
            }
            while (p > 0) raw(tmp[--p]);
            return;
        }
        int p = 0;
        while (x > 0) {
            tmp[p++] = (byte) ('0' + x % 10);
            x /= 10;
        }
        while (p > 0) raw(tmp[--p]);
    }

    void write(char c) throws IOException {
        raw(c);
    }

    void write(String s) throws IOException {
        for (int i = 0; i < s.length(); i++) raw(s.charAt(i));
    }

    void println() throws IOException {
        raw('\n');
    }

    void println(int x) throws IOException {
        write(x);
        raw('\n');
    }

    void println(long x) throws IOException {
        write(x);
        raw('\n');
    }

    void println(String s) throws IOException {
        write(s);
        raw('\n');
    }

    void flush() throws IOException {
        if (ptr > 0) {
            out.write(buf, 0, ptr);
            ptr = 0;
        }
        out.flush();
    }
}
