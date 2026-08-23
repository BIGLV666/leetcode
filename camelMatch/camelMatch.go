package camelMatch

import (
	"strings"
	"unicode"
)

/**
1023. 驼峰式匹配
@link{https://leetcode.cn/problems/camelcase-matching/}
*/

func camelMatch(queries []string, pattern string) []bool {
	patterns := build(pattern)
	res := make([]bool, 0, len(queries))

	for _, s := range queries {
		sd := build(s)
		l := true
		if len(sd) != len(patterns) {
			res = append(res, false)
			continue
		}
		for i, s1 := range sd {
			if !is(s1, patterns[i]) {
				res = append(res, false)
				l = false
				break
			}
		}
		if l {
			res = append(res, l)
		}
	}
	return res
}
func build(s string) []string {
	r := strings.Builder{}
	res := make([]string, 0, len(s))
	for _, ch := range s {
		if unicode.IsUpper(ch) {
			res = append(res, r.String())
			r = strings.Builder{}
			r.WriteRune(ch)
		} else {
			r.WriteRune(ch)
		}
	}
	if r.Len() != 0 {
		res = append(res, r.String())
	}
	return res
}
func is(s1, s2 string) bool {
	i := 0
	j := 0
	for i < len(s1) && j < len(s2) {
		if s1[i] == s2[j] {
			i++
			j++
		} else {
			i++
		}
	}
	return j == len(s2)
}
