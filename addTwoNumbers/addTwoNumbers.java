package addTwoNumbers;

import leetcode.ListNode;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://leetcode.cn/problems/add-two-numbers/">2. 两数相加</a>
 *
 * <p>两个非空链表表示两个非负整数,<b>低位在头</b>(个位是第一个节点),每位一个数字。
 * 求它们的和,同样以「低位在头」的链表返回。</p>
 *
 * <p>解法:以较长链表为基底,逐位相加后写回,再单独处理最后的进位。</p>
 * <ol>
 *   <li>分别把两个链表的节点收进 List,便于按下标随机访问;</li>
 *   <li>令 l 为较长者、p 为较短者(长度相等时任取,不影响结果);</li>
 *   <li>先按 p 的长度逐位相加,把结果写回 l 的对应节点;</li>
 *   <li>若仍有进位,继续沿 l 的剩余位传播,进位归零即可提前停止;</li>
 *   <li>走完 l 仍有进位说明最高位溢出,<b>在链表尾部</b>追加一个新节点。</li>
 * </ol>
 *
 * <p>关键:<b>低位在头</b>意味着新的最高位要挂在尾部,而不是插到头部;
 * 另外逐位相加的上界是较短链表的长度,不能拿较长链表的长度去索引较短链表。</p>
 *
 * <p>复杂度:时间 O(max(m, n))、空间 O(m + n)(存放两个链表的节点引用)。
 * 注意:实现是就地修改较长链表的节点值并返回其头节点。</p>
 */
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        List<ListNode> list1 = new ArrayList<>();
        List<ListNode> list2 = new ArrayList<>();

        for (ListNode cur = l1; cur != null; cur = cur.next) {
            list1.add(cur);
        }
        for (ListNode cur = l2; cur != null; cur = cur.next) {
            list2.add(cur);
        }

        // 较长的作为相加基底(结果长度最多比它多 1 位)
        return list1.size() > list2.size() ? f(list1, list2) : f(list2, list1);
    }

    /**
     * 把较短链表 p 加到较长链表 l 上,结果就地写回 l。
     *
     * @param l 较长的链表(基底,会被就地修改)
     * @param p 较短的链表(只读)
     * @return 结果链表头节点
     */
    private ListNode f(List<ListNode> l, List<ListNode> p) {
        int carry = 0;

        // 两表公共部分逐位相加
        for (int j = 0; j < p.size(); j++) {
            carry += l.get(j).val + p.get(j).val;
            l.get(j).val = carry % 10;
            carry /= 10;
        }

        // 进位沿 l 的剩余位继续传播;进位一旦归零,后面的高位不受影响,可提前停止
        for (int j = p.size(); j < l.size() && carry > 0; j++) {
            carry += l.get(j).val;
            l.get(j).val = carry % 10;
            carry /= 10;
        }

        // 走完 l 仍有进位:最高位溢出。低位在头部,故新节点追加到尾部而不是插到头部。
        if (carry > 0) {
            l.get(l.size() - 1).next = new ListNode(carry);
        }

        return l.get(0);
    }
}
