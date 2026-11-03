package leetcode.周赛.calculateScore;

class Solution {
  public int calculateScore(int a, int b) {
    int s1 = get(a);
    int s2 = get(b);
    if (s1 >= s2)
      return s1 * s2;
    return s2 / s1;
  }

  private int get(int n) {
    int ans = 0;
    while (n > 0) {
      if (n % 2 == 1) {
        ans++;
      }
      n /= 2;
    }
    return ans;
  }
}
