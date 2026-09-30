package luogu.p1015;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 洛谷 P1015 回文数(https://www.luogu.com.cn/problem/P1015)
 *
 * <p>N(2..16) 进制数 M:每一步把 M 与它在 N 进制下的反转数相加,问至少几步后
 * M 成为回文数;30 步(含)内得不到则输出 Impossible!。</p>
 *
 * <p>解法:按位模拟高精度加法。数字低位在前存进 List,一步加法就是原数与
 * 反转数逐位相加再进位(位数每步至多增 1),然后检查是否回文。</p>
 */
class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String m = sc.next();
        System.out.println(solve(n, m));
    }

    /** 主流程:返回 "STEP=i"(1..30 内首次回文的步数)或 "Impossible!"。 */
    static String solve(int base, String m) {
        List<Integer> arr = toDigits(m.toUpperCase(), base);
        for (int step = 1; step <= 30; step++) {
            addReverse(arr, base);
            if (isPalindrome(arr)) {
                return "STEP=" + step;
            }
        }
        return "Impossible!";
    }

    /** 把 N 进制字符串转成低位在前的数字列表;A~F 视作 10~15。 */
    static List<Integer> toDigits(String m, int base) {
        List<Integer> arr = new ArrayList<>();
        for (int i = m.length() - 1; i >= 0; i--) {
            char ch = m.charAt(i);
            arr.add(ch >= 'A' ? ch - 'A' + 10 : ch - '0');
        }
        return arr;
    }

    /** 原地执行一步「与反转相加」:arr[i] 加上倒数第 i 位,逐位进位,必要时扩一位。 */
    static void addReverse(List<Integer> arr, int base) {
        List<Integer> copy = new ArrayList<>(arr);
        int carry = 0;
        for (int i = 0, r = copy.size() - 1; i < copy.size(); i++, r--) {
            int sum = copy.get(r) + arr.get(i) + carry;
            arr.set(i, sum % base);
            carry = sum / base;
        }
        while (carry > 0) {
            arr.add(carry % base);
            carry /= base;
        }
    }

    static boolean isPalindrome(List<Integer> arr) {
        int l = 0, r = arr.size() - 1;
        while (l < r) {
            if (!arr.get(l).equals(arr.get(r))) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }
}
