package findrelativeranks

import (
	"sort"
	"strconv"
)

func findRelativeRanks(score []int) []string {
	arr := make([]int, len(score))
	copy(arr, score)
	sort.Slice(arr, func(i, j int) bool {
		return arr[i] > arr[j]
	})
	table := make(map[int]string)
	s := []string{"Gold Medal", "Silver Medal", "Bronze Medal"}
	res := make([]string, len(score))
	top := 0
	for i := 0; i < len(score); i++ {
		if top < 3 {
			table[arr[i]] = s[top]
			top++
		} else {
			table[arr[i]] = strconv.Itoa(i + 1)
		}
	}
	for i := 0; i < len(score); i++ {
		res[i] = table[score[i]]
	}
	return res

}
