package furthestDistanceFromOrigin

import "math"

func furthestDistanceFromOrigin(moves string) int {
	cnt := 0
	k := 0
	for _, v := range moves {
		if v == 'R' {
			cnt++
		}
		if v == 'L' {
			cnt--
		}
		if v == '_' {
			k++
		}
	}
	return int(math.Abs(float64(cnt))) + k
}
