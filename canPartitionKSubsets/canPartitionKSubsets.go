package main

import "fmt"

func canPartitionKSubsets(nums []int, k int) bool {
	sum := 0
	for _, n := range nums {
		sum += n
	}
	if sum%k != 0 {
		return false
	}
	n := sum / k
	hashTable := make(map[int]bool)
	for i := range nums {
		if hashTable[n-nums[i]] {
			delete(hashTable, n-nums[i])

		} else if nums[i] == n {
			delete(hashTable, nums[i])
		} else {
			hashTable[nums[i]] = true
		}
	}

	fmt.Println(hashTable)
	return len(hashTable) == 0
}
func main() {
	nusm := []int{1, 1, 1, 1, 2, 2, 2, 2}
	fmt.Println(canPartitionKSubsets(nusm, 2))
}
