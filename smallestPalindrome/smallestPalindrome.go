package main

import (
	"container/heap"
	"fmt"
)

type RuneHeap []rune

func (h RuneHeap) Len() int           { return len(h) }
func (h RuneHeap) Less(i, j int) bool { return h[i] < h[j] }
func (h RuneHeap) Swap(i, j int) {
	h[i], h[j] = h[j], h[i]
}
func (h *RuneHeap) Push(Value any) {
	if group[Value.(rune)] != 0 {
		return
	}
	*h = append(*h, Value.(rune))
}
func (h *RuneHeap) Pop() any {
	old := *h
	lastIndex := len(old) - 1
	Value := old[lastIndex]
	*h = old[:lastIndex]
	return Value
}

var group = make(map[rune]int)

func smallestPalindrome(str string) string {
	if len(str) == 1 {
		return str
	}
	h := RuneHeap{}

	chars := []rune(str)

	for _, char := range chars {

		heap.Push(&h, char)
		group[char]++

	}
	l, r := 0, len(str)-1
	for h.Len() > 0 {
		current := heap.Pop(&h).(rune)
		if group[current]%2 == 1 {
			chars[len(chars)/2] = current
			group[current]--
		}
		for group[current] > 0 {
			chars[l], chars[r] = current, current
			group[current] -= 2
			l, r = l+1, r-1
		}
	}
	return string(chars)
}

func main() {
	fmt.Println(smallestPalindrome("babab"))
	fmt.Println(smallestPalindrome("daccad"))
	fmt.Println(smallestPalindrome("rur"))
}
