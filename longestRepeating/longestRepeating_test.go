package longestRepeating

import (
	"reflect"
	"testing"
)

func TestLongestRepeating(t *testing.T) {
	tests := []struct {
		name            string
		s               string
		queryCharacters string
		queryIndices    []int
		want            []int
	}{
		{
			name:            "示例1",
			s:               "babacc",
			queryCharacters: "bcb",
			queryIndices:    []int{1, 3, 3},
			want:            []int{3, 3, 4},
		},
		{
			name:            "示例2",
			s:               "abyzz",
			queryCharacters: "aa",
			queryIndices:    []int{2, 1},
			want:            []int{2, 3},
		},
		{
			name:            "单字符",
			s:               "a",
			queryCharacters: "b",
			queryIndices:    []int{0},
			want:            []int{1},
		},
		{
			name:            "全部相同字符",
			s:               "aaaa",
			queryCharacters: "b",
			queryIndices:    []int{2},
			want:            []int{2},
		},
		{
			name:            "修改后变成全部相同",
			s:               "abaa",
			queryCharacters: "a",
			queryIndices:    []int{1},
			want:            []int{4},
		},
		{
			name:            "多次修改同一位置",
			s:               "aabbcc",
			queryCharacters: "abc",
			queryIndices:    []int{2, 2, 2},
			want:            []int{3, 2, 2},
		},
		{
			name:            "修改首尾",
			s:               "abc",
			queryCharacters: "ba",
			queryIndices:    []int{0, 2},
			want:            []int{2, 2},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			got := longestRepeating1(tt.s, tt.queryCharacters, tt.queryIndices)
			if !reflect.DeepEqual(got, tt.want) {
				t.Errorf("longestRepeating() = %v, want %v", got, tt.want)
			}
		})
	}
}

// 测试线段树节点合并逻辑
func TestMerge(t *testing.T) {
	tests := []struct {
		name  string
		left  node
		right node
		want  node
	}{
		{
			name: "中间可以合并",
			left: node{
				leftChar:  'a',
				rightChar: 'b',
				leftLen:   1,
				rightLen:  2,
				maxLen:    2,
				length:    3,
			},
			right: node{
				leftChar:  'b',
				rightChar: 'c',
				leftLen:   2,
				rightLen:  1,
				maxLen:    2,
				length:    3,
			},
			want: node{
				leftChar:  'a',
				rightChar: 'c',
				leftLen:   1,
				rightLen:  1,
				maxLen:    4, // 左边的2个b + 右边的2个b
				length:    6,
			},
		},
		{
			name: "中间不能合并",
			left: node{
				leftChar:  'a',
				rightChar: 'a',
				leftLen:   2,
				rightLen:  2,
				maxLen:    2,
				length:    2,
			},
			right: node{
				leftChar:  'b',
				rightChar: 'b',
				leftLen:   2,
				rightLen:  2,
				maxLen:    2,
				length:    2,
			},
			want: node{
				leftChar:  'a',
				rightChar: 'b',
				leftLen:   2,
				rightLen:  2,
				maxLen:    2,
				length:    4,
			},
		},
		{
			name: "左子树全是同一字符且能延伸",
			left: node{
				leftChar:  'a',
				rightChar: 'a',
				leftLen:   3,
				rightLen:  3,
				maxLen:    3,
				length:    3,
			},
			right: node{
				leftChar:  'a',
				rightChar: 'b',
				leftLen:   2,
				rightLen:  1,
				maxLen:    2,
				length:    3,
			},
			want: node{
				leftChar:  'a',
				rightChar: 'b',
				leftLen:   5, // 左边3个a + 右边开头2个a
				rightLen:  1,
				maxLen:    5,
				length:    6,
			},
		},
		{
			name: "右子树全是同一字符且能延伸",
			left: node{
				leftChar:  'a',
				rightChar: 'b',
				leftLen:   1,
				rightLen:  2,
				maxLen:    2,
				length:    3,
			},
			right: node{
				leftChar:  'b',
				rightChar: 'b',
				leftLen:   3,
				rightLen:  3,
				maxLen:    3,
				length:    3,
			},
			want: node{
				leftChar:  'a',
				rightChar: 'b',
				leftLen:   1,
				rightLen:  5, // 右边3个b + 左边结尾2个b
				maxLen:    5,
				length:    6,
			},
		},
		{
			name: "左子树为空",
			left: node{},
			right: node{
				leftChar:  'a',
				rightChar: 'a',
				leftLen:   2,
				rightLen:  2,
				maxLen:    2,
				length:    2,
			},
			want: node{
				leftChar:  'a',
				rightChar: 'a',
				leftLen:   2,
				rightLen:  2,
				maxLen:    2,
				length:    2,
			},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			got := merge(tt.left, tt.right)
			if !reflect.DeepEqual(got, tt.want) {
				t.Errorf("merge() = %+v, want %+v", got, tt.want)
			}
		})
	}
}

// 基准测试
func BenchmarkLongestRepeating(b *testing.B) {
	s := "babacc"
	queryCharacters := "bcb"
	queryIndices := []int{1, 3, 3}

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		longestRepeating(s, queryCharacters, queryIndices)
	}
}
