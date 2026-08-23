package maskPII

import (
	"fmt"
	"testing"
)

func Test1(t *testing.T) {
	s := "AB@qq.com"
	result := maskPII(s)
	expected := "a*****b@qq.com"
	if result != expected {
		fmt.Println(result)
		t.Errorf("Expected %s, got %s", expected, result)
	}
}

func Test2(t *testing.T) {
	s := "LeetCode@LeetCode.com"
	result := maskPII(s)
	expected := "l*****e@leetcode.com"
	if result != expected {
		fmt.Println(result)
		t.Errorf("Expected %s, got %s", expected, result)
	}
}
func Test3(t *testing.T) {
	s := "1(234)567-890"
	result := maskPII(s)
	expected := "***-***-7890"
	if result != expected {
		fmt.Println(result)
		t.Errorf("Expected %s, got %s", expected, result)
	}
}
