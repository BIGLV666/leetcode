# Go Deque：基于 `container/list` 的双端队列

## 1. 什么是 Deque

Deque 是 Double-Ended Queue 的缩写，中文通常称为双端队列。它允许在队首和队尾进行插入、删除和查看：

```text
队首 <-> [元素] <-> [元素] <-> [元素] <-> 队尾
```

本实现基于 Go 标准库 `container/list`，并使用泛型，因此可以保存任意类型：

```go
intDeque := deque.New[int]()
stringDeque := deque.New[string]()
```

`Deque[T]` 的零值也可以直接使用。

## 2. API

| 方法 | 说明 | 复杂度 |
| --- | --- | --- |
| `PushFront(value)` | 插入队首 | `O(1)` |
| `PushBack(value)` | 插入队尾 | `O(1)` |
| `PopFront()` | 删除并返回队首 | `O(1)` |
| `PopBack()` | 删除并返回队尾 | `O(1)` |
| `Front()` | 查看队首但不删除 | `O(1)` |
| `Back()` | 查看队尾但不删除 | `O(1)` |
| `Len()` | 返回元素数量 | `O(1)` |
| `Empty()` | 判断是否为空 | `O(1)` |
| `Clear()` | 清空队列 | `O(1)` |
| `Values()` | 返回队首到队尾的快照 | `O(n)` |
| `String()` | 返回普通字符串表示 | `O(n)` |

## 3. 为什么使用 `container/list`

Go 的 `container/list` 是双向链表。它的 `Element` 保存前后指针，因此可以在已知节点的情况下常数时间删除，并且头尾节点可以直接访问：

```go
type Deque[T any] struct {
	items *list.List
}
```

队首和队尾操作分别对应标准库方法：

```go
deque.items.PushFront(value)
deque.items.PushBack(value)
deque.items.Front()
deque.items.Back()
deque.items.Remove(element)
```

与切片相比，双向链表不需要在队首删除时搬移全部元素，因此 `PopFront` 是 `O(1)`。代价是每个元素都需要一个链表节点，并且链表节点的内存局部性通常不如切片。

## 4. 基本使用

```go
package main

import (
	"fmt"

	"leetcode/datastructure/deque"
)

func main() {
	queue := deque.New[int]()

	queue.PushBack(2)
	queue.PushFront(1)
	queue.PushBack(3)

	fmt.Println(queue.Values()) // [1 2 3]

	front, _ := queue.PopFront()
	back, _ := queue.PopBack()
	fmt.Println(front, back) // 1 3
	fmt.Println(queue.String()) // [2]
}
```

`Front`、`Back`、`PopFront` 和 `PopBack` 都返回 `(value, ok)`。空队列时 `ok == false`，调用者不需要担心 panic：

```go
value, ok := queue.PopFront()
if !ok {
	// 队列为空。
}
_ = value
```

即使元素类型本身的零值是合法值，也必须通过 `ok` 判断操作是否成功。例如 `Deque[int]` 中的 `0` 可能是真实元素，不能单纯通过返回值判断队列是否为空。

## 5. 零值可用

以下写法无需调用构造函数：

```go
var queue deque.Deque[string]
queue.PushBack("hello")
queue.PushFront("前面")
```

第一次插入时，`init` 会创建底层链表：

```go
func (deque *Deque[T]) init() {
	if deque.items == nil {
		deque.items = list.New()
	}
}
```

只读方法会将 `items == nil` 的零值视为空队列，因此 `Len`、`Empty`、`Front`、`Back` 和 `Values` 都可以安全调用。

## 6. 清空和复用

`Clear` 调用链表的 `Init`，清空原有元素后保留底层链表对象：

```go
queue.Clear()
fmt.Println(queue.IsEmpty()) // true

queue.PushBack(100)
fmt.Println(queue.Values()) // [100]
```

清空后的队列可以继续使用。

## 7. 快照和字符串表示

`Values()` 会创建新切片：

```go
values := queue.Values()
values[0] = 999
```

修改 `values` 不会影响队列内部数据。这样可以避免调用者直接破坏队列结构。

`String()` 只展示从队首到队尾的逻辑顺序：

```go
queue.PushBack(1)
queue.PushBack(2)
fmt.Println(queue.String()) // [1, 2]
```

它通过 `fmt.Sprint` 格式化泛型元素，适合调试。对于复杂结构，建议调用者自行定义更明确的输出方式。

## 8. 边界情况

测试覆盖了以下情况：

- 零值 Deque 直接使用。
- 空队列查看和删除两端。
- 空队列 `Len` 和 `Empty`。
- 只有一个元素时从两端删除。
- 队首插入和队尾插入交替进行。
- 队首删除和队尾删除交替进行。
- 重复值、负数、零和大整数。
- Unicode 字符串元素。
- `Values` 返回快照而非内部切片。
- `Clear` 后重新插入和使用。

运行测试：

```bash
go test ./deque
```

## 9. 正确性不变量

使用 `container/list` 后，双端队列主要需要保持以下语义：

1. `Len()` 等于底层链表中的元素数。
2. `Front()` 始终对应 `Values()[0]`。
3. `Back()` 始终对应 `Values()[Len()-1]`。
4. `PopFront` 返回并删除当前队首。
5. `PopBack` 返回并删除当前队尾。
6. 空队列的查看和删除操作都返回 `ok == false`。
7. `Values()` 的修改不会影响队列。

## 10. 适用场景

Deque 常用于：

- BFS 的队列。
- 滑动窗口。
- 单调队列。
- 需要两端操作的缓存结构。
- 任务调度。
- 回文判断。

例如使用双端队列判断字符串是否为回文：

```go
queue := deque.New[rune]()
for _, char := range text {
	queue.PushBack(char)
}

for queue.Len() > 1 {
	left, _ := queue.PopFront()
	right, _ := queue.PopBack()
	if left != right {
		return false
	}
}
return true
```

## 11. 当前实现限制

- 基于链表的 Deque 不是并发安全的。
- 每个元素有链表节点分配和指针开销。
- 只提供按两端操作，没有提供按下标随机访问。
- 如果主要需求是连续内存、批量遍历或较少的队首删除，切片实现可能更合适。
