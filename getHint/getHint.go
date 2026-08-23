package getHint

import (
	"fmt"
	"strconv"
	"strings"
)

func getHint(secret string, guess string) string {
	grop_1 := make(map[int]int)
	group_2 := make(map[int]int)
	for i := range secret {
		n, _ := strconv.Atoi(string(secret[i]))
		grop_1[n]++
	}
	for i := range guess {
		n, _ := strconv.Atoi(string(guess[i]))
		group_2[n]++
	}
	A := 0
	B := 0
	for i := range guess {
		n, _ := strconv.Atoi(string(guess[i]))
		if secret[i] == guess[i] {
			group_2[n]--
			grop_1[n]--
			A++
		}
	}

	fmt.Println(grop_1)
	for i := range guess {
		n, _ := strconv.Atoi(string(guess[i]))

		if val, ok := grop_1[n]; ok && val > 0 && group_2[n] > 0 {
			B++
			grop_1[n]--
			group_2[n]--
		}
	}
	res := strings.Builder{}
	res.WriteString(strconv.Itoa(A) + "A" + strconv.Itoa(B) + "B")
	return res.String()

}
