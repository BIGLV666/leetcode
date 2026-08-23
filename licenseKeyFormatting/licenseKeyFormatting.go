package licenseKeyFormatting

import (
	"strings"
	"unicode"
)

func rever(s string) string {
	chars := []rune(s)
	for l, r := 0, len(chars)-1; l < r; l, r = l+1, r-1 {
		chars[l], chars[r] = chars[r], chars[l]
	}
	return string(chars)
}
func String(s []string) string {
	res := strings.Builder{}
	for _, ch := range s {
		res.WriteString(ch)
	}
	return res.String()
}
func licenseKeyFormatting(s string, k int) string {
	ss := strings.Split(s, "-")
	s = String(ss)
	s = rever(s)
	count := 0
	sb := strings.Builder{}
	for _, ch := range s {
		sb.WriteRune(unicode.ToUpper(ch))
		count++
		if count == k {
			count = 0
			sb.WriteRune('-')

		}
	}
	s = sb.String()
	if len(s) == 0 || len(s) == 1 {
		return s
	}
	if s[len(s)-1] == '-' {
		s = s[:len(s)-1]
	}

	return rever(s)
}
