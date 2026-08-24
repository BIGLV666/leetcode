#ifndef LC_TREE_NODE_HPP
#define LC_TREE_NODE_HPP

#include <optional>
#include <queue>
#include <string>
#include <vector>

/// 二叉树节点
struct TreeNode {
    int val;
    TreeNode* left;
    TreeNode* right;

    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode* l, TreeNode* r) : val(x), left(l), right(r) {}
};

/// 从 LeetCode 层序数组构建二叉树
/// nullopt 表示空节点
inline TreeNode* buildTree(const std::vector<std::optional<int>>& values) {
    if (values.empty() || !values[0].has_value()) return nullptr;

    auto* root = new TreeNode(values[0].value());
    std::queue<TreeNode*> q;
    q.push(root);
    size_t i = 1;

    while (!q.empty() && i < values.size()) {
        TreeNode* parent = q.front();
        q.pop();

        if (i < values.size() && values[i].has_value()) {
            parent->left = new TreeNode(values[i].value());
            q.push(parent->left);
        }
        ++i;

        if (i < values.size() && values[i].has_value()) {
            parent->right = new TreeNode(values[i].value());
            q.push(parent->right);
        }
        ++i;
    }
    return root;
}

/// 将二叉树转换为层序数组（含 null，尾部 null 已修剪）
inline std::vector<std::optional<int>> treeToArray(const TreeNode* root) {
    if (!root) return {};

    std::vector<std::optional<int>> result;
    std::queue<const TreeNode*> q;
    q.push(root);

    while (!q.empty()) {
        const TreeNode* node = q.front();
        q.pop();

        if (!node) {
            result.push_back(std::nullopt);
            continue;
        }

        result.push_back(node->val);
        q.push(node->left);
        q.push(node->right);
    }

    // 删除末尾连续的 null
    while (!result.empty() && !result.back().has_value()) {
        result.pop_back();
    }
    return result;
}

/// 将二叉树输出为 LeetCode 格式字符串 "[1,2,null,3]"
inline std::string treeToString(const TreeNode* root) {
    auto arr = treeToArray(root);
    std::string s = "[";
    for (size_t i = 0; i < arr.size(); ++i) {
        if (i > 0) s += ",";
        if (arr[i].has_value()) {
            s += std::to_string(arr[i].value());
        } else {
            s += "null";
        }
    }
    s += "]";
    return s;
}

/// 释放二叉树内存
inline void freeTree(TreeNode* root) {
    if (!root) return;
    freeTree(root->left);
    freeTree(root->right);
    delete root;
}

#endif