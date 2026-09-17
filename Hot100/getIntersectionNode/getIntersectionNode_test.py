import random
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent.parent.parent))  # 仓库根，便于导入 python/ 与 Hot100.*

from python.list_node import ListNode, build_list

from Hot100.getIntersectionNode.getIntersectionNode import Solution


def tail(head: ListNode) -> ListNode:
    """返回链表尾节点。"""
    while head.next:
        head = head.next
    return head


def build_intersecting(a_vals: list, b_vals: list, common_vals: list):
    """构造两条链表:各自接上同一段公共尾部。

    common_vals 为空表示两条链表不相交。返回 (headA, headB, common_head)。
    """
    common = build_list(common_vals)
    head_a = build_list(a_vals)
    head_b = build_list(b_vals)

    if common is not None:
        if head_a is None:
            head_a = common
        else:
            tail(head_a).next = common
        if head_b is None:
            head_b = common
        else:
            tail(head_b).next = common

    return head_a, head_b, common


def reference(head_a: ListNode, head_b: ListNode):
    """独立参考实现:先各自求长度,让长的先走差值步,再同步前进直到相遇。"""
    def length(head: ListNode) -> int:
        n = 0
        while head:
            n += 1
            head = head.next
        return n

    len_a, len_b = length(head_a), length(head_b)
    p, q = head_a, head_b
    for _ in range(len_a - len_b):
        p = p.next
    for _ in range(len_b - len_a):
        q = q.next
    while p is not q:
        p = p.next
        q = q.next
    return p


def main() -> None:
    s = Solution()

    # 官方示例 1: listA=[4,1,8,4,5], listB=[5,6,1,8,4,5], 交点为 8
    ha, hb, common = build_intersecting([4, 1], [5, 6, 1], [8, 4, 5])
    got = s.getIntersectionNode(ha, hb)
    assert got is common, f"官方示例1: 期望交点 {common.val}, 实际 {got and got.val}"

    # 官方示例 2: 不相交
    ha, hb, common = build_intersecting([2, 6, 4], [1, 5], [])
    assert s.getIntersectionNode(ha, hb) is None, "官方示例2: 期望无交点"

    # 官方示例 3: 同一条链表(交点就是头节点)
    same = build_list([1, 2, 3])
    assert s.getIntersectionNode(same, same) is same, "官方示例3: 同链应返回头节点"

    # 边界: 一边为空
    ha, hb, _ = build_intersecting([], [1, 2], [])
    assert s.getIntersectionNode(ha, hb) is None, "一边为空应返回 None"
    assert s.getIntersectionNode(None, None) is None, "两边都空应返回 None"

    # 边界: 公共段就是其中一条链(一边为空、另一边即公共段)
    ha, hb, common = build_intersecting([], [7, 8], [9, 10])
    assert s.getIntersectionNode(ha, hb) is common, "公共段应被识别"

    # 边界: 公共段只有一个节点
    ha, hb, common = build_intersecting([1], [2, 3], [9])
    assert s.getIntersectionNode(ha, hb) is common, "单节点公共段"

    # 关键: 必须返回链表中的节点本身(按对象身份),而不是值相同的新节点。
    # 下面两条链表节点值全都相同但完全独立,不能判为相交。
    ha = build_list([1, 1, 1])
    hb = build_list([1, 1, 1])
    assert s.getIntersectionNode(ha, hb) is None, "值相同但不相交时应返回 None"

    # 随机对拍:与「长度对齐后同步前进」参考实现互验(比较对象身份)
    random.seed(42)
    for _ in range(2000):
        a_vals = [random.randint(0, 9) for _ in range(random.randint(0, 6))]
        b_vals = [random.randint(0, 9) for _ in range(random.randint(0, 6))]
        common_vals = [random.randint(0, 9) for _ in range(random.choice([0, 0, 1, 2, 3, 4]))]

        ha, hb, _ = build_intersecting(a_vals, b_vals, common_vals)
        want = reference(ha, hb)
        got = s.getIntersectionNode(ha, hb)
        assert got is want, (
            f"a={a_vals}, b={b_vals}, common={common_vals}: "
            f"期望 {want and want.val}, 实际 {got and got.val}"
        )

    print("All tests passed.")


if __name__ == "__main__":
    main()
