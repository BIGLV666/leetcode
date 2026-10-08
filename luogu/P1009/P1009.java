package luogu.P1009;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

/**
 * 洛谷 P1009 [NOIP 1998 普及组] 阶乘之和(Easy)
 *
 * 求 S = 1! + 2! + ... + n!(n ≤ 50)。50! 约 3×10^64,远超 long,必须高精度:
 * 用 BigInteger 维护「当前阶乘」num 与「累加和」ans,第 i 步把 num 乘上 i 后累加,
 * 避免每一项都从头计算阶乘。
 */
class Main{
    public static void main(String[] args)throws IOException{
        FastReader in=new FastReader();
        System.out.println(solve(new BigInteger(in.next())));
    }

    /** 返回 1! + 2! + ... + n!;循环不变量:num 始终等于 i!。 */
    static BigInteger solve(BigInteger n){
        BigInteger num=new BigInteger("1");
        BigInteger ans=new BigInteger("0");
        BigInteger a=new BigInteger("1");
        for(BigInteger i=new BigInteger("1");i.compareTo(n)<=0;i=i.add(a)){
            num=num.multiply(i);
            ans=ans.add(num);
        }
        return ans;
    }
}



/**
 * 快读：直接从 System.in 按字节缓冲区读取，比 Scanner 快很多。
 * 用法：FastReader fr = new FastReader();
 *      int x = fr.nextInt();  long y = fr.nextLong();
 *      String s = fr.next();  String line = fr.nextLine();
 */
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
