#include "cpp/list_node.hpp"
#include "cpp/tree_node.hpp"
#include "cpp/codec.hpp"
#include <iostream>
#include <cassert>

int main() {
    // 链表测试
    auto* l = buildList({1, 2, 3});
    std::string ls = listToString(l);
    assert(ls == "[1,2,3]");
    std::cout << "list: " << ls << std::endl;
    assert(l->val == 1 && l->next->val == 2 && l->next->next->val == 3);
    freeList(l);

    auto* ld = deserializeList("[4,5,6]");
    assert(listToArray(ld).size() == 3);
    freeList(ld);

    // 树测试
    auto* t = buildTree({1, std::nullopt, 2, 3});
    std::string ts = treeToString(t);
    assert(ts == "[1,null,2,3]");
    std::cout << "tree: " << ts << std::endl;
    freeTree(t);

    auto* td = deserializeTree("[1,null,2,null,3]");
    assert(td->val == 1 && td->right->val == 2 && td->right->right->val == 3);
    freeTree(td);

    std::cout << "C++ All OK" << std::endl;
    return 0;
}