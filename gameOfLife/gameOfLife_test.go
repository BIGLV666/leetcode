package gameOfLife

import (
	"leetcode"
	"testing"
)

func Test1(t *testing.T) {
	arr := leetcode.BuildIntArray("[[0,1,0],[0,0,1],[1,1,1],[0,0,0]]")
	gameOfLife(arr)
}
func Test2(t *testing.T) {
	arr := leetcode.BuildIntArray("[[1,1],[1,0]]")
	 gameOfLife(arr)

}
