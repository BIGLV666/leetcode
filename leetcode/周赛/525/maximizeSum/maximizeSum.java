
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.*;

class Solution {
  private Node[] nodes;

  public long maximizeSum(int[] nums, int k) {
    nodes = new Node[nums.length];
    f(nums);
    PriorityQueue<Node> pq = new PriorityQueue<>(
    Comparator.comparingLong(n -> n.max - n.val)
);
    long ans = 0;
    for (var node : nodes) {
      if(node.val!=node.max)
        pq.add(node);
      ans += node.val;
      if (pq.size() > k)
        pq.poll();
    }

    while (!pq.isEmpty()) {
      var node = pq.poll();
      ans -= node.val;
      ans += node.max;
    }
    return ans;

  }

  private long build(List<Integer> list) {
    int carry = 1;
    long ans = 0;
    for (Integer num : list) {
      ans += num * carry;
      carry *= 10;
    }
    return ans;
  }

  private void f(int[] nums) {
    for (int i = 0; i < nums.length; i++) {
      int num = nums[i];
      int len = 0;
      int maxNum = 0;
      int val = num;

      List<Integer>arr=new ArrayList<>();
      Node node = new Node();
      while (num > 0) {
        maxNum = Math.max(maxNum, num % 10);
        len++;
        arr.add(num % 10);
        num /= 10;
      }
      node.val=val;
      node.maxNum=maxNum;
      node.len=len;
      arr.sort((a, b) -> a.compareTo(b));
      node.max=build(arr);

      nodes[i] = node;

    }
  }

  class Node {
    int val;
    int maxNum;
    int len;
    long max;

    Node(int val, int maxNum, int len,int max) {
      this.val = val;
      this.maxNum = maxNum;
      this.len = len;
      this.max=max;
    }

    Node() {
    }
  }
}
