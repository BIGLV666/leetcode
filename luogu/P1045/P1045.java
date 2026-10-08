package luogu.P1045;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

/**
 * 洛谷 P1045 Meissen数(高精度/数学)
 *
 * 输入 p(≤ 3.1×10^6),求 2^p − 1 的位数与其最后 500 位。
 * 位数 = floor(p·log10 2) + 1(浮点直接算);
 * 末 500 位用 BigInteger.modPow 求 2^p mod 10^500 再减一,不足 500 位左补零,
 * 按每行 50 位共 10 行输出。
 */
class Main{
    public static void main(String[] args) throws IOException{
        FastReader in=new FastReader();
        int p=in.nextInt();
        System.out.println(digitCount(p));
        print(last500(p));
    }

    /** 返回 2^p − 1 的十进制位数。 */
    static int digitCount(int p){
        return (int)Math.floor(p*Math.log10(2))+1;
    }

    /** 返回 2^p − 1 的最后 500 位,恒为 500 个字符(不足左补零)。 */
    static String last500(int p){
        String ans=new BigInteger("2").modPow(BigInteger.valueOf(p),new BigInteger("1"+"0".repeat(500))).subtract(BigInteger.ONE).toString();
        return "0".repeat(500-ans.length())+ans;
    }

    private static void print(String ans){
        for(int i=0;i<10;i++){
            int l=i*50;
            int r=l+50;
            System.out.println(ans.substring(l,r));
        }
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
