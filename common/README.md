# Go 共享基础设施

包 `common` 提供 LeetCode 题目中常用的数据结构定义、构建函数、序列化工具和通用测试方法。

## 导入

```go
import "leetcode/common"
```

## 链表

```go
// 定义
type ListNode struct {
    Val  int
    Next *ListNode
}

// 构建：从整数数组构建链表
head := common.BuildListNode([]int{1, 2, 3})

// 打印：将链表转换为整数数组
arr := common.PrintListNode(head) // []int{1, 2, 3}

// 字符串：输出 LeetCode 格式 "[1,2,3]"
s := head.String() // "[1,2,3]"
```

## 二叉树

```go
// 定义
type TreeNode struct {
    Val   int
    Left  *TreeNode
    Right *TreeNode
}

// 构建：从 LeetCode 层序数组构建（nil 表示空节点）
root := common.BuildTreeNode([]any{1, nil, 2, 3})

// 打印：层序遍历返回数组（含 "nil" 标记，尾部空值已修剪）
arr := common.PrintTreeNode(root) // []any{1, nil, 2, 3}

// 字符串：输出 LeetCode 格式 "[1,null,2,3]"
s := root.String() // "[1,null,2,3]"
```

## 二维数组

```go
// 从 JSON 字符串解析二维数组
grid := common.BuildIntArray("[[1,2],[3,4]]")

// 将二维数组序列化为 JSON 字符串
s := common.IntArrayToString(grid) // "[[1,2],[3,4]]"
```

## 通用测试工具

```go
func TestMyFunc(t *testing.T) {
    common.RunTests(t, myFunc, []common.TestCase{
        {
            Args:     []any{common.BuildIntArray("[[1,2],[3,4]]")},
            Expected: true,
        },
        {
            Args:     []any{common.BuildIntArray("[[5,6],[7,8]]")},
            Expected: false,
        },
    })
}
```

`TestCase` 结构体：

- `Args`：被测函数的参数列表（按顺序）
- `Expected`：期望返回值（单返回值）或 `[]any`（多返回值）

`RunTests` 使用反射调用函数，自动对比 `reflect.DeepEqual`。