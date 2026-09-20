package numTilePossibilities;

import java.util.Arrays;


class Solution {
    /*
     *   使用DFS回溯的思路，每新增一个结果就将答案加一，每一层中不能使用相同的元素，这一点可以通过回溯判断
     * */
    int ans;
    char[] s;

    public int numTilePossibilities(String tiles) {
        s = tiles.toCharArray();
        Arrays.sort(s); //排序将所有使得所有相同字符在数组中连续出现
        boolean[] used = new boolean[s.length]; //used用来记录某个字符是否被使用过
        DFS(used);
        return ans;
    }

    public void DFS(boolean[] used) {
        char last = '*';    //在选择某一层的第一个字符时，将last设置为*，保证任意未使用过的字符均可被选择
        for (int i = 0; i < s.length; i++) {
            if (!used[i] && s[i] != last) {
                //只有被未被选择过并且不同于上一轮回溯使用的字符才会在同一层中被遍历
                ans++;
                used[i] = true;
                DFS(used);          //继续DFS遍历
                used[i] = false;    //回溯
                last = s[i];        //回溯后更新last
            }
        }
    }
}
