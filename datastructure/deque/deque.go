// Package deque 提供一个基于 container/list 的双端队列。
package deque

import (
	"container/list"
	"fmt"
	"strings"
)

// Deque 是一个双端队列。
//
// 队列两端都支持插入、删除和查看。Deque 的零值可以直接使用，
// 因此既可以通过 New 创建，也可以直接声明 var queue Deque[int]。
// Deque 不是并发安全的，多 goroutine 使用时需要在外部加锁。
type Deque[T any] struct {
	items *list.List
}

// New 创建一个空的双端队列。
func New[T any]() *Deque[T] {
	return &Deque[T]{items: list.New()}
}

// init 延迟初始化零值 Deque。
func (deque *Deque[T]) init() {
	if deque.items == nil {
		deque.items = list.New()
	}
}

// Len 返回队列中的元素数量。
func (deque *Deque[T]) Len() int {
	if deque.items == nil {
		return 0
	}
	return deque.items.Len()
}

// Empty 判断队列是否为空。
func (deque *Deque[T]) Empty() bool { return deque.Len() == 0 }

// PushFront 将 value 插入队首。
func (deque *Deque[T]) PushFront(value T) {
	deque.init()
	deque.items.PushFront(value)
}

// PushBack 将 value 插入队尾。
func (deque *Deque[T]) PushBack(value T) {
	deque.init()
	deque.items.PushBack(value)
}

// Front 查看队首元素，但不删除。
// 队列为空时返回零值和 false。
func (deque *Deque[T]) Front() (T, bool) {
	if deque.items == nil || deque.items.Len() == 0 {
		var zero T
		return zero, false
	}
	return deque.items.Front().Value.(T), true
}

// Back 查看队尾元素，但不删除。
// 队列为空时返回零值和 false。
func (deque *Deque[T]) Back() (T, bool) {
	if deque.items == nil || deque.items.Len() == 0 {
		var zero T
		return zero, false
	}
	return deque.items.Back().Value.(T), true
}

// PopFront 删除并返回队首元素。
// 队列为空时返回零值和 false。
func (deque *Deque[T]) PopFront() (T, bool) {
	if deque.items == nil || deque.items.Len() == 0 {
		var zero T
		return zero, false
	}
	element := deque.items.Front()
	value := element.Value.(T)
	deque.items.Remove(element)
	return value, true
}

// PopBack 删除并返回队尾元素。
// 队列为空时返回零值和 false。
func (deque *Deque[T]) PopBack() (T, bool) {
	if deque.items == nil || deque.items.Len() == 0 {
		var zero T
		return zero, false
	}
	element := deque.items.Back()
	value := element.Value.(T)
	deque.items.Remove(element)
	return value, true
}

// Clear 删除队列中的全部元素。
func (deque *Deque[T]) Clear() {
	if deque.items != nil {
		deque.items.Init()
	}
}

// Values 返回从队首到队尾的快照。
// 返回的切片与队列内部没有共享关系，调用者可以安全修改它。
func (deque *Deque[T]) Values() []T {
	values := make([]T, 0, deque.Len())
	if deque.items == nil {
		return values
	}
	for element := deque.items.Front(); element != nil; element = element.Next() {
		values = append(values, element.Value.(T))
	}
	return values
}

// String 返回队列从队首到队尾的字符串表示。
func (deque *Deque[T]) String() string {
	values := deque.Values()
	parts := make([]string, len(values))
	for i, value := range values {
		parts[i] = fmt.Sprint(value)
	}
	return "[" + strings.Join(parts, ", ") + "]"
}
