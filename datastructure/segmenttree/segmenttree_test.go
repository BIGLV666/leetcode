package segmenttree

import (
	"math/rand"
	"reflect"
	"strings"
	"testing"
)

func TestSegmentTreeBuildAndQuery(t *testing.T) {
	seg := Build([]int64{1, 2, 3, 4, 5})
	if seg.Len() != 5 {
		t.Fatalf("Len() = %d, want 5", seg.Len())
	}
	if got, ok := seg.RangeSum(0, 5); !ok || got != 15 {
		t.Fatalf("RangeSum(0, 5) = (%d, %v), want (15, true)", got, ok)
	}
	if got, ok := seg.RangeSum(1, 4); !ok || got != 9 {
		t.Fatalf("RangeSum(1, 4) = (%d, %v), want (9, true)", got, ok)
	}
	if got, ok := seg.RangeSum(3, 3); !ok || got != 0 {
		t.Fatalf("空区间结果 = (%d, %v), want (0, true)", got, ok)
	}
}

func TestSegmentTreeSinglePointUpdate(t *testing.T) {
	seg := Build([]int64{1, 2, 3})
	if !seg.Add(1, 5) {
		t.Fatal("Add 失败")
	}
	if got, want := seg.Values(), []int64{1, 7, 3}; !reflect.DeepEqual(got, want) {
		t.Fatalf("Values() = %v, want %v", got, want)
	}
	if got, _ := seg.RangeSum(0, 3); got != 11 {
		t.Fatalf("RangeSum(0, 3) = %d, want 11", got)
	}
	if !seg.Set(2, -4) {
		t.Fatal("Set 失败")
	}
	if got, want := seg.Values(), []int64{1, 7, -4}; !reflect.DeepEqual(got, want) {
		t.Fatalf("Values() = %v, want %v", got, want)
	}
	if got, _ := seg.RangeSum(0, 3); got != 4 {
		t.Fatalf("RangeSum(0, 3) = %d, want 4", got)
	}
}

func TestSegmentTreeRangeAdd(t *testing.T) {
	seg := Build([]int64{1, 2, 3, 4, 5})
	if !seg.RangeAdd(1, 4, 10) {
		t.Fatal("RangeAdd 失败")
	}
	if got, want := seg.Values(), []int64{1, 12, 13, 14, 5}; !reflect.DeepEqual(got, want) {
		t.Fatalf("Values() = %v, want %v", got, want)
	}
	if got, _ := seg.RangeSum(0, 5); got != 45 {
		t.Fatalf("RangeSum(0, 5) = %d, want 45", got)
	}
	if got, _ := seg.RangeSum(1, 4); got != 39 {
		t.Fatalf("RangeSum(1, 4) = %d, want 39", got)
	}
}

func TestSegmentTreeRangeMinMax(t *testing.T) {
	seg := Build([]int64{5, 2, 8, 1, 9})
	if got, ok := seg.RangeMin(0, 5); !ok || got != 1 {
		t.Fatalf("RangeMin(0, 5) = (%d, %v), want (1, true)", got, ok)
	}
	if got, ok := seg.RangeMax(0, 5); !ok || got != 9 {
		t.Fatalf("RangeMax(0, 5) = (%d, %v), want (9, true)", got, ok)
	}
	if got, ok := seg.RangeMin(1, 4); !ok || got != 1 {
		t.Fatalf("RangeMin(1, 4) = (%d, %v), want (1, true)", got, ok)
	}
	if got, ok := seg.RangeMax(1, 4); !ok || got != 8 {
		t.Fatalf("RangeMax(1, 4) = (%d, %v), want (8, true)", got, ok)
	}
	seg.RangeAdd(1, 4, -10)
	if got, ok := seg.RangeMin(0, 5); !ok || got != -9 {
		t.Fatalf("RangeMin(0, 5) = (%d, %v), want (-9, true)", got, ok)
	}
}

func TestSegmentTreeBoundaries(t *testing.T) {
	var zero SegmentTree
	if zero.Len() != 0 || zero.String() != "[]" {
		t.Fatal("零值线段树状态错误")
	}
	if sum, ok := zero.RangeSum(0, 0); !ok || sum != 0 {
		t.Fatal("零值 RangeSum(0, 0) 应该成功")
	}
	if zero.Add(0, 1) || zero.Set(0, 1) || zero.RangeAdd(0, 0, 1) {
		t.Fatal("空线段树不应该允许更新")
	}
	if _, ok := zero.At(0); ok {
		t.Fatal("空线段树 At(0) 应该失败")
	}
	if _, ok := zero.RangeMin(0, 0); ok {
		t.Fatal("空区间 RangeMin 应该失败")
	}

	if seg, ok := New(-1); ok || seg != nil {
		t.Fatal("New(-1) 应该失败")
	}
	seg, ok := New(3)
	if !ok {
		t.Fatal("New(3) 应该成功")
	}
	for _, index := range []int{-1, 3} {
		if seg.Add(index, 1) || seg.Set(index, 1) {
			t.Fatalf("index=%d 的更新应该失败", index)
		}
		if _, ok := seg.At(index); ok {
			t.Fatalf("At(%d) 应该失败", index)
		}
	}
	for _, interval := range [][2]int{{-1, 1}, {2, 1}, {0, 4}} {
		if _, ok := seg.RangeSum(interval[0], interval[1]); ok {
			t.Fatalf("RangeSum(%d, %d) 应该失败", interval[0], interval[1])
		}
		if seg.RangeAdd(interval[0], interval[1], 1) {
			t.Fatalf("RangeAdd(%d, %d) 应该失败", interval[0], interval[1])
		}
	}
}

func TestSegmentTreeInputAndOutputSnapshots(t *testing.T) {
	input := []int64{2, 4, 6}
	seg := Build(input)
	input[0] = 100
	values := seg.Values()
	values[1] = 100
	if got, want := seg.Values(), []int64{2, 4, 6}; !reflect.DeepEqual(got, want) {
		t.Fatalf("快照隔离失败：Values() = %v, want %v", got, want)
	}
	if got, want := seg.String(), "[2, 4, 6]"; got != want {
		t.Fatalf("String() = %q, want %q", got, want)
	}
	debug := seg.DebugString()
	for _, fragment := range []string{
		"SegmentTree{length: 3}",
		"values: [2, 4, 6]",
		"tree:",
		"lazy:",
	} {
		if !strings.Contains(debug, fragment) {
			t.Fatalf("DebugString() = %q, missing %q", debug, fragment)
		}
	}
}

func TestSegmentTreeExtremeValues(t *testing.T) {
	const large int64 = 1 << 60
	seg := Build([]int64{large, -large, large, -large})
	if got, _ := seg.RangeSum(0, 4); got != 0 {
		t.Fatalf("RangeSum(0, 4) = %d, want 0", got)
	}
	if !seg.RangeAdd(0, 4, large) {
		t.Fatal("大整数区间更新失败")
	}
	if got, _ := seg.RangeSum(0, 4); got != 4*large {
		t.Fatalf("RangeSum(0, 4) = %d, want %d", got, 4*large)
	}
}

func TestSegmentTreeRandomAgainstSlice(t *testing.T) {
	const size = 128
	seg, _ := New(size)
	want := make([]int64, size)
	rng := rand.New(rand.NewSource(42))
	for step := 0; step < 3000; step++ {
		op := rng.Intn(3)
		if op == 0 {
			// 单点更新
			index := rng.Intn(size)
			delta := int64(rng.Intn(201) - 100)
			seg.Add(index, delta)
			want[index] += delta
		} else if op == 1 {
			// 区间更新
			left := rng.Intn(size)
			right := left + rng.Intn(size-left+1)
			delta := int64(rng.Intn(21) - 10)
			seg.RangeAdd(left, right, delta)
			for i := left; i < right; i++ {
				want[i] += delta
			}
		} else {
			// 区间查询
			left := rng.Intn(size)
			right := left + rng.Intn(size-left+1)
			var wantSum int64
			for i := left; i < right; i++ {
				wantSum += want[i]
			}
			if got, ok := seg.RangeSum(left, right); !ok || got != wantSum {
				t.Fatalf("step %d: RangeSum(%d, %d) = (%d, %v), want (%d, true)",
					step, left, right, got, ok, wantSum)
			}
		}

		if got := seg.Values(); !reflect.DeepEqual(got, want) {
			t.Fatalf("step %d: Values() 与参考切片不一致", step)
		}
	}
}

func TestSegmentTreeLazyPropagation(t *testing.T) {
	seg := Build([]int64{1, 2, 3, 4, 5, 6, 7, 8})
	seg.RangeAdd(2, 6, 10)
	if got, want := seg.Values(), []int64{1, 2, 13, 14, 15, 16, 7, 8}; !reflect.DeepEqual(got, want) {
		t.Fatalf("RangeAdd 后 Values() = %v, want %v", got, want)
	}
	seg.RangeAdd(0, 4, 5)
	if got, want := seg.Values(), []int64{6, 7, 18, 19, 15, 16, 7, 8}; !reflect.DeepEqual(got, want) {
		t.Fatalf("第二次 RangeAdd 后 Values() = %v, want %v", got, want)
	}
	if got, _ := seg.RangeSum(0, 8); got != 96 {
		t.Fatalf("RangeSum(0, 8) = %d, want 96", got)
	}
	seg.Add(4, 100)
	if got, _ := seg.RangeSum(3, 6); got != 150 {
		t.Fatalf("单点更新后 RangeSum(3, 6) = %d, want 150", got)
	}
}
