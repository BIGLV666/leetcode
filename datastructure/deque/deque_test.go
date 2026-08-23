package deque

import (
	"reflect"
	"testing"
)

func TestDequeBasicOperations(t *testing.T) {
	var deque Deque[int]
	if !deque.Empty() || deque.Len() != 0 {
		t.Fatal("零值 Deque 应该为空")
	}
	if _, ok := deque.Front(); ok {
		t.Fatal("空队列 Front 应该失败")
	}
	if _, ok := deque.Back(); ok {
		t.Fatal("空队列 Back 应该失败")
	}
	if _, ok := deque.PopFront(); ok {
		t.Fatal("空队列 PopFront 应该失败")
	}
	if _, ok := deque.PopBack(); ok {
		t.Fatal("空队列 PopBack 应该失败")
	}

	deque.PushBack(2)
	deque.PushFront(1)
	deque.PushBack(3)
	if got, want := deque.Values(), []int{1, 2, 3}; !reflect.DeepEqual(got, want) {
		t.Fatalf("Values() = %v, want %v", got, want)
	}
	if got, ok := deque.Front(); !ok || got != 1 {
		t.Fatalf("Front() = (%d, %v), want (1, true)", got, ok)
	}
	if got, ok := deque.Back(); !ok || got != 3 {
		t.Fatalf("Back() = (%d, %v), want (3, true)", got, ok)
	}
	if got, ok := deque.PopFront(); !ok || got != 1 {
		t.Fatalf("PopFront() = (%d, %v), want (1, true)", got, ok)
	}
	if got, ok := deque.PopBack(); !ok || got != 3 {
		t.Fatalf("PopBack() = (%d, %v), want (3, true)", got, ok)
	}
	if got, ok := deque.PopFront(); !ok || got != 2 || !deque.Empty() {
		t.Fatal("最后一个元素删除错误")
	}
}

func TestDequeStringAndClear(t *testing.T) {
	deque := New[string]()
	deque.PushBack("中文")
	deque.PushFront("first")
	if got, want := deque.String(), "[first, 中文]"; got != want {
		t.Fatalf("String() = %q, want %q", got, want)
	}
	values := deque.Values()
	values[0] = "changed"
	if got, _ := deque.Front(); got != "first" {
		t.Fatal("Values() 应该返回快照")
	}
	deque.Clear()
	if !deque.Empty() || deque.Len() != 0 {
		t.Fatal("Clear 后队列应该为空")
	}
	deque.PushBack("reuse")
	if got, _ := deque.Front(); got != "reuse" {
		t.Fatal("Clear 后重新使用失败")
	}
}

func TestDequeExtremeValuesAndAlternatingEnds(t *testing.T) {
	deque := New[int]()
	values := []int{0, -1, 1, -1 << 62, 1<<62 - 1}
	for i, value := range values {
		if i%2 == 0 {
			deque.PushFront(value)
		} else {
			deque.PushBack(value)
		}
	}

	want := []int{1<<62 - 1, 1, 0, -1, -1 << 62}
	if got := deque.Values(); !reflect.DeepEqual(got, want) {
		t.Fatalf("Values() = %v, want %v", got, want)
	}
	for i := 0; i < len(want); i++ {
		if i%2 == 0 {
			wantValue := want[i/2]
			if got, _ := deque.PopFront(); got != wantValue {
				t.Fatalf("PopFront() = %d, want %d", got, wantValue)
			}
		} else {
			wantValue := want[len(want)-1-i/2]
			if got, _ := deque.PopBack(); got != wantValue {
				t.Fatalf("PopBack() = %d, want %d", got, wantValue)
			}
		}
	}
}
