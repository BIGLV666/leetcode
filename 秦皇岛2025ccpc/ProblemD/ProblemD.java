package 秦皇岛2025ccpc.ProblemD;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


public  class ProblemD{
    public static  void main(String[] args) throws IOException {
        FastReader in=new FastReader();
        FastWriter out=new FastWriter();

        int n=in.nextInt();
        long k=in.nextLong();

        int [] arr=new int[n];
        for(int i=0;i<n;i++){
            arr[i]=in.nextInt();
        }
        Arrays.sort(arr);

        long total=(long)n*(n-1)/2;         // 总方案数,题意保证 k <= total
        if(k>total)k=total;

        // 二分最小的 D,使得"差值 <= D 的数对"至少有 k 个
        long lo=0,hi=arr[n-1]-arr[0];
        while(lo<hi){
            long mid=(lo+hi)>>>1;
            if(countLE(arr,mid)>=k)hi=mid;
            else lo=mid+1;
        }
        long d=lo;

        long []pre=new long[n+1];           // 前缀和,用于 O(1) 求区间和
        for(int i=0;i<n;i++){
            pre[i+1]=pre[i]+arr[i];
        }

        // 差值 < d 的全取,剩下 k-cnt 个的差值都恰好等于 d
        long []cntBox=new long[1];
        long s=0,c=0;
        if(d>0){
            s=sumLE(arr,pre,d-1,cntBox);
            c=cntBox[0];
        }
        out.println(s+(k-c)*d);
        out.flush();

    }

    /** 差值 <= D 的无序数对个数。排序后双指针,O(n)。 */
    private static long countLE(int[]arr,long D){
        long c=0;
        int p=0;
        for(int j=0;j<arr.length;j++){
            while(p<j&&arr[j]-arr[p]>D)p++;  // p 随 j 单调不减
            c+=j-p;
        }
        return c;
    }

    /** 差值 <= D 的数对的差值之和,数对个数写入 cntBox[0]。双指针 + 前缀和,O(n)。 */
    private static long sumLE(int[]arr,long[]pre,long D,long[]cntBox){
        long c=0,s=0;
        int p=0;
        for(int j=0;j<arr.length;j++){
            while(p<j&&arr[j]-arr[p]>D)p++;
            long m=j-p;                      // i 取 [p, j-1]
            c+=m;
            s+=m*arr[j]-(pre[j]-pre[p]);
        }
        cntBox[0]=c;
        return s;
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
