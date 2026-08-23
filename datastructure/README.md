# 数据结构学习目录

这个目录集中存放用于学习的数据结构实现，避免与 LeetCode 题目目录混在一起。

| 目录 | 数据结构 | 主要用途 |
| --- | --- | --- |
| [`sortedlist`](./sortedlist/) | 基于跳表的有序列表 | 动态有序集合、按下标查询、快速插入和删除 |
| [`trie`](./trie/) | 字典树 | 单词查询、前缀匹配、前缀计数 |
| [`deque`](./deque/) | 基于双向链表的双端队列 | 两端插入删除、BFS、滑动窗口 |
| [`fenwicktree`](./fenwicktree/) | 树状数组 | 单点更新、前缀和、区间和 |
| [`segmenttree`](./segmenttree/) | 线段树 | 区间更新、区间查询（和/最小值/最大值）、懒标记 |

每个子目录都包含：

- 数据结构实现。
- 中文代码注释。
- 单元测试和边界测试。
- 独立的中文 `README.md` 教程。
- 普通 `String()` 和底层结构 `DebugString()`，适合观察数据与内部结构。

运行全部数据结构测试：

```bash
go test ./datastructure/...
```

执行静态检查：

```bash
go vet ./datastructure/...
```
