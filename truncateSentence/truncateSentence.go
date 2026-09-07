package truncateSentence

import "strings"

// 1816. 截断句子
// https://leetcode.cn/problems/truncate-sentence/
//
// 句子是由单空格分隔的单词串,截断为「前 k 个单词」并保留单空格分隔返回。
//
// 解法:按空格切词,取前 k 个再用空格拼回(题目保证 k 不超过单词总数)。
// 复杂度:时间 O(n),空间 O(n)。
func truncateSentence(s string, k int) string {
	return strings.Join(strings.Split(s, " ")[:k], " ")
}
