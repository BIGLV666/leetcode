package longestRepeating

// 线段树节点：维护区间的连续字符信息
type node struct {
	leftChar  byte // 区间左端字符
	rightChar byte // 区间右端字符
	leftLen   int  // 从左端开始的连续长度
	rightLen  int  // 从右端开始的连续长度
	maxLen    int  // 区间内最长连续长度
	length    int  // 区间总长度
}

// 合并两个子节点
// 关键：如果左子树右端字符 == 右子树左端字符，则中间可以合并
func merge(left, right node) node {
	if left.length == 0 {
		return right
	}
	if right.length == 0 {
		return left
	}

	n := node{
		leftChar:  left.leftChar,
		rightChar: right.rightChar,
		length:    left.length + right.length,
	}

	// 左端连续长度：如果左子树全是同一字符且与右子树左端相同，可以延伸
	n.leftLen = left.leftLen
	if left.leftLen == left.length && left.rightChar == right.leftChar {
		n.leftLen += right.leftLen
	}

	// 右端连续长度：如果右子树全是同一字符且与左子树右端相同，可以延伸
	n.rightLen = right.rightLen
	if right.rightLen == right.length && left.rightChar == right.leftChar {
		n.rightLen += left.rightLen
	}

	// 区间最长：三种情况取最大值
	// 1. 左子树内部最长
	// 2. 右子树内部最长
	// 3. 跨越中点合并（左子树右端 + 右子树左端）
	n.maxLen = max(left.maxLen, right.maxLen)
	if left.rightChar == right.leftChar {
		n.maxLen = max(n.maxLen, left.rightLen+right.leftLen)
	}

	return n
}

// 字符线段树
type segTree struct {
	s    []byte // 原字符串
	tree []node // 线段树数组，tree[1] 是根节点
}

// 从字符串构建线段树
func newSegTree(s string) *segTree {
	n := len(s)
	st := &segTree{
		s:    []byte(s),
		tree: make([]node, 4*n),
	}
	if n > 0 {
		st.build(1, 0, n-1)
	}
	return st
}

// 递归构建线段树
// idx: 当前节点在 tree 中的下标
// l, r: 当前节点管理的区间 [l, r]（闭区间）
func (st *segTree) build(idx, l, r int) {
	if l > r {
		// 非法区间（如空字符串导致的 l>r），直接返回避免死循环
		return
	}
	if l == r {
		// 叶子节点：单个字符
		st.tree[idx] = node{
			leftChar:  st.s[l],
			rightChar: st.s[l],
			leftLen:   1,
			rightLen:  1,
			maxLen:    1,
			length:    1,
		}
		return
	}
	mid := (l + r) / 2
	st.build(idx*2, l, mid)
	st.build(idx*2+1, mid+1, r)
	st.tree[idx] = merge(st.tree[idx*2], st.tree[idx*2+1])
}

// 单点更新：将位置 pos 的字符修改为 ch
func (st *segTree) update(idx, l, r, pos int, ch byte) {
	if l > r {
		// 非法区间，直接返回避免死循环
		return
	}
	if l == r {
		// 叶子节点：直接修改
		st.s[pos] = ch
		st.tree[idx] = node{
			leftChar:  ch,
			rightChar: ch,
			leftLen:   1,
			rightLen:  1,
			maxLen:    1,
			length:    1,
		}
		return
	}
	mid := (l + r) / 2
	if pos <= mid {
		st.update(idx*2, l, mid, pos, ch)
	} else {
		st.update(idx*2+1, mid+1, r, pos, ch)
	}
	st.tree[idx] = merge(st.tree[idx*2], st.tree[idx*2+1])
}

// 查询全局最长连续段
func (st *segTree) queryMax() int {
	if len(st.s) == 0 {
		return 0
	}
	return st.tree[1].maxLen
}

// LeetCode 2213. 由单个字符重复的最长子字符串
// 题目要求：每次单点修改后，查询全局最长连续相同字符段的长度
func longestRepeating(s string, queryCharacters string, queryIndices []int) []int {
	st := newSegTree(s)
	k := len(queryIndices)
	result := make([]int, k)

	for i := 0; i < k; i++ {
		pos := queryIndices[i]
		ch := queryCharacters[i]
		st.update(1, 0, len(s)-1, pos, ch)
		result[i] = st.queryMax()
	}

	return result
}

func max(a, b int) int {
	if a > b {
		return a
	}
	return b
}
