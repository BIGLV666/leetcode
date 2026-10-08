package luogu.P1033;

import java.io.IOException;
import java.io.InputStream;

/**
 * 洛谷 P1033 [NOIP 2002 提高组] 自由落体(模拟)
 *
 * 高 H 的天花板下 0..n-1 处各有一个小球;小车(长 L、高 K,右缘初始距原点 S1)
 * 以速度 V 向原点方向匀速行驶;小球按 d = 5t²(g=10) 自由下落。
 * 小球与小车的(水平)距离 ≤ 1e-4 且尚未落地时即被接住,落到车尾同样计入。
 *
 * 对小球 i:车覆盖它的时间窗为 [(S1-i-ε)/V, (S1-i+L+ε)/V](ε=1e-4 为题面空间容差);
 * 下落距离落在车内高度区间 [H-K, H] 的时间窗为 [√((H-K)/5), √(H/5)]。
 * 两个窗口相交(等价于 d(TMin) ≤ H 且 d(TMax) ≥ H-K,d 单调递增)则计数。
 */
class Main{
    public static void main(String[] args) throws IOException{
        FastReader in=new FastReader();
        System.out.println(solve(
                Double.parseDouble(in.next()),
                Double.parseDouble(in.next()),
                Double.parseDouble(in.next()),
                Double.parseDouble(in.next()),
                Double.parseDouble(in.next()),
                in.nextInt()));
    }

    static int solve(double H, double s1, double v, double L, double k, int n){
        int ans=0;
        for(int i=0;i<n;i++){
            double TMin=(s1-i-1e-4)/v;           // 车右缘刚到达球 i(含 1e-4 容差)
            double TMax=(s1-i+L+1e-4)/v;         // 车尾掠过球 i(落到车尾也计入)
            if(TMax<0){
                continue;                         // 球在车初始区间的右侧,向左行驶永远碰不到
            }
            TMin=Math.max(TMin,0.0);
            double dmin=0.5*Math.pow(TMin,2)*10;  // 时间窗起点的下落距离
            double dmax=0.5*Math.pow(TMax,2)*10;  // 时间窗终点的下落距离
            if(dmin<=H&&dmax>=H-k){               // 下落区间与车内高度区间 [H-k, H] 相交
                ans++;
            }
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
