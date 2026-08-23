package longestRepeating

import "testing"

func TestMyTree(t *testing.T) {
	// 测试用例1：LeetCode官方示例
	s := "babaaa"
	queryCharacters := "bcb"
	queryIndices := []int{1, 3, 3}
	expected := []int{3, 3, 4}

	result := longestRepeating1(s, queryCharacters, queryIndices)

	for i := 0; i < len(result); i++ {
		if result[i] != expected[i] {
			t.Errorf("查询 %d: got %d, want %d", i, result[i], expected[i])
			t.Logf("修改后的字符串应该是: %v", s)
		}
	}
}

func TestMyTreeSimple(t *testing.T) {
	// 简单测试
	s := "aaaa"
	tree := NewTree(s)

	if got := tree.getmaxLen(); got != 4 {
		t.Errorf("初始maxLen错误: got %d, want 4", got)
	}


	tree.update(1, 0, 3, 1, 'b')
	if got := tree.getmaxLen(); got != 2 {
		t.Errorf(" got %d, want 2", got)
	}
}

func TestMyTreeDebug(t *testing.T) {
	s := "abcd"
	tree := NewTree(s)

	t.Logf("初始字符串: %s", s)
	t.Logf("初始maxLen: %d", tree.getmaxLen())

	// 修改位置1为'a'，变成"aacd"
	tree.update(1, 0, 3, 1, 'a')
	t.Logf("修改位置1为'a'后: %s", string(tree.s))
	t.Logf("maxLen: %d (期望2)", tree.getmaxLen())

	// 打印树节点信息
	t.Logf("根节点: leftChar=%c, rightChar=%c, leftLen=%d, rightLen=%d, maxLen=%d",
		tree.tree[1].leftChar, tree.tree[1].rightChar,
		tree.tree[1].leftLen, tree.tree[1].rightLen, tree.tree[1].maxLen)
}

