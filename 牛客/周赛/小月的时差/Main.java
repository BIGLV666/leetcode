package 牛客.周赛.小月的时差;

import java.io.IOException;
import java.io.InputStream;

/**
 * 小月的时差
 *
 * 结点 v 的时差 diff[v] = |dist(a,v) - dist(b,v)|(两次 BFS)。
 * 路径的时差 = 路径上结点时差的最大值,要求最小化,即“最小瓶颈路”问题。
 * 对阈值 X,只保留 diff<=X 的结点,若 s、t 仍连通则存在一条满足条件的简单路径
 * (连通必含简单路径)。于是把结点按 diff 升序加入并查集,首次令 s、t 连通时的
 * diff 即为答案。
 */
public class Main {

    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader();
        FastWriter out = new FastWriter();

        int n = in.nextInt();
        int m = in.nextInt();
        int a = in.nextInt();
        int b = in.nextInt();
        int s = in.nextInt();
        int t = in.nextInt();

        // 链式前向星存无向图,每条边存两个方向
        int[] head = new int[n + 1];
        java.util.Arrays.fill(head, -1);
        int[] to = new int[2 * m];
        int[] nxt = new int[2 * m];
        int ec = 0;
        for (int i = 0; i < m; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            to[ec] = v; nxt[ec] = head[u]; head[u] = ec++;
            to[ec] = u; nxt[ec] = head[v]; head[v] = ec++;
        }

        int[] da = bfs(a, head, to, nxt);
        int[] db = bfs(b, head, to, nxt);

        int[] diff = new int[n + 1];
        for (int v = 1; v <= n; v++) diff[v] = Math.abs(da[v] - db[v]);

        // s==t 时路径只含该结点,时差就是它自己的 diff
        if (s == t) {
            out.println(diff[s]);
            out.flush();
            return;
        }

        // 计数排序:diff 取值在 [0, n-1],按 diff 升序得到结点顺序
        int[] cnt = new int[n + 1];
        for (int v = 1; v <= n; v++) cnt[diff[v]]++;
        int[] start = new int[n + 3];
        for (int i = 0; i < n; i++) start[i + 1] = start[i] + cnt[i];
        int[] pos = start.clone();
        int[] order = new int[n];
        for (int v = 1; v <= n; v++) order[pos[diff[v]]++] = v;

        // 并查集,按 diff 渐进加入结点并与已加入的邻居合并
        int[] dsu = new int[n + 1];
        int[] size = new int[n + 1];
        for (int v = 1; v <= n; v++) {
            dsu[v] = v;
            size[v] = 1;
        }
        boolean[] added = new boolean[n + 1];

        for (int k = 0; k < n; k++) {
            int v = order[k];
            added[v] = true;
            for (int e = head[v]; e != -1; e = nxt[e]) {
                int u = to[e];
                if (added[u]) union(dsu, size, v, u);
            }
            // 首次连通即为最小瓶颈值
            if (find(dsu, s) == find(dsu, t)) {
                out.println(diff[v]);
                out.flush();
                return;
            }
        }
    }

    /** 从 src 出发的单源最短路(BFS),返回距离数组,未到达为 -1(图连通不会出现)。 */
    private static int[] bfs(int src, int[] head, int[] to, int[] nxt) {
        int n = head.length - 1;
        int[] dist = new int[n + 1];
        java.util.Arrays.fill(dist, -1);
        int[] q = new int[n];
        int qh = 0, qt = 0;
        dist[src] = 0;
        q[qt++] = src;
        while (qh < qt) {
            int u = q[qh++];
            for (int e = head[u]; e != -1; e = nxt[e]) {
                int w = to[e];
                if (dist[w] == -1) {
                    dist[w] = dist[u] + 1;
                    q[qt++] = w;
                }
            }
        }
        return dist;
    }

    private static int find(int[] dsu, int x) {
        while (dsu[x] != x) {
            dsu[x] = dsu[dsu[x]]; // 路径折半压缩
            x = dsu[x];
        }
        return x;
    }

    private static void union(int[] dsu, int[] size, int x, int y) {
        int rx = find(dsu, x);
        int ry = find(dsu, y);
        if (rx == ry) return;
        if (size[rx] < size[ry]) {
            int tmp = rx;
            rx = ry;
            ry = tmp;
        }
        dsu[ry] = rx;
        size[rx] += size[ry];
    }
}
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
