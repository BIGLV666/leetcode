package truncateSentence

import (
	"leetcode/common"
	"testing"
)

func TestTruncateSentence(t *testing.T) {
	common.RunTests(
		t,
		truncateSentence,
		[]common.TestCase{
			// 官方示例
			{Args: []any{"Hello how are you Contestant", 4}, Expected: "Hello how are you"},
			{Args: []any{"What is the solution to this problem", 4}, Expected: "What is the solution"},
			{Args: []any{"chopper is not a tanuki", 5}, Expected: "chopper is not a tanuki"},
			// 边界:k 等于单词总数,原样返回
			{Args: []any{"chopper is not a tanuki", 5}, Expected: "chopper is not a tanuki"},
			// 边界:k = 1,只留第一个单词
			{Args: []any{"hello world", 1}, Expected: "hello"},
			// 边界:单单词句子
			{Args: []any{"alone", 1}, Expected: "alone"},
			// 边界:单词内容含数字/标点也按空格切
			{Args: []any{"a1 b2 c3 d4", 2}, Expected: "a1 b2"},
		},
	)
}