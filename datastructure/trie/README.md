# Go 字典树（Trie）完整讲解

## 1. 什么是字典树

字典树是一种专门处理字符串集合的数据结构。它把字符串按字符拆开，每个字符对应一条边，从根节点到某个节点的一条路径就代表一个字符串。

例如插入 `app`、`apple` 和 `apply` 后，前三个字符会共享路径：

```text
root -> a -> p -> p
                    |-> l -> e   （apple）
                    |-> l -> y   （apply）
                    └             （app）
```

节点是否是完整单词不能只靠“节点存在”判断。比如插入 `apple` 后，`app` 对应的节点存在，但只有在插入 `app` 时，该节点才会被标记为完整单词。

## 2. 当前实现支持的操作

| 方法                  | 作用                   | 时间复杂度        |
|-----------------------|------------------------|-------------------|
| `Insert(word)`        | 插入一个单词           | `O(L)`            |
| `Search(word)`        | 查询完整单词           | `O(L)`            |
| `Delete(word)`        | 删除一个完整单词       | `O(L)`            |
| `StartsWith(prefix)`  | 查询前缀是否存在       | `O(P)`            |
| `PrefixCount(prefix)` | 统计某个前缀下的单词数 | `O(P)`            |
| `Count()`             | 统计不同单词数         | `O(1)`            |
| `Words()`             | 返回所有单词的排序快照 | `O(N + 总字符数)` |

其中：

- `L` 是单词的 Unicode 字符数。
- `P` 是前缀的 Unicode 字符数。
- `N` 是不同单词的数量。

实现使用 `rune` 遍历字符串，因此中文、日文、表情符号和带重音字符都按 Unicode 字符处理，而不是错误地按 UTF-8 字节拆分。

## 3. 节点结构

核心节点定义如下：

```go
type node struct {
	children     map[rune]*node
	word         bool
	subtreeCount int
}
```

字段说明：

| 字段           | 含义                             |
|----------------|----------------------------------|
| `children`     | 当前字符到下一级节点的映射       |
| `word`         | 当前节点是否代表一个完整单词     |
| `subtreeCount` | 当前节点子树中不同完整单词的数量 |

`children` 使用 `map[rune]*node`，可以支持任意 Unicode 字符。使用数组只适合字符集固定且很小的场景；使用 `map`
更通用，但每个节点会有额外的哈希表开销。

## 4. 为什么需要 word 标记

假设只插入了 `apple`：

```text
root -> a -> p -> p -> l -> e
```

此时 `app` 的路径存在，但 `Search("app")` 必须返回 `false`，因为 `app` 不是已插入的完整单词。

如果后来再插入 `app`，只需要将 `p` 节点的 `word` 改为 `true`，不需要新增节点。

因此：

- 路径存在表示“可能是前缀”。
- `word == true` 才表示“是完整单词”。

## 5. 插入单词

插入过程从根节点开始，依次处理每个 Unicode 字符：

```go
func (trie *Trie) Insert(word string) {
	trie.init()
	current := trie.root
	path := []*node{current}
	for _, char := range word {
		if current.children[char] == nil {
			current.children[char] = newNode()
		}
		current = current.children[char]
		path = append(path, current)
	}
	if current.word {
		return
	}
	current.word = true
	trie.count++
	for _, item := range path {
		item.subtreeCount++
	}
}
```

保存 `path` 有两个作用：

1. 插入成功后，让路径上每个节点的 `subtreeCount` 加一。
2. 删除时可以复用相同思路，从路径尾部向根部清理无用节点。

重复插入不会增加 `count`，也不会重复增加任何 `subtreeCount`。这是因为代码在修改计数前检查了 `current.word`。

空字符串也可以插入。空字符串的路径只有根节点，因此插入空字符串只会将 `root.word` 设置为 `true`，并使根节点的计数加一。

## 6. 查询完整单词

查询先沿字符路径查找节点：

```go
func (trie *Trie) Search(word string) bool {
	current := trie.find(word)
	return current != nil && current.word
}
```

`find` 找不到路径时返回 `nil`。即使路径存在，也必须进一步检查 `word` 标记。

下面的结果是不同的：

```go
trie.Insert("apple")

trie.Search("app")   // false，只有前缀
trie.StartsWith("app") // true，存在以 app 开头的单词
trie.Search("apple") // true，完整单词
```

## 7. 前缀查询和前缀计数

`StartsWith(prefix)` 只需要判断前缀路径是否存在，并且该节点的 `subtreeCount` 大于 0：

```go
func (trie *Trie) StartsWith(prefix string) bool {
	current := trie.find(prefix)
	return current != nil && current.subtreeCount > 0
}
```

`PrefixCount(prefix)` 直接返回前缀节点维护的子树计数：

```go
func (trie *Trie) PrefixCount(prefix string) int {
	current := trie.find(prefix)
	if current == nil {
		return 0
	}
	return current.subtreeCount
}
```

例如：

```go
for _, word := range []string{"app", "apple", "apply", "banana"} {
	trie.Insert(word)
}

trie.PrefixCount("app") // 3
trie.PrefixCount("ban") // 1
trie.PrefixCount("x")   // 0
```

空前缀对应根节点，因此 `PrefixCount("")` 等于 Trie 中不同完整单词的总数。

## 8. 删除单词

删除分为三步：

1. 找到完整单词对应的节点。
2. 取消 `word` 标记，并沿路径递减计数。
3. 从叶子向根清理不再属于任何单词的节点。

关键代码如下：

```go
current.word = false
trie.count--
for _, item := range path {
	item.subtreeCount--
}

for i := len(chars) - 1; i >= 0; i-- {
	child := path[i+1]
	parent := path[i]
	if child.word || child.subtreeCount > 0 || len(child.children) > 0 {
		break
	}
	delete(parent.children, chars[i])
}
```

不能简单地把整条路径删除。例如同时存在 `car` 和 `card` 时，删除 `car` 只能取消 `car` 的完整单词标记，不能删除 `c -> a -> r`
，否则 `card` 也会被破坏。

只有当节点同时满足以下条件时才能清理：

- 不是另一个完整单词。
- 子树中没有其他完整单词。
- 没有任何子节点。

## 9. 重复值和前缀关系

当前 Trie 是集合，不是多重集合：

```go
trie.Insert("go")
trie.Insert("go")

trie.Count()       // 1
trie.PrefixCount("g") // 1
trie.Delete("go") // true
trie.Delete("go") // false
```

如果需要统计重复插入次数，可以把节点的 `word bool` 改成计数器，并相应修改 `subtreeCount` 的含义；当前实现不保存重复次数。

## 10. Unicode 处理

Go 的 `string` 是 UTF-8 字节序列，不能直接按下标把一个中文字符当作一个字符。例如：

```go
for _, char := range "中文🙂" {
	// char 的类型是 rune，依次得到 中、文、🙂。
}
```

实现使用：

```go
for _, char := range word {
	current = current.children[char]
}
```

因此以下单词可以正常共存：

```go
trie.Insert("中文")
trie.Insert("中文题")
trie.Insert("🙂")
trie.Insert("é")
```

需要注意，`rune` 表示 Unicode 码点；一些由多个码点组成的视觉字符序列仍可能被拆成多个 rune，这是 Go 字符串处理的正常语义。

## 11. 完整使用示例

当前模块的导入路径是 `leetcode/datastructure/trie`：

```go
package main

import (
	"fmt"

	"leetcode/datastructure/trie"
)

func main() {
	words := trie.New()
	for _, word := range []string{"go", "gopher", "中文", "中文题"} {
		words.Insert(word)
	}

	fmt.Println(words.Search("go"))          // true
	fmt.Println(words.Search("g"))           // false
	fmt.Println(words.StartsWith("go"))      // true
	fmt.Println(words.PrefixCount("中文"))   // 2
	fmt.Println(words.Words())               // [go gopher 中文 中文题]
	fmt.Println(words.Delete("go"))         // true
	fmt.Println(words.Search("gopher"))      // true
}
```

## 12. 边界情况

当前测试覆盖以下情况：

- 空 Trie 查询空字符串。
- 插入、查询和删除空字符串。
- 重复插入空字符串和普通字符串。
- 查询不存在的单词和不存在的前缀。
- 查询前缀但不把前缀误判成完整单词。
- 删除不存在的前缀。
- 删除同时是其他单词前缀的完整单词。
- 删除后缀单词但保留其前缀单词。
- 中文、英文、数字、标点、重音字符和表情符号。
- 很长的 Unicode 字符串。
- 重复删除同一个单词。
- 删除所有单词后重新插入。
- 2000 次重复的插入、删除和查询操作。
- 用参考 `map[string]bool` 验证计数、查询和单词列表。

运行测试：

```bash
go test ./trie
```

## 13. 正确性不变量

修改实现时，需要保持以下规则：

1. 根节点始终存在于已初始化的 Trie 中。
2. 从根到节点的路径唯一表示一个 Unicode 字符串。
3. `word == true` 的节点才代表完整单词。
4. `count` 等于 Trie 中不同完整单词数量。
5. 每个节点的 `subtreeCount` 等于该节点子树中完整单词的数量。
6. `subtreeCount` 为 0 的节点不能保留有效单词或有效子树。
7. 删除一个单词不能影响它的其他前缀或后缀单词。

最容易出错的是第 5 条。如果只维护 `word` 而忘记更新祖先节点的 `subtreeCount`，普通 `Search` 可能仍然正确，但 `PrefixCount`
会错误。

## 14. 空间复杂度和适用场景

设所有单词路径上不同节点的总数为 `K`：

- 插入、查询、删除：`O(L)`。
- 前缀查询、前缀计数：`O(P)`。
- 空间复杂度：`O(K)`。

字典树适合：

- 自动补全。
- 搜索框前缀提示。
- 敏感词前缀匹配。
- 单词表查询。
- 统计某个前缀下的单词数量。

如果字符集非常小且固定，例如只处理小写英文字母，可以把 `map[rune]*node` 改成 `[26]*node`，查询速度可能更稳定，但会增加每个节点的固定空间，并且不再适合通用
Unicode。

## 15. 当前实现限制

为了保持学习代码简单，当前版本有以下限制：

- 只保存字符串集合，不保存与单词关联的额外值。
- 重复插入不会累计次数。
- `Words()` 会遍历整个 Trie，并将结果排序，适合导出或测试，不适合高频调用。
- 不是并发安全的数据结构，多 goroutine 访问时需要外部加锁。
- 没有实现模糊匹配、通配符匹配和编辑距离查询。

## 16. 字符串表示

`Trie` 同样提供正常表示和底层结构表示两种方法。

### 正常表示：String

`String()` 返回所有完整单词的排序结果：

```go
fmt.Println(trie.String()) // ["apple", "应用"]
```

该表示只关心有哪些完整单词，不展示共享前缀关系。

### 原始结构：DebugString

`DebugString()` 以树形结构展示节点：

```text
Trie{count: 2, root subtreeCount: 2}
根 (2)
├── 'a' (1)
│   └── 'p' (1)
└── '应' (1)
```

其中 `*` 表示当前节点是完整单词，括号中的数字表示该节点的 `subtreeCount`。子节点按 Unicode 码点排序，保证输出稳定。

## 17. 学习建议

建议按以下顺序阅读代码：

1. 先理解 `Search` 和 `find` 如何沿字符路径查找。
2. 再理解 `word` 为什么不能用“节点是否存在”替代。
3. 手动插入 `app` 和 `apple`，观察共享路径。
4. 手动删除 `app`，确认 `apple` 仍然存在。
5. 追踪 `subtreeCount` 在插入和删除时如何沿路径更新。
6. 最后阅读叶子节点清理逻辑，理解为什么不能无条件删除整条路径。

配合 `trie_test.go` 中的边界测试和随机参考集合测试，可以验证实现不仅能处理普通英文单词，也能正确处理空字符串、重复操作、Unicode
和复杂前缀关系。
