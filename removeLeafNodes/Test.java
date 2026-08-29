package removeLeafNodes;

import leetcode.TreeNode;

/** removeLeafNodes 的 BFS 测试。 */
public class Test {
    public static void main(String[] args) {
        check("[1,2,3,2,null,2,4]", 2, "[1,null,3,null,4]");
        check("[1,2,3,2,2,2,4]", 2, "[1,null,3,null,4]");
        check("[1,2,3]", 2, "[1,null,3]");
        check("[1,1,1]", 1, "[]");
        check("[1]", 1, "[]");
        check("[1]", 2, "[1]");
        check("[]", 1, "[]");
        check("[1,2,2,2,2]", 2, "[1]");
        System.out.println("All removeLeafNodes tests passed.");
    }

    private static void check(String input, int target, String expected) {
        TreeNode root = TreeNode.deserializeTree(input);
        TreeNode actual = new Solution().removeLeafNodes(root, target);
        String actualText = TreeNode.treeToString(actual);
        if (!expected.equals(actualText)) {
            throw new AssertionError(input + ", target=" + target
                    + ": expected " + expected + ", got " + actualText);
        }
    }
}