package reorderedPowerOf2

import (
	"bytes"
)

func reorderedPowerOf2(n int) bool {
	res := f(n)
	for i := 2; i < 1000000001; i = i * 2 {
		if bytes.Equal(res, f(i)) {
			return true
		}
	}
	return false
}
func f(x int) []byte {
	cnt := make([]byte, 10)
	for x > 0 {
		cnt[x%10]++
		x /= 10
	}
	return cnt
}
