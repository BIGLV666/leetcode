package exist;


/**
 * <a href="https://leetcode.cn/problems/word-search/">79. 单词搜索</a>
 *
 * <p>在字符网格中判断 word 是否存在于某条「上下左右相邻、每个格子最多用一次」的路径上。</p>
 *
 * <p>解法:DFS 回溯。从每个等于 word[0] 的格子出发逐字符匹配:</p>
 * <ul>
 *   <li>vis 记录当前路径用过的格子,进入标记、回溯撤销;</li>
 *   <li>StringBuilder 记录当前路径拼出的串,拼出完整 word 即命中
 *       (用 ans 标志提前终止整棵搜索树);</li>
 *   <li>起点重试时要把 ans 复位,避免上一个起点的结论串场。</li>
 * </ul>
 *
 * <p>复杂度:时间 O(m·n·3^L)(L 为 word 长度;首格 4 个方向,之后每个格子最多 3 个可走)、
 * 空间 O(L)(vis 网格与路径串)。</p>
 */
class Solution {
    private boolean[][] vis;     // 当前路径用过的格子
    private StringBuilder sb;    // 当前路径拼出的串
    private boolean ans;         // 是否已找到一条完整路径

    public boolean exist(char[][] board, String word) {
        sb = new StringBuilder();
        vis = new boolean[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == word.charAt(0)) {   // 只有首字符相等才值得出发
                    ans = false;                        // 换起点,复位结论
                    dfs(i, j, board, word, 0);
                    if (ans) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void dfs(int r, int c, char[][] b, String w, int k) {
        if (ans) {
            return;                                     // 已找到,其余分支全部剪掉
        }
        if (sb.toString().equals(w)) {                  // 路径串拼出了完整 word
            ans = true;
            return;
        }
        if (k >= w.length()) {
            return;
        }
        if (r < 0 || r >= b.length) {
            return;
        }
        if (c < 0 || c >= b[0].length) {
            return;
        }
        if (vis[r][c]) {
            return;                                     // 同一条路径不能重复用格
        }
        if (b[r][c] != w.charAt(k)) {
            return;                                     // 与 word 第 k 位不符
        }
        vis[r][c] = true;
        sb.append(b[r][c]);
        dfs(r + 1, c, b, w, k + 1);
        dfs(r - 1, c, b, w, k + 1);
        dfs(r, c + 1, b, w, k + 1);
        dfs(r, c - 1, b, w, k + 1);
        sb.deleteCharAt(sb.length() - 1);               // 回溯:撤销字符与占用标记
        vis[r][c] = false;
    }
}
