package findDuplicate

import (
	"leetcode"
	"testing"
)
func Test1(t *testing.T) {
	leetcode.RunTests(
		t,
		findDuplicate,
		[]leetcode.TestCase{
			{
				Args: []any{
					[]string{"root/a 1.txt(abcd) 2.txt(efgh)","root/c 3.txt(abcd)","root/c/d 4.txt(efgh)","root 4.txt(efgh)"},
				},
				Expected: []any{
					[][]string{
						{"root/a/2.txt","root/c/d/4.txt","root/4.txt"},
						{"root/a/1.txt","root/c/3.txt"},
					},
				},
			},
		},
	)
}