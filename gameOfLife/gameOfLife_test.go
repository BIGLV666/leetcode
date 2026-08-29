package gameOfLife

import (
	"leetcode/common"
	"testing"
)

func Test1(t *testing.T) {
	arr := common.BuildIntArray("[[0,1,0],[0,0,1],[1,1,1],[0,0,0]]")
	gameOfLife(arr)
}
func Test2(t *testing.T) {
	arr := common.BuildIntArray("[[1,1],[1,0]]")
	gameOfLife(arr)

}
