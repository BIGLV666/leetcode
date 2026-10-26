package luogu.P1210;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        // 一次性读取全部输入，保留换行符
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[8192];
        int len;
        while ((len = br.read(buf)) != -1) {
            sb.append(buf, 0, len);
        }
        String text = sb.toString();

        int n = text.length();

        // letters[i] 表示第 i 个字母（统一转小写）
        // pos[i] 表示第 i 个字母在原文中的位置
        char[] letters = new char[n];
        int[] pos = new int[n];

        int L = 0;
        for (int i = 0; i < n; i++) {
            char c = text.charAt(i);
            if (isLetter(c)) {
                letters[L] = Character.toLowerCase(c);
                pos[L] = i;
                L++;
            }
        }

        FastWriter out = new FastWriter();

        if (L == 0) {
            out.println(0);
            out.println();
            out.flush();
            return;
        }

        int bestLen = 0;
        int bestStart = 0;
        int bestEnd = 0;

        for (int center = 0; center < L; center++) {
            // 奇数长度回文
            int l = center;
            int r = center;
            while (l >= 0 && r < L && letters[l] == letters[r]) {
                l--;
                r++;
            }

            int start = l + 1;
            int end = r - 1;
            int curLen = end - start + 1;

            if (curLen > bestLen || (curLen == bestLen && start < bestStart)) {
                bestLen = curLen;
                bestStart = start;
                bestEnd = end;
            }

            // 偶数长度回文
            l = center - 1;
            r = center;
            while (l >= 0 && r < L && letters[l] == letters[r]) {
                l--;
                r++;
            }

            start = l + 1;
            end = r - 1;
            curLen = end - start + 1;

            if (curLen > bestLen || (curLen == bestLen && start < bestStart)) {
                bestLen = curLen;
                bestStart = start;
                bestEnd = end;
            }
        }

        // 映射回原字符串，保留标点、空格和换行
        String answer = text.substring(pos[bestStart], pos[bestEnd] + 1);

        out.println(bestLen);
        out.println(answer);
        out.flush();
    }

    private static boolean isLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }
}
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