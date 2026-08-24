# 序列化/反序列化快捷入口
# 本模块对 list_node 和 tree_node 的序列化函数做统一重导出，方便使用

from python.list_node import (
    ListNode,
    build_list,
    list_to_array,
    list_to_string,
    deserialize_list,
)
from python.tree_node import (
    TreeNode,
    build_tree,
    tree_to_array,
    tree_to_string,
    deserialize_tree,
)

__all__ = [
    # 链表
    "ListNode",
    "build_list",
    "list_to_array",
    "list_to_string",
    "deserialize_list",
    # 树
    "TreeNode",
    "build_tree",
    "tree_to_array",
    "tree_to_string",
    "deserialize_tree",
]