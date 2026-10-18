package lanqiao.JavaC17.调皮的橘猫;


// 2: 类名必须Main, 不可修改

public class Main {
    public static void main(String[] args) {
        long N = 2026041221046202L;
        long total = 0;

        // 1 到 N 的总位数
        long start = 1;
        long len = 1;
        while (start <= N) {
            long end = Math.min(N, start * 10 - 1);
            total += (end - start + 1) * len;
            start *= 10;
            len++;
        }

        // 减去 1 到 2026
        long bad = 0;
        start = 1;
        len = 1;
        long M = 2026;
        while (start <= M) {
            long end = Math.min(M, start * 10 - 1);
            bad += (end - start + 1) * len;
            start *= 10;
            len++;
        }

        System.out.println(total - bad);
    }
}
