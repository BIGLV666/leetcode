package longestmonotonicsubarray

import (
	"math/rand"
	"testing"
)

func brute(nums []int) int {
	n := len(nums)
	best := 0
	for i := 0; i < n; i++ {
		j := i
		for j+1 < n && nums[j] < nums[j+1] {
			j++
		}
		if j-i+1 > best {
			best = j - i + 1
		}
		j = i
		for j+1 < n && nums[j] > nums[j+1] {
			j++
		}
		if j-i+1 > best {
			best = j - i + 1
		}
	}
	return best
}

// ref 是 O(n) 的一次扫描参考实现,与题解的分组循环写法不同。
func ref(nums []int) int {
	if len(nums) == 0 {
		return 0
	}
	best, inc, dec := 1, 1, 1
	for i := 1; i < len(nums); i++ {
		switch {
		case nums[i] > nums[i-1]:
			inc++
			dec = 1
		case nums[i] < nums[i-1]:
			dec++
			inc = 1
		default:
			inc, dec = 1, 1
		}
		if inc > best {
			best = inc
		}
		if dec > best {
			best = dec
		}
	}
	return best
}

func TestOfficial(t *testing.T) {
	cases := []struct {
		nums []int
		want int
	}{
		{[]int{1, 4, 3, 3, 2}, 2},
		{[]int{3, 3, 3, 3}, 1},
		{[]int{3, 2, 1}, 3},
		{[]int{1}, 1},
		{[]int{1, 2, 3, 4}, 4},
		{[]int{9, 8, 7, 6}, 4},
	}
	for _, c := range cases {
		if got := longestMonotonicSubarray(c.nums); got != c.want {
			t.Fatalf("nums=%v want=%d got=%d", c.nums, c.want, got)
		}
	}
}

func TestRandomCrossCheck(t *testing.T) {
	rnd := rand.New(rand.NewSource(3105))
	for round := 0; round < 5000; round++ {
		n := 1 + rnd.Intn(200)
		nums := make([]int, n)
		for i := range nums {
			nums[i] = rnd.Intn(10)
		}
		want := brute(nums)
		if got := longestMonotonicSubarray(nums); got != want {
			t.Fatalf("nums=%v want=%d got=%d", nums, want, got)
		}
	}
}

func TestLarge(t *testing.T) {
	const n = 100000
	rnd := rand.New(rand.NewSource(3105))

	// 全相等
	equal := make([]int, n)
	for i := range equal {
		equal[i] = 7
	}
	// 严格递增
	inc := make([]int, n)
	for i := range inc {
		inc[i] = i
	}
	// 严格递减
	dec := make([]int, n)
	for i := range dec {
		dec[i] = n - i
	}
	// 随机
	rndArr := make([]int, n)
	for i := range rndArr {
		rndArr[i] = rnd.Intn(1000)
	}
	// 构造递增/递减交替段
	saw := make([]int, n)
	for i := range saw {
		if (i/100)%2 == 0 {
			saw[i] = i % 100
		} else {
			saw[i] = 100 - i%100
		}
	}

	for name, nums := range map[string][]int{
		"equal": equal,
		"inc":   inc,
		"dec":   dec,
		"rand":  rndArr,
		"saw":   saw,
	} {
		want := ref(nums)
		if got := longestMonotonicSubarray(nums); got != want {
			t.Fatalf("%s: want=%d got=%d", name, want, got)
		}
	}
}

func BenchmarkLongestMonotonicSubarray(b *testing.B) {
	rnd := rand.New(rand.NewSource(3105))
	nums := make([]int, 100000)
	for i := range nums {
		nums[i] = rnd.Intn(1000)
	}
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		longestMonotonicSubarray(nums)
	}
}
