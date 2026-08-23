package main

func numPairsDivisibleBy60(time []int) int {
	res := 0
	catch := make(map[int]int)
	for i := range time {
		temp := time[i] % 60
		res += catch[(60-temp)%60]
		catch[temp]++

	}
	return res
}
