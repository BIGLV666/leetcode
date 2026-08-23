package countCharacters

import (
	"maps"
)

func countCharacters(words []string, chars string) int {
	table := make(map[rune]int)

	for _, ch := range chars {
		table[ch]++
	}
	total := 0
	for _, word := range words {
		temp := maps.Clone(table)
		is := true
		for _, ch := range word {
			v, ok := temp[ch]
			if !ok {
				is = false
				break
			}
			if v < 1 {
				is = false
				break
			}
			temp[ch]--
		}
		if is {
			total += len(word)
		}
	}
	return total
}
