#ifndef LC_CODEC_HPP
#define LC_CODEC_HPP

#include "list_node.hpp"
#include "tree_node.hpp"
#include <sstream>
#include <string>

/// 从 LeetCode 字符串 "[1,2,3]" 反序列化链表
inline ListNode* deserializeList(const std::string& data) {
    if (data.empty() || data == "[]") return nullptr;

    ListNode dummy;
    ListNode* cur = &dummy;
    std::string num;
    for (char c : data) {
        if (c == '[' || c == ' ' || c == '"') continue;
        if (c == ',' || c == ']') {
            if (!num.empty()) {
                cur->next = new ListNode(std::stoi(num));
                cur = cur->next;
                num.clear();
            }
        } else {
            num += c;
        }
    }
    return dummy.next;
}

/// 从 LeetCode 字符串 "[1,null,2,3]" 反序列化二叉树
inline TreeNode* deserializeTree(const std::string& data) {
    if (data.empty() || data == "[]") return nullptr;

    std::vector<std::optional<int>> values;
    std::string token;
    for (char c : data) {
        if (c == '[' || c == ' ' || c == '"') continue;
        if (c == ',' || c == ']') {
            if (!token.empty()) {
                if (token == "null") {
                    values.push_back(std::nullopt);
                } else {
                    values.push_back(std::stoi(token));
                }
                token.clear();
            }
        } else {
            token += c;
        }
    }
    return buildTree(values);
}

#endif