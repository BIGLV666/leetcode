package stringMatching

import "strings"

func stringMatching(words []string) []string {
	res := make(map[string]bool)
	for i := range words {
		for j := range words {
			if strings.Contains(words[i], words[j]) && i != j {
				res[words[j]] = true
			}
		}
	}
	l := make([]string, 0, len(res))
	for k, _ := range res {
		l = append(l, k)
	}
	return l
}
