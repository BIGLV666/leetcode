package stoneGameIX

import "math"

func stoneGameIX(stones []int) bool {
	cnt := [3]int{0, 0, 0}
	for _, v := range stones {
		cnt[v%3]++
	}
	if cnt[0]%2 == 0 {
		return !(cnt[1] == 0 || cnt[2] == 0)
	} else {
		return !(math.Abs(float64(cnt[1]-cnt[2])) <= 2)
	}

}