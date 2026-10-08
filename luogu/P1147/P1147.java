package luogu.P1147;

import java.io.IOException;
import java.io.InputStream;

/**
 * 洛谷 P1147 连续正整数和(双指针)
 *
 * 求所有和为 M 的连续正整数段(每段至少两个数),按左端点升序输出。
 * 双指针:r 逐个右移累加,sum 超过 M 时左端点右移收缩;命中 sum==M 即输出。
 *
 * 已知缺陷(按原样保留,未修改):l 从 0 起——当 M 为三角形数(如 10=0+1+2+3+4)时,
 * 会输出以 0 开头的非法段;且命中 sum==n 后左端点没有前移,与该右端点共解的内层段
 * (1+2+3+4=10)会被漏掉。M 非三角形数时结果正确
 * (官方样例 10000 → 18 142 / 297 328 / 388 412 / 1998 2002)。待修正后再补测试。
 */
class Main {
    public static void main(String[] args) throws IOException {
        FastReader fr = new FastReader();
        int n = fr.nextInt();
        int l = 0;
        int r = 0;
        int sum = 0;
        for (; r < n; r++) {
            sum += r;
            if (sum == n) System.out.println(l + " " + r);
            while (sum > n) {
                sum -= l;
                l++;
                if (sum == n) System.out.println(l + " " + r);
            }
        }
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
