package sortedlist

import (
	"math/rand"
	"reflect"
	"sort"
	"strings"
	"testing"
)

// zeroSource 总是生成 0，使 randomLevel 每次都晋升到 maxLevel。
// 它用于稳定覆盖平时很难随机出现的最高层边界。
type zeroSource struct{}

func (zeroSource) Int63() int64 { return 0 }
func (zeroSource) Seed(int64)   {}

// assertInvariants 验证跳表的内部不变量，而不只是第 0 层的最终结果。
func assertInvariants(t *testing.T, list *SortedList) {
	t.Helper()

	if list.header == nil {
		if list.length != 0 || list.level != 0 {
			t.Fatalf("未初始化列表的 length=%d, level=%d", list.length, list.level)
		}
		return
	}
	if list.level < 1 || list.level > maxLevel {
		t.Fatalf("level=%d，不在 [1, %d] 范围内", list.level, maxLevel)
	}
	if len(list.header.next) != maxLevel || len(list.header.span) != maxLevel {
		t.Fatalf("头节点必须拥有 %d 层", maxLevel)
	}

	// position 记录每个节点在第 0 层的位置；头节点位置为 0，
	// 第一个数据节点位置为 1。因此两节点位置之差就是指针应有的 span。
	position := map[*node]int{list.header: 0}
	count := 0
	var previous *node
	for current := list.header.next[0]; current != nil; current = current.next[0] {
		count++
		position[current] = count
		if len(current.next) == 0 || len(current.next) != len(current.span) {
			t.Fatalf("值为 %d 的节点层高非法", current.value)
		}
		if previous != nil && previous.value > current.value {
			t.Fatalf("第 0 层无序：%d > %d", previous.value, current.value)
		}
		previous = current
	}
	if count != list.length {
		t.Fatalf("第 0 层有 %d 个节点，length=%d", count, list.length)
	}

	for level := 0; level < list.level; level++ {
		current := list.header
		for {
			next := current.next[level]
			expectedSpan := list.length - position[current]
			if next != nil {
				nextPosition, ok := position[next]
				if !ok {
					t.Fatalf("第 %d 层指向不在第 0 层中的节点", level)
				}
				expectedSpan = nextPosition - position[current]
				if expectedSpan <= 0 {
					t.Fatalf("第 %d 层指针没有向前移动", level)
				}
			}
			if current.span[level] != expectedSpan {
				t.Fatalf("第 %d 层值为 %d 的节点 span=%d, want %d",
					level, current.value, current.span[level], expectedSpan)
			}
			if next == nil {
				break
			}
			if len(next.next) <= level || len(next.span) <= level {
				t.Fatalf("第 %d 层包含高度不足的节点 %d", level, next.value)
			}
			current = next
		}
	}
	if list.level > 1 && list.header.next[list.level-1] == nil {
		t.Fatalf("最高的第 %d 层为空", list.level-1)
	}
}

func TestSortedListBasicOperations(t *testing.T) {
	var list SortedList // 验证零值也可以直接使用。
	for _, value := range []int{5, 1, 3, 3, -2, 8} {
		list.Add(value)
	}

	want := []int{-2, 1, 3, 3, 5, 8}
	if got := list.Values(); !reflect.DeepEqual(got, want) {
		t.Fatalf("Values() = %v, want %v", got, want)
	}
	if list.Len() != len(want) || !list.Contains(3) || list.Contains(4) {
		t.Fatalf("unexpected length or Contains result")
	}
	for i, value := range want {
		if got, ok := list.Get(i); !ok || got != value {
			t.Fatalf("Get(%d) = (%d, %v), want (%d, true)", i, got, ok, value)
		}
	}
}

func TestSortedListString(t *testing.T) {
	var empty SortedList
	if got, want := empty.String(), "[]"; got != want {
		t.Fatalf("empty String() = %q, want %q", got, want)
	}
	if got, want := empty.DebugString(), "SortedList{length: 0, level: 0}\n<未初始化>"; got != want {
		t.Fatalf("empty DebugString() = %q, want %q", got, want)
	}

	list := New()
	for _, value := range []int{3, 1, 3, -2} {
		list.Add(value)
	}
	if got, want := list.String(), "[-2, 1, 3, 3]"; got != want {
		t.Fatalf("String() = %q, want %q", got, want)
	}
	debug := list.DebugString()
	t.Log(debug)
	for _, fragment := range []string{"SortedList{length: 4, level:", "第 0 层: header", " -[", "-> -2", "-> 3"} {
		if !strings.Contains(debug, fragment) {
			t.Fatalf("DebugString() = %q, missing %q", debug, fragment)
		}
	}
}

func TestSortedListBoundaries(t *testing.T) {
	list := New()
	if _, ok := list.Get(0); ok {
		t.Fatal("Get on an empty list should fail")
	}
	if _, ok := list.Get(-1); ok {
		t.Fatal("negative Get index should fail")
	}
	if list.Remove(10) {
		t.Fatal("Remove of a missing value should fail")
	}

	list.Add(7)
	if _, ok := list.Get(1); ok {
		t.Fatal("Get at Len() should fail")
	}
	if !list.Remove(7) || list.Remove(7) || list.Len() != 0 {
		t.Fatal("single-element removal is incorrect")
	}
	assertInvariants(t, list)
}

func TestSortedListIntegerLimits(t *testing.T) {
	maxInt := int(^uint(0) >> 1)
	minInt := -maxInt - 1
	list := New()
	for _, value := range []int{maxInt, 0, minInt, maxInt, minInt} {
		list.Add(value)
	}

	want := []int{minInt, minInt, 0, maxInt, maxInt}
	if got := list.Values(); !reflect.DeepEqual(got, want) {
		t.Fatalf("Values() = %v, want %v", got, want)
	}
	if !list.Contains(minInt) || !list.Contains(maxInt) {
		t.Fatal("没有找到 int 边界值")
	}
	if !list.Remove(minInt) || !list.Remove(maxInt) {
		t.Fatal("删除 int 边界值失败")
	}
	assertInvariants(t, list)
}

func TestSortedListManyDuplicates(t *testing.T) {
	list := New()
	const count = 1000
	for i := 0; i < count; i++ {
		list.Add(7)
	}
	assertInvariants(t, list)

	for remaining := count - 1; remaining >= 0; remaining-- {
		if !list.Remove(7) {
			t.Fatalf("还剩 %d 个元素时删除失败", remaining+1)
		}
		if list.Len() != remaining {
			t.Fatalf("Len() = %d, want %d", list.Len(), remaining)
		}
	}
	if list.Remove(7) || list.Contains(7) {
		t.Fatal("重复值全部删除后仍能找到或删除该值")
	}
	assertInvariants(t, list)
}

func TestSortedListOrderedInputsAndReuse(t *testing.T) {
	for _, name := range []string{"ascending", "descending"} {
		t.Run(name, func(t *testing.T) {
			list := New()
			for cycle := 0; cycle < 5; cycle++ {
				for i := 0; i < 500; i++ {
					value := i
					if name == "descending" {
						value = 499 - i
					}
					list.Add(value)
				}
				assertInvariants(t, list)
				for i := 0; i < 500; i++ {
					if !list.Remove(i) {
						t.Fatalf("cycle %d: Remove(%d) 失败", cycle, i)
					}
				}
				if list.Len() != 0 || list.level != 1 {
					t.Fatalf("cycle %d: 清空后 length=%d, level=%d", cycle, list.Len(), list.level)
				}
			}
			assertInvariants(t, list)
		})
	}
}

func TestSortedListMaximumLevel(t *testing.T) {
	list := New()
	list.rng = rand.New(zeroSource{})
	list.Add(10)

	if list.level != maxLevel || len(list.header.next) != maxLevel {
		t.Fatalf("level=%d, want %d", list.level, maxLevel)
	}
	assertInvariants(t, list)

	if !list.Remove(10) {
		t.Fatal("删除最高层节点失败")
	}
	if list.level != 1 || list.Len() != 0 {
		t.Fatalf("清空后 level=%d, length=%d", list.level, list.Len())
	}
	assertInvariants(t, list)
}

func TestSortedListRandomAgainstSortedSlice(t *testing.T) {
	list := New()
	want := make([]int, 0)
	rng := rand.New(rand.NewSource(42))
	for step := 0; step < 2000; step++ {
		value := rng.Intn(41) - 20
		if rng.Intn(3) != 0 {
			list.Add(value)
			want = append(want, value)
			sort.Ints(want)
		} else {
			removed := list.Remove(value)
			position := sort.SearchInts(want, value)
			inWant := position < len(want) && want[position] == value
			if removed != inWant {
				t.Fatalf("step %d: Remove(%d) = %v, want %v", step, value, removed, inWant)
			}
			if inWant {
				want = append(want[:position], want[position+1:]...)
			}
		}
		if got := list.Values(); !reflect.DeepEqual(got, want) {
			t.Fatalf("step %d: Values() = %v, want %v", step, got, want)
		}
		if list.Len() != len(want) {
			t.Fatalf("step %d: Len() = %d, want %d", step, list.Len(), len(want))
		}
		// Values 只沿最底层链表遍历；逐个 Get 才能验证各层 span 是否正确。
		for i, value := range want {
			if got, ok := list.Get(i); !ok || got != value {
				t.Fatalf("step %d: Get(%d) = (%d, %v), want (%d, true)", step, i, got, ok, value)
			}
		}
		for candidate := -21; candidate <= 21; candidate++ {
			position := sort.SearchInts(want, candidate)
			exists := position < len(want) && want[position] == candidate
			if got := list.Contains(candidate); got != exists {
				t.Fatalf("step %d: Contains(%d) = %v, want %v", step, candidate, got, exists)
			}
		}
		assertInvariants(t, list)
	}
}
