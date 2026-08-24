from collections import deque
from typing import Optional


class TreeNode:
    def __init__(self, val: int = 0, left: 'TreeNode' = None, right: 'TreeNode' = None):
        self.val = val
        self.left = left
        self.right = right

    def __repr__(self) -> str:
        return tree_to_string(self)


def build_tree(values: list[int | None]) -> Optional[TreeNode]:
    """从 LeetCode 层序数组构建二叉树，None 表示空节点"""
    if not values or values[0] is None:
        return None

    root = TreeNode(values[0])
    q = deque([root])
    i = 1

    while q and i < len(values):
        parent = q.popleft()

        if i < len(values) and values[i] is not None:
            parent.left = TreeNode(values[i])
            q.append(parent.left)
        i += 1

        if i < len(values) and values[i] is not None:
            parent.right = TreeNode(values[i])
            q.append(parent.right)
        i += 1

    return root


def tree_to_array(root: TreeNode) -> list[Optional[int]]:
    """将二叉树转换为层序数组（含 None，尾部 None 已修剪）"""
    if not root:
        return []

    result = []
    q = deque([root])

    while q:
        node = q.popleft()

        if node is None:
            result.append(None)
            continue

        result.append(node.val)
        q.append(node.left)
        q.append(node.right)

    # 删除末尾连续的 None
    while result and result[-1] is None:
        result.pop()

    return result


def tree_to_string(root: TreeNode) -> str:
    """将二叉树输出为 LeetCode 格式字符串 '[1,2,null,3]'"""
    arr = tree_to_array(root)
    tokens = [str(x) if x is not None else "null" for x in arr]
    return "[" + ",".join(tokens) + "]"


def deserialize_tree(data: str) -> Optional[TreeNode]:
    """从 LeetCode 字符串 '[1,null,2,3]' 反序列化二叉树"""
    if not data or data == "[]":
        return None
    data = data.strip("[]").replace(" ", "")
    if not data:
        return None
    values = []
    for token in data.split(","):
        if token == "null" or token == "None":
            values.append(None)
        else:
            values.append(int(token))
    return build_tree(values)