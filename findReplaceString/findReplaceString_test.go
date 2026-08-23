package findReplaceString

import "testing"

func Test1(t *testing.T) {
	t.Log(findReplaceString("abcd", []int{0, 2}, []string{"a", "c"}, []string{"eee", "ffff"}))
}
