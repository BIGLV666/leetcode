package 牛客.周赛.小月的长廊;

import java.io.IOException;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader();
        FastWriter out = new FastWriter();

        int n = in.nextInt();
        int p = in.nextInt();

        int[] left = new int[n];
        int[] right = new int[n];
        int[] all = new int[2 * n];
        for (int i = 0; i < n; i++) {
            left[i] = in.nextInt();
            right[i] = in.nextInt();
            all[2 * i] = left[i];
            all[2 * i + 1] = right[i];
        }

        // 坐标离散化:把所有端点排序去重,得到 m 个互不相同的坐标
        java.util.Arrays.sort(all);
        int m = 0;
        for (int i = 0; i < all.length; i++) {
            if (i == 0 || all[i] != all[m - 1]) all[m++] = all[i];
        }

        // 每个结界 [l, r] 覆盖其压缩下标区间,整段 +1;之后单点查询即得 d(x)
        SegmentTree st = new SegmentTree(m);
        for (int i = 0; i < n; i++) {
            st.rangeAdd(lowerBound(all, m, left[i]), lowerBound(all, m, right[i]), 1);
        }

        int ans = 0;
        for (int i = 0; i < n; i++) {
            int dl = st.pointQuery(lowerBound(all, m, left[i]));
            int dr = st.pointQuery(lowerBound(all, m, right[i]));
            if (Math.abs(dl - dr) >= p) ans++;
        }

        out.println(ans);
        out.flush();
    }

    /** 在升序去重数组 a[0..m) 中找第一个 >= key 的下标(key 一定存在于数组中)。 */
    private static int lowerBound(int[] a, int m, int key) {
        int lo = 0, hi = m;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] < key) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }
}

/** 线段树:区间加、单点查询。tree[node] 记录该节点整段累积的懒标记。 */
class SegmentTree {
    private final int n;
    private final int[] tree;

    SegmentTree(int n) {
        this.n = n;
        this.tree = new int[4 * Math.max(1, n)];
    }

    void rangeAdd(int l, int r, int v) {
        add(1, 0, n - 1, l, r, v);
    }

    int pointQuery(int pos) {
        // 从根走到叶子,累加沿途所有节点上的标记
        int node = 1, lo = 0, hi = n - 1, sum = 0;
        while (true) {
            sum += tree[node];
            if (lo == hi) break;
            int mid = (lo + hi) >>> 1;
            if (pos <= mid) {
                node = node << 1;
                hi = mid;
            } else {
                node = node << 1 | 1;
                lo = mid + 1;
            }
        }
        return sum;
    }

    private void add(int node, int lo, int hi, int l, int r, int v) {
        if (r < lo || hi < l) return;
        if (l <= lo && hi <= r) {
            tree[node] += v;
            return;
        }
        int mid = (lo + hi) >>> 1;
        add(node << 1, lo, mid, l, r, v);
        add(node << 1 | 1, mid + 1, hi, l, r, v);
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
