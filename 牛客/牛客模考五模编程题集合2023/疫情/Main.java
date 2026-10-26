package 牛客.牛客模考五模编程题集合2023.疫情;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    static int n;
    static int[] a;
    // 邻接表: 每条无向边只存一次 (u -> v), 这样 check 时每条边只数一遍
    static Map<Integer, List<Integer>> table = new HashMap<>();

    public static void main(String[] args) throws IOException {
        FastReader fastReader = new FastReader();
        FastWriter fastWriter = new FastWriter();
        n = fastReader.nextInt();
        int m = fastReader.nextInt();
        a = new int[n];
        int maxA = 0;
        for (int i = 0; i < n; i++) {
            a[i] = fastReader.nextInt();
            maxA = Math.max(maxA, a[i]);
        }
        for (int i = 0; i < n - 1; i++) {
            int u = fastReader.nextInt() - 1;
            int v = fastReader.nextInt() - 1;
            table.computeIfAbsent(u, key -> new ArrayList<>()).add(v);
        }

        // h 足够小(<=min a)时全部封城,连通块数 = n;若 m >= n 则 h 可以无限小
        if (m >= n) {
            fastWriter.println(-1);
            fastWriter.flush();
            return;
        }

        // f(h)=连通块数 随 h 增大单调不增,二分最小满足 f(h) <= m 的 h
        long lo = 1, hi = (long) maxA + 1; // h=maxA+1 时无人封城,f=1 <= m
        while (lo < hi) {
            long mid = (lo + hi) >>> 1;
            if (components(mid) <= m) hi = mid;
            else lo = mid + 1;
        }
        fastWriter.println(lo);
        fastWriter.flush();
    }

    /**
     * 封城所有 a[i] >= h 的城市。原图是树,只保留两端都未封城的边后仍然是森林。
     * 森林中: 连通块数 = 点数 - 边数。点数恒为 n(封城城市各自成块),所以
     * f(h) = n - (两端密度都 < h 的边数)。
     */
    static int components(long h) {
        int comp = n;
        for (Map.Entry<Integer, List<Integer>> entry : table.entrySet()) {
            int u = entry.getKey();
            if (a[u] >= h) continue;            // u 被封城,这条边不可能连通任何东西
            for (int v : entry.getValue()) {
                if (a[v] < h) comp--;           // 两端都未封城,边数 +1
            }
        }
        return comp;
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
