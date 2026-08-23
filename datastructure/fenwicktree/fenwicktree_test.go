package fenwicktree

import (
	"math/rand"
	"reflect"
	"strings"
	"testing"
)

func TestFenwickTreeBuildAndQuery(t *testing.T) {
	fenwick := Build([]int64{1, 2, 3, 4, 5})
	if fenwick.Len() != 5 {
		t.Fatalf("Len() = %d, want 5", fenwick.Len())
	}
	for end, want := range []int64{0, 1, 3, 6, 10, 15} {
		if got, ok := fenwick.PrefixSum(end); !ok || got != want {
			t.Fatalf("PrefixSum(%d) = (%d, %v), want (%d, true)", end, got, ok, want)
		}
	}
	if got, ok := fenwick.RangeSum(1, 4); !ok || got != 9 {
		t.Fatalf("RangeSum(1, 4) = (%d, %v), want (9, true)", got, ok)
	}
	if got, ok := fenwick.RangeSum(3, 3); !ok || got != 0 {
		t.Fatalf("空区间结果 = (%d, %v), want (0, true)", got, ok)
	}
}

func TestFenwickTreeUpdates(t *testing.T) {
	fenwick := Build([]int64{1, 2, 3})
	if !fenwick.Add(1, 5) || !fenwick.Set(2, -4) {
		t.Fatal("合法更新失败")
	}
	if got, want := fenwick.Values(), []int64{1, 7, -4}; !reflect.DeepEqual(got, want) {
		t.Fatalf("Values() = %v, want %v", got, want)
	}
	if got, _ := fenwick.RangeSum(0, 3); got != 4 {
		t.Fatalf("RangeSum(0, 3) = %d, want 4", got)
	}
	if value, ok := fenwick.At(1); !ok || value != 7 {
		t.Fatalf("At(1) = (%d, %v), want (7, true)", value, ok)
	}
}

func TestFenwickTreeBoundaries(t *testing.T) {
	var zero FenwickTree
	if zero.Len() != 0 || zero.String() != "[]" {
		t.Fatal("零值树状数组状态错误")
	}
	if sum, ok := zero.PrefixSum(0); !ok || sum != 0 {
		t.Fatal("零值 PrefixSum(0) 应该成功")
	}
	if sum, ok := zero.RangeSum(0, 0); !ok || sum != 0 {
		t.Fatal("零值空区间应该成功")
	}
	if zero.Add(0, 1) || zero.Set(0, 1) {
		t.Fatal("空树状数组不应该允许更新")
	}
	if _, ok := zero.At(0); ok {
		t.Fatal("空树状数组 At(0) 应该失败")
	}

	if fenwick, ok := New(-1); ok || fenwick != nil {
		t.Fatal("New(-1) 应该失败")
	}
	fenwick, ok := New(3)
	if !ok {
		t.Fatal("New(3) 应该成功")
	}
	for _, index := range []int{-1, 3} {
		if fenwick.Add(index, 1) || fenwick.Set(index, 1) {
			t.Fatalf("index=%d 的更新应该失败", index)
		}
		if _, ok := fenwick.At(index); ok {
			t.Fatalf("At(%d) 应该失败", index)
		}
	}
	for _, end := range []int{-1, 4} {
		if _, ok := fenwick.PrefixSum(end); ok {
			t.Fatalf("PrefixSum(%d) 应该失败", end)
		}
	}
	for _, interval := range [][2]int{{-1, 1}, {2, 1}, {0, 4}} {
		if _, ok := fenwick.RangeSum(interval[0], interval[1]); ok {
			t.Fatalf("RangeSum(%d, %d) 应该失败", interval[0], interval[1])
		}
	}
}

func TestFenwickTreeInputAndOutputSnapshots(t *testing.T) {
	input := []int64{2, 4, 6}
	fenwick := Build(input)
	input[0] = 100
	values := fenwick.Values()
	values[1] = 100
	if got, want := fenwick.Values(), []int64{2, 4, 6}; !reflect.DeepEqual(got, want) {
		t.Fatalf("快照隔离失败：Values() = %v, want %v", got, want)
	}
	if got, want := fenwick.String(), "[2, 4, 6]"; got != want {
		t.Fatalf("String() = %q, want %q", got, want)
	}
	debug := fenwick.DebugString()
	for _, fragment := range []string{
		"FenwickTree{length: 3}",
		"values: [2, 4, 6]",
		"tree(一基): [0, 2, 6, 6]",
		"tree[2] = 6，负责 values[0:2)",
	} {
		if !strings.Contains(debug, fragment) {
			t.Fatalf("DebugString() = %q, missing %q", debug, fragment)
		}
	}
}

func TestFenwickTreeExtremeValues(t *testing.T) {
	const large int64 = 1 << 60
	fenwick := Build([]int64{large, -large, large, -large})
	if got, _ := fenwick.PrefixSum(4); got != 0 {
		t.Fatalf("PrefixSum(4) = %d, want 0", got)
	}
	if !fenwick.Add(1, large) || !fenwick.Set(3, large) {
		t.Fatal("大整数更新失败")
	}
	if got, _ := fenwick.RangeSum(0, 4); got != 3*large {
		t.Fatalf("RangeSum(0, 4) = %d, want %d", got, 3*large)
	}
}

func TestFenwickTreeRandomAgainstSlice(t *testing.T) {
	const size = 128
	fenwick, _ := New(size)
	want := make([]int64, size)
	rng := rand.New(rand.NewSource(42))
	for step := 0; step < 5000; step++ {
		index := rng.Intn(size)
		if rng.Intn(2) == 0 {
			delta := int64(rng.Intn(201) - 100)
			fenwick.Add(index, delta)
			want[index] += delta
		} else {
			value := int64(rng.Intn(2001) - 1000)
			fenwick.Set(index, value)
			want[index] = value
		}

		left := rng.Intn(size + 1)
		right := left + rng.Intn(size-left+1)
		var wantSum int64
		for _, value := range want[left:right] {
			wantSum += value
		}
		if got, ok := fenwick.RangeSum(left, right); !ok || got != wantSum {
			t.Fatalf("step %d: RangeSum(%d, %d) = (%d, %v), want (%d, true)",
				step, left, right, got, ok, wantSum)
		}
		if got := fenwick.Values(); !reflect.DeepEqual(got, want) {
			t.Fatalf("step %d: Values() 与参考切片不一致", step)
		}
	}
}
