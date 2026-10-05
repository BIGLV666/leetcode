package findString

import (
	"math/rand"
	"sort"
	"testing"

	"leetcode/common"
)

func TestFindString(t *testing.T) {
	common.RunTests(
		t,
		findString,
		[]common.TestCase{
			{
				// 官方示例 1:目标不在数组中
				Args:     []any{[]string{"at", "", "", "", "ball", "", "", "car", "", "", "dad", "", ""}, "ta"},
				Expected: -1,
			},
			{
				// 官方示例 2:目标在空串右侧
				Args:     []any{[]string{"at", "", "", "", "ball", "", "", "car", "", "", "dad", "", ""}, "ball"},
				Expected: 4,
			},
			{
				// 全部为空串
				Args:     []any{[]string{"", "", ""}, "a"},
				Expected: -1,
			},
			{
				// 单个空串
				Args:     []any{[]string{""}, "a"},
				Expected: -1,
			},
			{
				// 空数组
				Args:     []any{[]string{}, "a"},
				Expected: -1,
			},
			{
				// 无空串的普通二分
				Args:     []any{[]string{"a", "b", "c"}, "b"},
				Expected: 1,
			},
			{
				// 目标在空串之前,验证左半区不被误伤
				Args:     []any{[]string{"a", "", "c"}, "a"},
				Expected: 0,
			},
			{
				// 目标在空串之后
				Args:     []any{[]string{"a", "", "c"}, "c"},
				Expected: 2,
			},
			{
				// 空串包夹唯一目标
				Args:     []any{[]string{"", "a", ""}, "a"},
				Expected: 1,
			},
			{
				// 连续空串横跨中点
				Args:     []any{[]string{"a", "", "", "b"}, "b"},
				Expected: 3,
			},
			{
				// 目标大于全部元素
				Args:     []any{[]string{"a", "b", "", "", "c"}, "d"},
				Expected: -1,
			},
			{
				// 目标落在两非空串之间
				Args:     []any{[]string{"a", "", "b"}, "aa"},
				Expected: -1,
			},
			{
				// 空串打头且目标在首位
				Args:     []any{[]string{"", "", "c", "d"}, "c"},
				Expected: 2,
			},
		},
	)
}

// TestSparseArrayStress 压测:随机生成「非空串全局严格升序 + 空串穿插」的数组,
// 与线性扫描参考实现对拍 2000 轮,覆盖空串打头/收尾/连续空串等形态。
func TestSparseArrayStress(t *testing.T) {
	random := rand.New(rand.NewSource(20261005))
	for round := 0; round < 2000; round++ {
		n := 1 + random.Intn(30)
		// 收集 n 个互不相同的随机词后整体排序,保证非空串全局严格升序(目标下标唯一);
		// 不能用「逐个生成更大的词」的爬升法——随机到 "zzz" 这类上界词后会永远找不到更大的词
		seen := make(map[string]bool, n)
		words := make([]string, 0, n)
		for len(words) < n {
			cand := randWord(random)
			if !seen[cand] {
				seen[cand] = true
				words = append(words, cand)
			}
		}
		sort.Strings(words)
		for i := range words { // 按概率把约 1/3 的位置挖成空串
			if random.Intn(3) == 0 {
				words[i] = ""
			}
		}

		// 目标:一半轮次取数组中真实存在的非空串,一半取可能不存在的串
		nonEmpty := make([]int, 0, len(words))
		for i, w := range words {
			if w != "" {
				nonEmpty = append(nonEmpty, i)
			}
		}
		var s string
		if len(nonEmpty) > 0 && random.Intn(2) == 0 {
			s = words[nonEmpty[random.Intn(len(nonEmpty))]]
		} else {
			s = randWord(random)
		}

		want := -1 // 线性扫描参考实现:直接逐个比对
		for i, w := range words {
			if w == s {
				want = i
				break
			}
		}
		if got := findString(words, s); got != want {
			t.Fatalf("round %d words=%q s=%q: 线性扫描 %d, 题解 %d", round, words, s, want, got)
		}
	}
}

// randWord 生成随机小写单词(长度 1~3),保证非空,使空串永远不会与目标相等。
func randWord(random *rand.Rand) string {
	length := 1 + random.Intn(3)
	b := make([]byte, length)
	for i := range b {
		b[i] = byte('a' + random.Intn(26))
	}
	return string(b)
}
