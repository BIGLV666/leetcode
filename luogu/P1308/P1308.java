package luogu.P1308;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 洛谷 P1308 [NOIP 2011 普及组] 统计单词数(Easy)
 *
 * 给定一个单词(不区分大小写)和一行文章,输出「该单词在文章中出现的总次数 +
 * 首次出现的下标(0 起)」;未出现输出 -1。
 * 原目录编号 P1038(神经网络)与代码实际语义不符,已按实际解法迁移至 P1308。
 *
 * 已知缺陷(按原样保留,未修改):首位置用「文章是否以 key 开头」判定,
 * 当文章首单词以 key 为前缀但不等于 key(如 key=to、首词 total)时会误报下标 0。
 */
class Main{

    public  static void main(String[] args) throws IOException {
        FastReader f = new FastReader();
        String key=f.next().toLowerCase();
        String s1=f.nextLine().toLowerCase();
        String[]arr= s1.split(" ");
        int count=0;
        int ans;
        for (String s :arr) {
            if (key.equals(s)) count++;
        }

        if (count == 0) {
            System.out.println(-1);
        } else {
            if (key.equals(s1.substring(0, key.length()))) {
                ans=0;
            } else
                ans = s1.indexOf(" " + key + " ") + 1;
            System.out.println(count + " " + ans);
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