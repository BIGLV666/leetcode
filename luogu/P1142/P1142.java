package luogu.P1142;

import java.io.IOException;
import java.io.InputStream;

/**
 * 洛谷 P1142 轰炸(枚举/几何)
 *
 * 求平面上 n 个点中最多有多少个点落在同一条直线上:枚举点对 (i,j),
 * 用叉积为零统计与其余共线点的个数,取最大值。
 *
 * 已知缺陷(按原样保留,未修改):
 * 1) 内层循环写成 k<=n,k==n 时访问 nodes[n] 数组越界,n≥3 的任何输入都会抛
 *    ArrayIndexOutOfBoundsException;
 * 2) 点对从 i=1 起、第三点从 j+1 起,下标小于 i 的点永远不会参与配对与统计,会漏解。
 * 待修正后重写为全点对枚举或斜率哈希,再补测试。
 */
class Main{
    public static void main(String[] args) throws IOException{
        FastReader in=new FastReader();
        int n=in.nextInt();
        Node[] nodes=new Node[n];
        for(int i=0;i<n;i++){
            nodes[i]=new Node(in.nextInt(),in.nextInt());
        }
        int ans=0;
        for(int i=1;i<n-1;i++){
            for(int j=i+1;j<n;j++){
                int num=2;
                for(int k=j+1;k<=n;k++){
                    if((nodes[i].x-nodes[k].x)*(nodes[j].y-nodes[k].y)==(nodes[j].x-nodes[k].x)*(nodes[i].y-nodes[k].y))
                        num++;
                }
                ans= Math.max(num, ans);
            }
        }
        System.out.println(ans);
    }
}
class Node{
    int x;
    int y;
    Node(int x,int y){
        this.x=x;
        this.y=y;
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