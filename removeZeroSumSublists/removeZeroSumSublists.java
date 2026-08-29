package removeZeroSumSublists;

import leetcode.ListNode;
import java.util.LinkedList;
import java.util.List;

/**
 * 1171. 从链表中删去总和值为零的连续节点。
 *
 * <p>使用栈保存当前仍可能出现在结果中的节点及其累计和。当当前节点值
 * 使某个累计和变为零时，从该位置开始的节点可以整体移除；否则把当前值
 * 累加到已有节点的累计和，并将当前节点加入栈。</p>
 *
 * <p>设 n 为链表长度。每次查找和删除使用线性表操作，最坏时间复杂度为
 * O(n^2)，额外空间复杂度为 O(n)。</p>
 */
class Solution {
    public ListNode removeZeroSumSublists(ListNode head) {
        List<Node> stack = new LinkedList<>();

        for (ListNode cur = head; cur != null; cur = cur.next) {
            boolean find = true;
            for (int i = 0; i < stack.size(); i++) {
                if (stack.get(i).sum + cur.val == 0) {
                    // 当前节点与第 i 个候选节点之间的连续和为零。
                    stack.subList(i, stack.size()).clear();
                    find = false;
                    break;
                }
                stack.get(i).sum += cur.val;
            }
            if (find && cur.val != 0) {
                stack.add(new Node(cur.val, cur));
            }
        }

        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        for (Node node : stack) {
            if (node.node.val != 0) {
                tail.next = new ListNode(node.node.val);
                tail = tail.next;
            }
        }
        return dummy.next;
    }
}

class Node {
    ListNode node;
    int sum;

    Node(int sum, ListNode node) {
        this.sum = sum;
        this.node = node;
    }

    /** Node 只描述当前节点，整条链表请使用 ListNode.serializeList。 */
    @Override
    public String toString() {
        return "Node{sum=" + sum + ", value=" + node + "}";
    }
}