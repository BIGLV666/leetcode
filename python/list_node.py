# 链表节点
class ListNode:
    def __init__(self, val: int = 0, next: 'ListNode' = None):
        self.val = val
        self.next = next

    def __repr__(self) -> str:
        return list_to_string(self)


def build_list(values: list[int]) -> ListNode | None:
    """从整数数组构建链表"""
    if not values:
        return None
    dummy = ListNode(0)
    cur = dummy
    for v in values:
        cur.next = ListNode(v)
        cur = cur.next
    return dummy.next


def list_to_array(head: ListNode) -> list[int]:
    """将链表转换为整数数组"""
    res = []
    while head:
        res.append(head.val)
        head = head.next
    return res


def list_to_string(head: ListNode) -> str:
    """将链表输出为 LeetCode 格式字符串 '[1,2,3]'"""
    return str(list_to_array(head)).replace(" ", "")


def deserialize_list(data: str) -> ListNode | None:
    """从 LeetCode 字符串 '[1,2,3]' 反序列化链表"""
    if not data or data == "[]":
        return None
    # 去除方括号和空格
    data = data.strip("[]").replace(" ", "")
    if not data:
        return None
    values = [int(x) for x in data.split(",")]
    return build_list(values)