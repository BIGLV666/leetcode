package findReplaceString

import "strings"

func findReplaceString(s string, indices []int, sources []string, targets []string) string {
	type replacement struct {
		source string
		target string
	}

	replacements := make(map[int]replacement, len(indices))

	// 建立“起始位置 -> 替换内容”的映射。
	// 这样即使 indices 没有排序，也可以按照字符串位置顺序处理。
	for i, index := range indices {
		replacements[index] = replacement{
			source: sources[i],
			target: targets[i],
		}
	}

	var result strings.Builder

	for position := 0; position < len(s); {
		item, ok := replacements[position]

		// 当前位置存在替换规则，并且 source 确实匹配。
		if ok && strings.HasPrefix(s[position:], item.source) {
			result.WriteString(item.target)
			position += len(item.source)
			continue
		}

		// 没有替换规则，或者 source 不匹配，只保留当前字符。
		result.WriteByte(s[position])
		position++
	}

	return result.String()
}
