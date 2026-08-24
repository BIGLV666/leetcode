#ifndef LC_LIST_NODE_HPP
#define LC_LIST_NODE_HPP

#include <string>
#include <vector>

/// 单链表节点
struct ListNode {
    int val;
    ListNode* next;

    ListNode() : val(0), next(nullptr) {}
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode* n) : val(x), next(n) {}
};

/// 从整数数组构建链表
inline ListNode* buildList(const std::vector<int>& values) {
    ListNode dummy;
    ListNode* cur = &dummy;
    for (int v : values) {
        cur->next = new ListNode(v);
        cur = cur->next;
    }
    return dummy.next;
}

/// 将链表转换为整数数组
inline std::vector<int> listToArray(const ListNode* head) {
    std::vector<int> res;
    for (; head; head = head->next) {
        res.push_back(head->val);
    }
    return res;
}

/// 将链表输出为 LeetCode 格式字符串 "[1,2,3]"
inline std::string listToString(const ListNode* head) {
    std::string s = "[";
    for (const ListNode* p = head; p; p = p->next) {
        if (p != head) s += ",";
        s += std::to_string(p->val);
    }
    s += "]";
    return s;
}

/// 释放链表内存
inline void freeList(ListNode* head) {
    while (head) {
        ListNode* nxt = head->next;
        delete head;
        head = nxt;
    }
}

#endif