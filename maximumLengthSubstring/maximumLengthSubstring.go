package maximumLengthSubstring

func maximumLengthSubstring(s string) int {
	res := 0
	table := make(map[byte]int)
	l := 0
	for r := 0; r < len(s); r++ {
		table[s[r]]++
		for table[s[r]] > 2 {
			table[s[l]]--
			l++
		}
		res = max(r-l+1, res)
	}
	return res
}
