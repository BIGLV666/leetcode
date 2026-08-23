package main

import "fmt"

func f(n int) []rune {
	b := make([]rune, 0)
	for n > 0 {
		b = append(b, rune(n%10))
		n /= 10
	}
	for l, r := 0, len(b)-1; r > l; l, r = l+1, r-1 {
		b[l], b[r] = b[r], b[l]
	}
	return b
}

func compress(chars []byte) int {
	count := 0
	read := 0
	write := 0
	for read < len(chars) {
		left := read
		current := chars[read]
		for read < len(chars) && chars[read] == current {
			read++
		}
		count = read - left
		chars[write] = current
		write++
		if count > 1 {
			b := f(count)
			for j := range b {
				chars[write] = byte(b[j]) + '0'
				write++
			}
		}
	}
	return write
}
func main() {
	chars := []byte{'a', 'a', 'b', 'b', 'c', 'c', 'c'}
	fmt.Println(compress(chars))
	chars = []byte{'a'}
	fmt.Println(compress(chars))
}
