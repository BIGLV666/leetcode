package luogu.P1198;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Luogu P1198 [JSOI2008] 最大数(Medium)
 *
 * 维护一个数列:A n 把 (上次查询答案 + n) mod D 插入末尾;Q L 查询末尾 L 个数的最大值。
 *
 * 已知性能缺陷(原作者注):Q 用 O(L) 逐个扫描末尾 L 个数,最坏 O(m²),
 * 大数据会超时,需改单调队列/线段树/ST 表;此处按原实现保留,测试只验证正确性。
 */
class Main{
    public  static void main(String[] args) throws IOException {
        FastReader in = new FastReader();
        int m = in.nextInt();
        int MOD = in.nextInt();
        String[] ops = new String[m];
        long[] vals = new long[m];
        for (int i = 0; i < m; i++) {
            ops[i] = in.next();
            vals[i] = in.nextLong();
        }
        for (long ans : solve(m, MOD, ops, vals)) {
            System.out.println(ans);
        }
    }

    /**
     * 按操作序列处理后返回每个 Q 的答案(按出现顺序)。
     *
     * <p>last 保存上一次查询的答案(初始 0);A 插入 (last + n) mod D,
     * Q 线性扫描末尾 L 个元素取最大并更新 last。题面保证 L 不超过当前数列长度。</p>
     */
    static long[] solve(int m, int MOD, String[] ops, long[] vals){
        List<Long> list = new ArrayList<>();
        List<Long> out = new ArrayList<>();
        long last=0;
        for(int i=0;i<m;i++){
            String ch=ops[i];
            long k=vals[i];
            if("Q".equals(ch)){
                long ans=0;
                for(int j=list.size()-1;j>=list.size()-k;j--){
                    ans=Math.max(ans,list.get(j));
                }
                last=ans;
                out.add(ans);
            }
            if("A".equals(ch)){
                long l=(last+k)%MOD;
                list.add(l);
            }
        }
        return out.stream().mapToLong(Long::longValue).toArray();
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
