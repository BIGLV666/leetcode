package maskPII

import (
	"strings"
	"unicode"
)

func maskPII(s string) string {
	if strings.Contains(s, "@") {
		ss := strings.Split(s, "@")
		s1 := ss[0]
		res := strings.Builder{}
		res.WriteRune(unicode.ToLower(rune(s1[0])))
		res.WriteString("*****")
		res.WriteRune(unicode.ToLower(rune(s1[len(s1)-1])))
		res.WriteString("@")
		for i := 1; i < len(ss); i++ {
			for _, ch := range ss[i] {
				res.WriteRune(unicode.ToLower(ch))
			}
		}
		return res.String()
	}
	return to0(s, getInt(s))

}
func to0(s string, n int) string {
	chars := []rune{}
	for i := len(s) - 1; i > -1; i-- {
		c := s[i]
		ch := rune(c)
		if len(chars) == 4 {
			break
		}
		if unicode.IsDigit(ch) {
			chars = append(chars, ch)
		}
	}
	res := strings.Builder{}
	switch n {
	case 0:
		res.WriteString("***-***-")
		break
	case 1:
		res.WriteString("+*-***-***-")
		break
	case 2:
		res.WriteString("+**-***-***-")
		break
	case 3:
		res.WriteString("+***-***-***-")

	}

	for i := len(chars) - 1; i > -1; i-- {
		res.WriteRune(chars[i])
	}
	return res.String()
}
func getInt(s string) int {
	count := 0
	for _, ch := range s {
		if unicode.IsDigit(ch) {
			count++
		}
	}
	return count - 10
}
