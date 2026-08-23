package getHint

import "testing"

func Test1(t *testing.T) {
	t.Log(getHint("1807", "7810"))
}
func Test2(t *testing.T) {
	t.Log(getHint("1123", "0111"))
}
func Test3(t *testing.T) {
	t.Log(getHint("1122", "1222"))
}
