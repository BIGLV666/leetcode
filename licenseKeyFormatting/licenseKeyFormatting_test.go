package licenseKeyFormatting

import "testing"

func Test1(t *testing.T) {
	t.Log(licenseKeyFormatting("5F3Z-2e-9-w", 4))
}
func Test2(t *testing.T) {
	t.Log(licenseKeyFormatting("2-5g-3-J", 2))
}
