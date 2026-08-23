package integerReplacement

func integerReplacement(n int) int {
	if n == 1 {
		return 0
	}
	if n%2 == 0 {
		return integerReplacement(n/2) + 1
	}
	return 2 + min(integerReplacement(n/2), integerReplacement(n/2+1))
}
