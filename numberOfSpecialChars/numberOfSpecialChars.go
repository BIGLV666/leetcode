package numberOfSpecialChars

import (
	"fmt"
	"unicode"
)

func numberOfSpecialChars(word string) int {
	set := make(map[int32]bool)
	cnt := 0
	for _, ch := range word {
		if unicode.IsLower(ch) {
			set[unicode.ToLower(ch)-'a'] = true
		}
	}

	for _, ch := range word {
		if !unicode.IsLower(ch) {
			if set[unicode.ToLower(ch)-'a'] && !set[ch-'a'] {
				fmt.Println(ch)
				cnt++
				set[ch-'a'] = true
			}
		}
	}

	return cnt
}
