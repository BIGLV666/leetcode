package minRemoveToMakeValid

import "testing"

func Test1(t *testing.T) {
	t.Log(minRemoveToMakeValid("lee(t(c)o)de)"))
}
func Test2(t *testing.T) {
	t.Log(minRemoveToMakeValid("a)b(c)d"))
}
func Test3(t *testing.T) {
	t.Log(minRemoveToMakeValid("))(("))
}
