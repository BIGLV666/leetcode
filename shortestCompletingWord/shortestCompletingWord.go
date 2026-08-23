package main

import (
	"fmt"
	"unicode"
)

func shortestCompletingWord(licensePlate string, words []string) (ans string) {
	cnt := [26]int{}
	for _, ch := range licensePlate {
		if unicode.IsLetter(ch) {
			cnt[unicode.ToLower(ch)-'a']++
		}
	}

next:
	for _, word := range words {
		c := [26]int{}
		for _, ch := range word {
			c[ch-'a']++
		}
		for i := range 26 {
			if c[i] < cnt[i] {
				continue next
			}
		}
		if ans == "" || len(word) < len(ans) {
			ans = word
		}
	}
	return
}

func main() {
	fmt.Print(shortestCompletingWord("1s3 PSt", []string{"step", "steps", "stripe", "stepple"}))
}
