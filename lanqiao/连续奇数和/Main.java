package lanqiao.连续奇数和;


public class Main {

    public static void main(String[] args) {

        for (int i = 1; i < 10000; i += 2) {
            int sum = 0;
            for (int j = i; j < 10000; j += 2) {
                sum += j;
                if (111 * 111 * 111 == sum) {
                    System.out.println(i);
                    return;
                }
            }
        }
    }
}
