package nextGreaterElement

import (
	"math/rand"
	"sort"
	"testing"
)

// bruteNext 暴力参考实现:枚举各位全排列,取大于 n 的最小值;不存在或溢出 32 位返回 -1。
func bruteNext(n int) int {
	if n <= 0 {
		return -1
	}
	digits := []int{}
	for m := n; m > 0; m /= 10 {
		digits = append(digits, m%10)
	}
	sort.Ints(digits)
	best := -1
	var perm func([]int, int)
	perm = func(a []int, k int) {
		if k == len(a) {
			v := 0
			for _, d := range a {
				v = v*10 + d
			}
			if v > n && v <= 1<<31-1 && (best == -1 || v < best) {
				best = v
			}
			return
		}
		seen := map[int]bool{}
		for i := k; i < len(a); i++ {
			if seen[a[i]] {
				continue
			}
			seen[a[i]] = true
			a[k], a[i] = a[i], a[k]
			perm(a, k+1)
			a[k], a[i] = a[i], a[k]
		}
	}
	perm(digits, 0)
	return best
}

func TestNextGreaterElement(t *testing.T) {
	cases := []struct {
		n   int
		want int
	}{
		{12, 21},           // 官方示例 1:交换后即为更大
		{21, -1},           // 官方示例 2:已是最大排列
		{230241, 230412},   // 官方示例:尾部旋转
		{12443322, 13222344}, // 官方示例 3:含重复数字
		{5, -1},            // 单个数字
		{0, -1},            // 0 的各位无法重排
		{1234, 1243},       // 简单升序
		{1999999999, -1},   // 下一个排列超过 32 位上界
		{2147483647, -1},   // int32 最大值:2147483674 溢出
		{111, -1},          // 全部相同
	}
	for _, c := range cases {
		if got := nextGreaterElement(c.n); got != c.want {
			t.Fatalf("nextGreaterElement(%d) = %d, want %d", c.n, got, c.want)
		}
	}
}

func TestNextGreaterElementRandom(t *testing.T) {
	rng := rand.New(rand.NewSource(42))
	for i := 0; i < 500; i++ {
		// 生成 1..8 位随机数,与暴力枚举排列对拍
		n := rng.Intn(99999999) + 1
		want := bruteNext(n)
		if got := nextGreaterElement(n); got != want {
			t.Fatalf("nextGreaterElement(%d) = %d, want %d", n, got, want)
		}
	}
}