package rotate

import (
	"math/rand"
	"reflect"
	"testing"
)

// refRotate 独立参考实现:直接按「后 k 个 + 前 n-k 个」切片拼出新数组。
func refRotate(nums []int, k int) []int {
	n := len(nums)
	if n == 0 {
		return []int{}
	}
	k %= n
	out := make([]int, 0, n)
	out = append(out, nums[n-k:]...)
	out = append(out, nums[:n-k]...)
	return out
}

// TestRotate 同时校验两个实现:rotate(切片拼接)与 rotateII(三次翻转)。
// 两者都是原地修改、没有返回值,所以不能直接用 common.RunTests,这里手动拷贝入参再比对。
func TestRotate(t *testing.T) {
	impls := []struct {
		name string
		fn   func([]int, int)
	}{
		{"rotate", rotate},
		{"rotateII", rotateII},
	}

	cases := []struct {
		name string
		nums []int
		k    int
		want []int
	}{
		{"官方示例1", []int{1, 2, 3, 4, 5, 6, 7}, 3, []int{5, 6, 7, 1, 2, 3, 4}},
		{"官方示例2", []int{-1, -100, 3, 99}, 2, []int{3, 99, -1, -100}},
		// 小 k + 长数组:最容易暴露「拼接长度算错」的情形
		{"k=1 长数组", []int{1, 2, 3, 4, 5, 6, 7}, 1, []int{7, 1, 2, 3, 4, 5, 6}},
		{"k=2 长数组", []int{1, 2, 3, 4, 5, 6, 7}, 2, []int{6, 7, 1, 2, 3, 4, 5}},
		{"k=1 n=6", []int{1, 2, 3, 4, 5, 6}, 1, []int{6, 1, 2, 3, 4, 5}},
		// k 大于长度:应等价于 k % n
		{"k 超过长度", []int{1, 2, 3}, 5, []int{2, 3, 1}},
		{"k 是长度整数倍", []int{1, 2, 3}, 6, []int{1, 2, 3}},
		// 边界
		{"单元素", []int{1}, 0, []int{1}},
		{"单元素 k=5", []int{1}, 5, []int{1}},
		{"两元素", []int{1, 2}, 1, []int{2, 1}},
		{"k=0", []int{1, 2, 3, 4}, 0, []int{1, 2, 3, 4}},
		{"k=n-1", []int{1, 2, 3, 4}, 3, []int{2, 3, 4, 1}},
		{"含负数", []int{-5, -4, -3, -2, -1}, 2, []int{-2, -1, -5, -4, -3}},
	}

	for _, tc := range cases {
		for _, impl := range impls {
			got := append([]int(nil), tc.nums...)
			impl.fn(got, tc.k)
			if !reflect.DeepEqual(got, tc.want) {
				t.Errorf("%s / %s: nums=%v k=%d, want %v, got %v",
					impl.name, tc.name, tc.nums, tc.k, tc.want, got)
			}
		}
	}

	// 随机对拍:长度与 k 都随机,与切片拼接参考实现互验
	rnd := rand.New(rand.NewSource(42))
	for i := 0; i < 5000; i++ {
		n := 1 + rnd.Intn(15)
		nums := make([]int, n)
		for j := range nums {
			nums[j] = rnd.Intn(41) - 20
		}
		k := rnd.Intn(3 * n)
		want := refRotate(nums, k)

		for _, impl := range impls {
			got := append([]int(nil), nums...)
			impl.fn(got, k)
			if !reflect.DeepEqual(got, want) {
				t.Fatalf("round %d %s: nums=%v k=%d, want %v, got %v",
					i, impl.name, nums, k, want, got)
			}
		}
	}
}
