package longestRepeating

type Tree struct {
	s    []byte
	tree []Node
}

type Node struct {
	leftChar  byte
	rightChar byte
	leftLen   int
	rightLen  int
	maxLen    int
	length    int
}

func marage(left, right Node) Node {
	if left.length == 0 {
		return right
	}
	if right.length == 0 {
		return left
	}
	NewNode := Node{
		leftChar:  left.leftChar,
		rightChar: right.rightChar,
		length:    left.length + right.length,
	}
	NewNode.leftLen = left.leftLen
	if left.length == left.leftLen && left.rightChar == right.leftChar {
		NewNode.leftLen += right.leftLen
	}
	NewNode.rightLen = right.rightLen // 修复：应该是right的右端长度
	if right.length == right.rightLen && left.rightChar == right.leftChar {
		NewNode.rightLen += left.rightLen // 修复：应该加上left的右端长度
	}
	// 修复：maxLen应该考虑三种情况
	NewNode.maxLen = max(left.maxLen, right.maxLen)
	if left.rightChar == right.leftChar {
		NewNode.maxLen = max(NewNode.maxLen, right.leftLen+left.rightLen)
	}
	return NewNode
}

func (t *Tree) build(index, l, r int) {
	if l > r {
		return // 非法区间，直接返回避免死循环
	}
	if l == r {
		t.tree[index] = Node{
			leftChar:  t.s[l],
			rightChar: t.s[l],
			leftLen:   1,
			rightLen:  1,
			maxLen:    1,
			length:    1,
		}
		return // 修复：必须return，否则会继续递归
	}
	mid := l + (r-l)/2
	t.build(index*2, l, mid)
	t.build(index*2+1, mid+1, r)
	t.tree[index] = marage(t.tree[index*2], t.tree[index*2+1])
}

func (t *Tree) update(index, l, r, pos int, ch byte) {
	if l > r {
		return // 非法区间，直接返回避免死循环
	}
	if l == r {
		t.s[pos] = ch
		t.tree[index] = Node{
			leftChar:  ch,
			rightChar: ch,
			leftLen:   1,
			rightLen:  1,
			maxLen:    1,
			length:    1,
		}
		return // 修复：必须return
	}
	mid := l + (r-l)/2
	if mid >= pos {
		t.update(index*2, l, mid, pos, ch)
	} else {
		t.update(index*2+1, mid+1, r, pos, ch)
	}
	t.tree[index] = marage(t.tree[index*2], t.tree[index*2+1])
}

func NewTree(s string) *Tree {
	bytes := []byte(s)
	tree := &Tree{
		s:    bytes,
		tree: make([]Node, 4*len(s)),
	}
	if len(s) > 0 {
		tree.build(1, 0, len(s)-1)
	}
	return tree
}

func (t *Tree) getmaxLen() int {
	return t.tree[1].maxLen
}

func longestRepeating1(s string, queryCharacters string, queryIndices []int) []int {
	st := NewTree(s)
	result := make([]int, len(queryIndices))
	for i, pos := range queryIndices {
		st.update(1, 0, len(s)-1, pos, queryCharacters[i])
		result[i] = st.getmaxLen()
	}
	return result
}
