package numJewelsInStones

func numJewelsInStones(jewels string, stones string) int {
	set := make(map[rune]bool)
	for _, v := range jewels {
		set[v] = true
	}

	res := 0
	for _, ch := range stones {
		if set[ch] {
			res++
		}
	}
	return res
}
