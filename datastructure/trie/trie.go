// Package trie 提供一个支持 Unicode 字符的字典树。
package trie

import (
	"sort"
	"strconv"
	"strings"
)

// node 是字典树中的一个节点。
// children 的键是一个 Unicode 码点，而不是一个字节，因此中文字符会
// 被当作一个完整字符处理。subtreeCount 用于快速统计某个前缀下的单词数。
type node struct {
	children     map[rune]*node
	word         bool
	subtreeCount int
}

// Trie 是一个集合型字典树。
//
// 相同单词重复插入只保留一份。空字符串也是合法单词，插入空字符串
// 会将根节点标记为完整单词。Trie 的零值可以直接使用。
type Trie struct {
	root  *node
	count int
}

// New 创建一个空的 Trie。
func New() *Trie {
	return &Trie{root: newNode()}
}

func newNode() *node {
	return &node{children: make(map[rune]*node)}
}

// init 延迟初始化零值 Trie。
func (trie *Trie) init() {
	if trie.root == nil {
		trie.root = newNode()
	}
}

// Count 返回 Trie 中不同完整单词的数量。
func (trie *Trie) Count() int { return trie.count }

// Insert 插入 word。
// 如果 word 已经存在，则不会重复计数。
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

// Search 判断完整单词 word 是否存在。
// 只有被 Insert 标记为完整单词的节点才会返回 true；单纯的前缀不会返回 true。
func (trie *Trie) Search(word string) bool {
	current := trie.find(word)
	return current != nil && current.word
}

// StartsWith 判断是否至少存在一个单词以 prefix 开头。
// 空前缀匹配所有单词；如果 Trie 为空，则返回 false。
func (trie *Trie) StartsWith(prefix string) bool {
	current := trie.find(prefix)
	return current != nil && current.subtreeCount > 0
}

// PrefixCount 返回以 prefix 开头的不同单词数量。
func (trie *Trie) PrefixCount(prefix string) int {
	current := trie.find(prefix)
	if current == nil {
		return 0
	}
	return current.subtreeCount
}

func (trie *Trie) find(text string) *node {
	if trie.root == nil {
		return nil
	}
	current := trie.root
	for _, char := range text {
		current = current.children[char]
		if current == nil {
			return nil
		}
	}
	return current
}

// Delete 删除完整单词 word，并返回该单词是否存在。
// 删除前缀不会误删其他更长单词；只有没有后继分支的节点才会被清理。
func (trie *Trie) Delete(word string) bool {
	if trie.root == nil {
		return false
	}

	current := trie.root
	path := []*node{current}
	chars := []rune(word)
	for _, char := range chars {
		current = current.children[char]
		if current == nil {
			return false
		}
		path = append(path, current)
	}
	if !current.word {
		return false
	}

	current.word = false
	trie.count--
	for _, item := range path {
		item.subtreeCount--
	}

	// 从叶子向根清理已经没有用途的节点，避免无意义地保留长路径。
	for i := len(chars) - 1; i >= 0; i-- {
		child := path[i+1]
		parent := path[i]
		if child.word || child.subtreeCount > 0 || len(child.children) > 0 {
			break
		}
		delete(parent.children, chars[i])
	}
	return true
}

// Words 返回所有完整单词的排序快照。
// 返回的切片与 Trie 内部无共享关系，调用者可以安全修改它。
func (trie *Trie) Words() []string {
	words := make([]string, 0, trie.count)
	if trie.root == nil {
		return words
	}
	var collect func(*node, []rune)
	collect = func(current *node, path []rune) {
		if current.word {
			words = append(words, string(path))
		}
		for char, child := range current.children {
			collect(child, append(path, char))
		}
	}
	collect(trie.root, nil)
	sort.Strings(words)
	return words
}

// String 返回字典树中所有完整单词的排序字符串表示。
// 该表示只展示逻辑内容，不展示节点之间的父子关系。
func (trie *Trie) String() string {
	words := trie.Words()
	var builder strings.Builder
	builder.WriteByte('[')
	for i, word := range words {
		if i > 0 {
			builder.WriteString(", ")
		}
		builder.WriteString(strconv.Quote(word))
	}
	builder.WriteByte(']')
	return builder.String()
}

// DebugString 返回字典树的树形结构。
// * 表示当前节点是一个完整单词，括号中的数字表示该节点子树中的单词数。
func (trie *Trie) DebugString() string {
	if trie.root == nil {
		return "Trie{count: 0}\n<未初始化>"
	}

	var builder strings.Builder
	builder.WriteString("Trie{count: ")
	builder.WriteString(strconv.Itoa(trie.count))
	builder.WriteString(", root subtreeCount: ")
	builder.WriteString(strconv.Itoa(trie.root.subtreeCount))
	builder.WriteString("}\n")
	builder.WriteString("根")
	if trie.root.word {
		builder.WriteString(" *")
	}
	builder.WriteString(" (")
	builder.WriteString(strconv.Itoa(trie.root.subtreeCount))
	builder.WriteString(")")
	trie.writeDebugChildren(&builder, trie.root, "")
	return builder.String()
}

func (trie *Trie) writeDebugChildren(builder *strings.Builder, parent *node, indent string) {
	chars := make([]rune, 0, len(parent.children))
	for char := range parent.children {
		chars = append(chars, char)
	}
	sort.Slice(chars, func(i, j int) bool { return chars[i] < chars[j] })
	for i, char := range chars {
		child := parent.children[char]
		last := i == len(chars)-1
		branch := "├── "
		nextIndent := indent + "│   "
		if last {
			branch = "└── "
			nextIndent = indent + "    "
		}
		builder.WriteByte('\n')
		builder.WriteString(indent)
		builder.WriteString(branch)
		builder.WriteString(strconv.QuoteRune(char))
		if child.word {
			builder.WriteString(" *")
		}
		builder.WriteString(" (")
		builder.WriteString(strconv.Itoa(child.subtreeCount))
		builder.WriteString(")")
		trie.writeDebugChildren(builder, child, nextIndent)
	}
}
