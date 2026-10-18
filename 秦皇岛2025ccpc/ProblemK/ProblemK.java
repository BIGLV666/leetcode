package 秦皇岛2025ccpc.ProblemK;

import java.io.IOException;
import java.io.InputStream;


public class ProblemK{
    public static void main(String[] args) throws IOException {
        FastReader in=new FastReader();
        FastWriter out=new FastWriter();
        int n=in.nextInt();
        int m=in.nextInt();
        int []a=new int[n+1];               // 玩家 1..n,下标 0 不用
        for(int i=1;i<=n;i++){
            a[i]=in.nextInt();
        }

        // 数组模拟双向循环链表:只串起"还在对局中"的玩家,摘除和找下家都是 O(1)
        int []nxt=new int[n+1];
        int []prv=new int[n+1];
        for(int i=1;i<=n;i++){
            nxt[i]=(i==n)?1:i+1;
            prv[i]=(i==1)?n:i-1;
        }

        String s=in.next();
        int cur=1;                          // 从 1 号玩家开始
        int dir=1;                          // 1 表示顺时针(编号增大)
        for(int i=0;i<m;i++){
            char ch=s.charAt(i);

            a[cur]--;                       // 出牌
            if(a[cur]==0){                  // 打光立刻退出对局:摘掉自己
                nxt[prv[cur]]=nxt[cur];
                prv[nxt[cur]]=prv[cur];
            }

            if(ch=='R'){
                dir=-dir;                   // 反转牌:先翻转方向
            }
            // 轮到下家;刚被摘除时 nxt/prv 仍指向摘除时的活邻居,所以照常迈一步
            cur=(dir>0)?nxt[cur]:prv[cur];
            if(ch=='S'){                    // 禁止牌:下家暂停一次,再迈一步
                cur=(dir>0)?nxt[cur]:prv[cur];
            }else if(ch=='D'){              // 罚抽两张:下家 +2 并暂停一次
                a[cur]+=2;
                cur=(dir>0)?nxt[cur]:prv[cur];
            }
        }

        for(int i=1;i<=n;i++){
            out.println(a[i]);
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
