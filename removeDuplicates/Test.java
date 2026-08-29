package removeDuplicates;

public class Test {
    private static final Solution solution = new Solution();

    public static void main(String[] args) {
        testPrettyDeque();
        testRemoveDuplicates();
        System.out.println("ok");
    }

    static void testPrettyDeque() {
        PrettyDeque<Character> dq = new PrettyDeque<>();
        dq.addLast('a'); dq.addLast('b'); dq.addLast('c');
        String s = dq.toString();
        if (!s.equals("[a,b,c]")) {
            System.out.println("PrettyDeque toString failed: " + s);
        }
    }

    static void testRemoveDuplicates() {
        TestCase[] cases = new TestCase[] {
            new TestCase("abbaca", "ca"),
            new TestCase("azxxzy", "ay"),
        };
        for (TestCase tc : cases) {
            String actual = solution.removeDuplicates(tc.input);
            if (!actual.equals(tc.expect)) {
                System.out.println("FAIL: input=" + tc.input + " actual=" + actual + " expect=" + tc.expect);
            }
        }
    }

    static class TestCase {
        String input; String expect;
        TestCase(String i, String e) { input = i; expect = e; }
    }
}
