package smallestfromleaf

import (
	"leetcode"
	"testing"
)

// 测试用例1: 完全二叉树，多个叶子节点，取字典序最小的路径
func Test1(t *testing.T) {
	leetcode.RunTests(
		t,
		smallestFromLeaf,
		[]leetcode.TestCase{
			{
				Args: []any{
					leetcode.BuildTreeNode([]any{0, 1, 2, 3, 4, 2, 3}),
				},
				Expected: "cca",
			},
		},
	)
}

// 测试用例2: 包含较大值节点(25='z')，验证边界字符处理
func Test2(t *testing.T) {
	leetcode.RunTests(
		t,
		smallestFromLeaf,
		[]leetcode.TestCase{
				{
				Args: []any{
					leetcode.BuildTreeNode([]any{25, 1, 3, 1, 3, 0, 2}),
				},
				Expected: "adz",
			},
		},
	)
}

// 测试用例3: 包含空节点的不完全二叉树，验证单分支路径处理
func Test3(t *testing.T) {
	leetcode.RunTests(
		t,
		smallestFromLeaf,
		[]leetcode.TestCase{
			{
				Args: []any{
					leetcode.BuildTreeNode([]any{2, 2, 1, nil, nil, 1, 0, nil, 0}),
				},
				Expected: "abbc",
			},
		},
	)
}

// 测试用例4: 单节点树，根节点即为叶节点
func Test4(t *testing.T) {
	leetcode.RunTests(
		t,
		smallestFromLeaf,
		[]leetcode.TestCase{
			{
				Args: []any{
					leetcode.BuildTreeNode([]any{0}),
				},
				Expected: "a",
			},
		},
	)
}
