package removeZeroSumSublists;

import leetcode.ListNode;

/** removeZeroSumSublists 的无框架测试。 */
public class Test {
    private static final Solution SOLUTION = new Solution();

    public static void main(String[] args) {
        check("[1,2,-3,3,1]", "[3,1]");
        check("[1,2,3,-3,4]", "[1,2,4]");
        check("[1,2,3,-3,-2]", "[1]");
        check("[0]", "[]");
        check("[1,-1]", "[]");
        check("[1,2,3]", "[1,2,3]");
        check("[1,3,2,-3,-2,5,5,-5,1]", "[1,5,1]");
        check("[0,0,0]", "[]");

        ListNode empty = SOLUTION.removeZeroSumSublists(null);
        assertEquals(null, empty, "null input should return null");

        ListNode node = ListNode.buildList(new int[] {7, 8});
        assertEquals("7", node.toString(), "node.toString()");
        assertEquals("[7,8]", ListNode.serializeList(node), "serializeList");
        assertEquals("[7,8]", ListNode.listToString(node), "listToString compatibility");
        assertEquals("[]", ListNode.serializeList(null), "null serialization");

        System.out.println("All tests passed.");
    }

    private static void check(String input, String expected) {
        ListNode result = SOLUTION.removeZeroSumSublists(ListNode.deserializeList(input));
        assertEquals(expected, ListNode.serializeList(result), input);
    }

    private static void assertEquals(Object expected, Object actual, String name) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
