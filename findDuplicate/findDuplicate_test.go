package findDuplicate

import (
	"reflect"
	"sort"
	"testing"
)

// TestFindDuplicate 校验重复内容文件的分组结果。
//
// findDuplicate 是遍历 map 输出分组的,组的先后顺序不确定,而题目本身也接受任意顺序,
// 所以这里先把「组内 + 组间」都排序成规范形式,再做比较——不能直接断言某一种顺序,
// 否则测试会随 map 迭代顺序时好时坏。
func TestFindDuplicate(t *testing.T) {
	cases := []struct {
		name  string
		paths []string
		want  [][]string
	}{
		{
			"官方示例",
			[]string{
				"root/a 1.txt(abcd) 2.txt(efgh)",
				"root/c 3.txt(abcd)",
				"root/c/d 4.txt(efgh)",
				"root 4.txt(efgh)",
			},
			[][]string{
				{"root/a/1.txt", "root/c/3.txt"},
				{"root/a/2.txt", "root/c/d/4.txt", "root/4.txt"},
			},
		},
		{
			"无重复内容",
			[]string{"root/a 1.txt(abcd)", "root/c 3.txt(efgh)"},
			[][]string{},
		},
		{
			"同一目录内两份文件内容相同",
			[]string{"root/a 1.txt(abcd) 2.txt(abcd)"},
			[][]string{{"root/a/1.txt", "root/a/2.txt"}},
		},
		{
			"三组各自重复",
			[]string{
				"root/a 1.txt(x) 2.txt(y)",
				"root/b 3.txt(x)",
				"root/b 4.txt(y) 5.txt(z) 6.txt(z)",
			},
			[][]string{
				{"root/a/1.txt", "root/b/3.txt"},
				{"root/a/2.txt", "root/b/4.txt"},
				{"root/b/5.txt", "root/b/6.txt"},
			},
		},
		{
			"根目录下的文件也参与分组",
			[]string{"root 1.txt(same)", "root/sub 2.txt(same)"},
			[][]string{{"root/1.txt", "root/sub/2.txt"}},
		},
	}

	for _, tc := range cases {
		got := findDuplicate(tc.paths)
		if !reflect.DeepEqual(normalize(got), normalize(tc.want)) {
			t.Errorf("%s:\n  want %v\n  got  %v", tc.name, normalize(tc.want), normalize(got))
		} else {
			t.Logf("%s: PASS", tc.name)
		}
	}
}

// normalize 把分组整理成顺序无关的规范形式:组内文件名排序,组间按字典序排序。
func normalize(groups [][]string) [][]string {
	out := make([][]string, 0, len(groups))
	for _, g := range groups {
		cp := append([]string(nil), g...)
		sort.Strings(cp)
		out = append(out, cp)
	}
	sort.Slice(out, func(i, j int) bool {
		a, b := out[i], out[j]
		for k := 0; k < len(a) && k < len(b); k++ {
			if a[k] != b[k] {
				return a[k] < b[k]
			}
		}
		return len(a) < len(b)
	})
	return out
}
