package Hot100.lengthOfLongestSubstring;

public class Test {
    private static final Solution solution = new Solution();

    public static void main(String[] args) {
        assertResult("abcabcbb", 3);
        assertResult("bbbbb", 1);
        assertResult("pwwkew", 3);
        assertResult("dvdf", 3);
        assertResult("abcdef", 6);
        assertResult("a", 1);

        System.out.println("All lengthOfLongestSubstring tests passed.");
    }

    private static void assertResult(String input, int expected) {
        int actual = solution.lengthOfLongestSubstring(input);
        if (actual != expected) {
            throw new AssertionError("input=\"" + input + "\", actual=" + actual
                    + ", expected=" + expected);
        }
    }
}