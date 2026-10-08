package luogu.P1085;

import java.io.IOException;
import java.io.InputStream;

/**
 * 洛谷 P1085 [NOIP 2004 普及组] 不高兴的津津(Easy)
 *
 * 津津连续 7 天,每天上学 a 小时、课后上课 b 小时;一天总时长超过 8 小时就会不高兴。
 * 输出最不高兴的一天(总时长最长;并列取最靠前的一天),全程不超过 8 小时输出 0。
 * 一遍扫描:只在严格大于当前最大值时更新,天然保证并列取最早的一天。
 */
class Main{

    public  static void main(String[] args) throws IOException {
        FastReader in = new FastReader();
        int[] hours = new int[14];
        for (int i = 0; i < 14; i++) {
            hours[i] = in.nextInt();
        }
        System.out.println(solve(hours));
    }

    /** hours 为 7 天依次的 (a, b);返回最不高兴的天数(1..7),无不高兴日为 0。 */
    static int solve(int[] hours){
        int ans=0;
        int th1=0;
        for (int i = 1; i <= 7; i++) {
            int a = hours[(i - 1) * 2];
            int b = hours[(i - 1) * 2 + 1];
            if (a + b > 8) {
                if(a+b>th1){
                    th1=a+b;
                    ans=i;
                }
            }
        }
        return ans;
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
