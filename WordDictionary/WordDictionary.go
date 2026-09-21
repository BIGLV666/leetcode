package WordDictionary

// WordDictionary 使用字典树保存单词，并支持 '.' 匹配任意一个字符。
type WordDictionary struct {
	root *Tree
}

// Tree 是字典树节点；word 表示从根到当前节点的路径是否构成完整单词。
type Tree struct {
	children map[rune]*Tree
	word     bool
}

// Constructor 创建一个空的单词字典。
func Constructor() WordDictionary {
	return WordDictionary{root: newTree()}
}

func newTree() *Tree {
	return &Tree{children: make(map[rune]*Tree)}
}

// AddWord 将 word 的每个字符逐层写入字典树，并标记单词结尾。
func (this *WordDictionary) AddWord(word string) {
	if this.root == nil {
		this.root = newTree()
	}

	current := this.root
	for _, ch := range word {
		if current.children[ch] == nil {
			current.children[ch] = newTree()
		}
		current = current.children[ch]
	}
	current.word = true
}

// Search 查询精确单词或包含 '.' 通配符的模式是否存在。
func (this *WordDictionary) Search(word string) bool {
	if this.root == nil {
		return false
	}
	return this.dfs(this.root, []rune(word), 0)
}

// dfs 在字典树上匹配模式；遇到 '.' 时枚举当前节点的所有子节点。
func (this *WordDictionary) dfs(current *Tree, chars []rune, i int) bool {
	if i == len(chars) {
		return current.word
	}

	if chars[i] == '.' {
		for _, child := range current.children {
			if this.dfs(child, chars, i+1) {
				return true
			}
		}
		return false
	}

	child := current.children[chars[i]]
	return child != nil && this.dfs(child, chars, i+1)
}
