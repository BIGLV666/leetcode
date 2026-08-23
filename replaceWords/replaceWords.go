package replaceWords

import (
	"strings"
)

/**
 *@title{648.单词替换}
 *@link{https://leetcode.cn/problems/replace-words/}
 */
func replaceWords(dictionary []string, sentence string) string {
	table := make(map[string]bool)

	for _, word := range dictionary {
		table[word] = true
	}
	words := strings.SplitAfter(sentence, " ")
	for i, word := range words {
		sb := strings.Builder{}
		for _, ch := range word {
			sb.WriteRune(ch)
			if table[sb.String()] {
				words[i] = sb.String()
				if i < len(words)-1 {
					words[i] += " "
				}
				break
			}
		}
	}
	return strings.Join(words, "")
}
