package minCost;
class Solution {
    private boolean[][][] vis;
    private int ans;
    public int minCost(int[][] grid) {
        ans = Integer.MAX_VALUE;
        vis = new boolean[grid.length][grid[0].length][1024];
        dfs(0, 0, grid, 0);
        return ans;
    }

    private void dfs(int r, int c, int[][] grid, int xor) {
        if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length)
            return;
        xor ^= grid[r][c];
        if (ans == 0)
            return;
        if (vis[r][c][xor])
            return;

        vis[r][c][xor] = true;
        if (r == grid.length - 1 && c == grid[0].length - 1) {
            ans = Math.min(ans, xor);
            return;
        }
        dfs(r + 1, c, grid, xor);
        dfs(r, c + 1, grid, xor);
    }
}
