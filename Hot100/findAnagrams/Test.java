package Hot100.findAnagrams;

import java.util.List;

public class Test {
    private static final Solution solution = new Solution();

    public static void main(String[] args) {
        assertResult("cbaebabacd", "abc", List.of(0, 6));
        assertResult("abab", "ab", List.of(0, 1, 2));
        assertResult("baa", "aa", List.of(1));
        assertResult("abcdef", "gh", List.of());
        assertResult("a", "a", List.of(0));
        assertResult("ab", "abc", List.of());

        System.out.println("All findAnagrams tests passed.");
    }

    private static void assertResult(String s, String p, List<Integer> expected) {
        List<Integer> actual = solution.findAnagrams(s, p);
        if (!actual.equals(expected)) {
            throw new AssertionError("s=\"" + s + "\", p=\"" + p
                    + "\", actual=" + actual + ", expected=" + expected);
        }
    }
}