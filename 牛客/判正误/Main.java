package 牛客.判正误;

import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    private static final BigInteger[] MODS = {
            BigInteger.valueOf(1_000_000_007L),
            BigInteger.valueOf(1_000_000_009L),
            BigInteger.valueOf(998_244_353L),
            BigInteger.valueOf(1_000_000_033L)
    };
    public  static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n=in.nextInt();
        for(int t=0;t<n;t++) {
            BigInteger a = new BigInteger(in.next());
            BigInteger b = new BigInteger(in.next());
            BigInteger c = new BigInteger(in.next());
            BigInteger d = new BigInteger(in.next());
            BigInteger e = new BigInteger(in.next());
            BigInteger f = new BigInteger(in.next());
            BigInteger g = new BigInteger(in.next());

            // Check if a^d + b^e + c^f == g
            boolean ok = true;
            for (BigInteger mod : MODS) {
                BigInteger left = a.modPow(d, mod)
                        .add(b.modPow(e, mod))
                        .add(c.modPow(f, mod))
                        .mod(mod);

                BigInteger right = g.mod(mod);

                if (!left.equals(right)) {
                    ok = false;
                    break;
                }
            }
            System.out.println(ok ? "Yes" : "No");

        }
    }
}
