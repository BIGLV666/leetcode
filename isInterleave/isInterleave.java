package isInterleave;
/**超时*/
class Solution {

    public boolean isInterleave(String s1, String s2, String s3) {
        if (s3.length() != s1.length() + s2.length()) {
            return false;               // 长度对不上，直接 false
        }
        return dfs(s1, s2, s3, 0, 0);
    }

    /**
     * @param i1 s1 已经用了 i1 个字符
     * @param i2 s2 已经用了 i2 个字符
     *           不变量：s3 已经用了 i1 + i2 个字符，下一步要匹配的就是 s3[i1 + i2]
     */
    private boolean dfs(String s1, String s2, String s3, int i1, int i2) {
        int i3 = i1 + i2;

        if (i3 == s3.length()) {
            return true;                // 长度相等已保证 s1、s2 同时用完
        }

        // 分支一：这一位取自 s1
        if (i1 < s1.length() && s1.charAt(i1) == s3.charAt(i3)
                && dfs(s1, s2, s3, i1 + 1, i2)) {
            return true;
        }

        // 分支二：这一位取自 s2
        if (i2 < s2.length() && s2.charAt(i2) == s3.charAt(i3)
                && dfs(s1, s2, s3, i1, i2 + 1)) {
            return true;
        }

        // 两条路都走不通 → 返回 false，上层自动回溯去试别的分法
        return false;
    }
}