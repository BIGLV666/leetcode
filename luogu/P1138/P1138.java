package luogu.P1138;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * 洛谷 P1138 第 k 小整数(Easy)
 *
 * n 个正整数中求第 k 小的「不同」整数;不同整数不足 k 个输出 NO RESULT。
 * 用 HashSet 去重、大小为 k 的大根堆维护最小的 k 个不同值:
 * 堆顶即第 k 小;堆未满(k 个)时无答案。
 */
class Main{
    public static void main(String[] args)throws IOException{
        FastReader in=new FastReader();
        int n=in.nextInt();
        int k=in.nextInt();
        int[] a=new int[n];
        for(int i=0;i<n;i++){
            a[i]=in.nextInt();
        }
        System.out.println(solve(n, k, a));
    }

    /** 返回第 k 小的不同整数(字符串形式),不足 k 个不同值为 NO RESULT。 */
    static String solve(int n, int k, int[] a){
        Set<Integer> set=new HashSet<>();
        PriorityQueue<Integer> pq=new PriorityQueue<>((x,y)->(y-x));
        for(int i=0;i<n;i++){
            int v=a[i];
            if(!set.contains(v)){
                set.add(v);
                pq.add(v);
            }
            if(pq.size()>k)
                pq.poll();
        }
        return pq.size()>=k?String.valueOf(pq.peek()):"NO RESULT";
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
