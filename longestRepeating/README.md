# LeetCode 2213: 由单个字符重复的最长子字符串 - 线段树详解

## 问题理解

给定字符串 `s = "babacc"`，每次修改一个位置的字符后，查询**整个字符串中**最长的连续相同字符段长度。

```
初始: "babacc"  → 最长连续段 "cc" = 2
修改 s[1]='b': "bbbacc"  → 最长连续段 "bbb" = 3
修改 s[3]='c': "bbbccc"  → 最长连续段 "bbb" 或 "ccc" = 3
修改 s[3]='b': "bbbbcc"  → 最长连续段 "bbbb" = 4
```

## 为什么用线段树？

**暴力做法**：每次修改后遍历整个字符串统计 → O(n)，k 次查询总共 O(nk)，会超时。

**线段树优势**：
- 修改：O(log n)
- 查询：O(1)（直接读根节点）
- 总时间：O(n + k log n)

## 核心思想：线段树节点维护什么信息？

每个节点代表一个区间 `[l, r]`，需要维护 6 个信息：

```go
type node struct {
    leftChar  byte // 区间最左边的字符是什么
    rightChar byte // 区间最右边的字符是什么
    leftLen   int  // 从左端开始，最多能连续多少个相同字符
    rightLen  int  // 从右端开始，最多能连续多少个相同字符
    maxLen    int  // 区间内部，最长连续段的长度
    length    int  // 区间总长度
}
```

### 示例：叶子节点（单个字符）

```
字符串: "a"
节点信息:
  leftChar = 'a'
  rightChar = 'a'
  leftLen = 1    (从左边开始1个a)
  rightLen = 1   (从右边开始1个a)
  maxLen = 1     (最长连续1个)
  length = 1
```

### 示例：区间 "bbb"

```
字符串: "bbb"
节点信息:
  leftChar = 'b'
  rightChar = 'b'
  leftLen = 3    (从左边开始3个b)
  rightLen = 3   (从右边开始3个b)
  maxLen = 3     (最长连续3个)
  length = 3
```

### 示例：区间 "abc"

```
字符串: "abc"
节点信息:
  leftChar = 'a'
  rightChar = 'c'
  leftLen = 1    (从左边开始只有1个a)
  rightLen = 1   (从右边开始只有1个c)
  maxLen = 1     (最长连续只有1个)
  length = 3
```

## 关键：如何合并两个子节点？

假设有左子树区间 `[l, mid]` 和右子树区间 `[mid+1, r]`，要合并成父节点 `[l, r]`。

### 情况1：左右端字符不同

```
左子树: "aab"          右子树: "ccc"
  leftChar='a'           leftChar='c'
  rightChar='b'          rightChar='c'
  leftLen=2              leftLen=3
  rightLen=1             rightLen=3
  maxLen=2               maxLen=3

合并后: "aabccc"
  leftChar='a'         (继承左子树)
  rightChar='c'        (继承右子树)
  leftLen=2            (左边不能延伸到右边，因为 b≠c)
  rightLen=3           (右边不能延伸到左边，因为 c≠b)
  maxLen=3             (max(左边最长2, 右边最长3) = 3)
```

### 情况2：左右端字符相同 - 可以跨越合并！

```
左子树: "abb"          右子树: "bbc"
  leftChar='a'           leftChar='b'
  rightChar='b'          rightChar='c'
  leftLen=1              leftLen=2
  rightLen=2             rightLen=1
  maxLen=2               maxLen=2

合并后: "abbbbc"
  leftChar='a'
  rightChar='c'
  leftLen=1            (左边只有1个a，不能延伸)
  rightLen=1           (右边只有1个c，不能延伸)
  maxLen=4             (关键！左边的2个b + 右边的2个b = 4)
                       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                       因为 rightChar(左)='b' == leftChar(右)='b'
```

**合并规则（跨越中点）**：

```go
if left.rightChar == right.leftChar {
    // 可以把左边结尾的连续段 + 右边开头的连续段合并
    maxLen = max(maxLen, left.rightLen + right.leftLen)
}
```

### 情况3：左子树全是同一字符，且能和右边合并

```
左子树: "aaa"          右子树: "aab"
  leftChar='a'           leftChar='a'
  rightChar='a'          rightChar='b'
  leftLen=3              leftLen=2
  rightLen=3             rightLen=1
  maxLen=3               maxLen=2
  length=3               length=3

合并后: "aaaaab"
  leftChar='a'
  rightChar='b'
  leftLen=5            (左边3个a + 右边开头2个a = 5)
                       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                       因为左子树全是a (leftLen==length)
                       且 rightChar(左)='a' == leftChar(右)='a'
  rightLen=1
  maxLen=5
```

**合并规则（左边延伸）**：

```go
leftLen = left.leftLen
if left.leftLen == left.length && left.rightChar == right.leftChar {
    // 左子树全是同一字符，可以延伸到右子树
    leftLen += right.leftLen
}
```

### 情况4：右子树全是同一字符，且能和左边合并

```
左子树: "abb"          右子树: "bbb"
  leftChar='a'           leftChar='b'
  rightChar='b'          rightChar='b'
  leftLen=1              leftLen=3
  rightLen=2             rightLen=3
  maxLen=2               maxLen=3
  length=3               length=3

合并后: "abbbbb"
  leftChar='a'
  rightChar='b'
  leftLen=1
  rightLen=5           (右边3个b + 左边结尾2个b = 5)
                       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                       因为右子树全是b (rightLen==length)
                       且 rightChar(左)='b' == leftChar(右)='b'
  maxLen=5
```

**合并规则（右边延伸）**：

```go
rightLen = right.rightLen
if right.rightLen == right.length && left.rightChar == right.leftChar {
    // 右子树全是同一字符，可以延伸到左子树
    rightLen += left.rightLen
}
```

## 完整示例：构建 "babacc" 的线段树

```
字符串: b  a  b  a  c  c
索引:   0  1  2  3  4  5

叶子节点（最底层）：
[0,0]: b    [1,1]: a    [2,2]: b    [3,3]: a    [4,4]: c    [5,5]: c
left='b'    left='a'    left='b'    left='a'    left='c'    left='c'
right='b'   right='a'   right='b'   right='a'   right='c'   right='c'
lLen=1      lLen=1      lLen=1      lLen=1      lLen=1      lLen=1
rLen=1      rLen=1      rLen=1      rLen=1      rLen=1      rLen=1
max=1       max=1       max=1       max=1       max=1       max=1

第二层（合并两两叶子）：
[0,1]: "ba"                [2,3]: "ba"                [4,5]: "cc"
left='b', right='a'        left='b', right='a'        left='c', right='c'
lLen=1, rLen=1             lLen=1, rLen=1             lLen=2, rLen=2
max=1                      max=1                      max=2 ← 两个c合并
(b≠a 不能合并)             (b≠a 不能合并)             (c==c 能合并!)

第三层：
[0,3]: "baba"                      [4,5]: "cc"
left='b', right='a'                (已经在上层)
lLen=1, rLen=1
max=1
(右边是a，左边是a，能合并? 不能，因为 [0,1]的rightChar='a', [2,3]的leftChar='b')

根节点：
[0,5]: "babacc"
left='b', right='c'
lLen=1
rLen=2
max=2  (最长连续段是 "cc")
```

## 修改操作：单点更新

当修改 `s[1] = 'b'` 时：

1. 定位到叶子节点 `[1,1]`，更新为 `'b'`
2. 向上递归更新所有祖先节点：
   - `[0,1]`: "ba" → "bb" (maxLen: 1→2)
   - `[0,3]`: "baba" → "bbba" (maxLen: 1→3)
   - `[0,5]`: "babacc" → "bbbacc" (maxLen: 2→3)

每次修改只需更新 O(log n) 个节点。

## 查询操作

直接读取根节点的 `maxLen` 字段 → O(1)

## 时间复杂度

- 构建线段树：O(n)
- 单次修改：O(log n)（更新从叶子到根的路径）
- 单次查询：O(1)
- 总时间：O(n + k log n)，其中 k 是查询次数

## 对比通用线段树

| 类型 | 节点维护 | 合并操作 | 适用场景 |
|------|----------|----------|----------|
| 通用线段树 | sum/min/max | 简单加法或比较 | 区间和、区间最值 |
| 字符线段树 | 6个字段 | 复杂的字符匹配和延伸逻辑 | 连续段、区间特征 |

通用线段树的合并很简单：

```go
node.sum = left.sum + right.sum
node.min = min(left.min, right.min)
```

字符线段树的合并需要考虑：
1. 字符是否相同？
2. 能否跨越合并？
3. 能否向左/右延伸？

## 何时使用这种线段树？

适用于需要维护**区间连续性质**的问题：
- 最长连续相同元素
- 最长递增/递减子段
- 区间内的连续性判断

如果只是简单的区间和、最值，用通用线段树或树状数组即可。
