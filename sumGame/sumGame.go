package sumGame

import "unicode"

func sumGame(num string) bool {

	length := len(num)
	leftSum := 0
	rightSum := 0
	all := 0
	leftCount := 0
	rightCount := 0
	for l, r := length/2-1, length/2; l > -1 && r < length; l, r = l-1, r+1 {
		if unicode.IsDigit(rune(num[l])) {
			leftSum += int(num[l] - '0')
		}
		if unicode.IsDigit(rune(num[r])) {
			rightSum += int(num[r] - '0')
		}
		if num[l] == '?' {
			leftCount++
			all++
		}
		if num[r] == '?' {
			rightCount++
			all++
		}
	}

	return all%2 == 1 || (leftSum-rightSum != (rightCount-leftCount)*9/2)
}
