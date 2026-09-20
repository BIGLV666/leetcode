package findOcurrences

import "strings"

func findOcurrences(text string, first string, second string) []string {
	ch := strings.Split(text, " ")
	res := make([]string, 0, len(ch))
	for i := 1; i < len(ch)-1; i++ {
		if ch[i] == second && ch[i-1] == first {
			res = append(res, ch[i+1])
		}
	}
	return res
}
