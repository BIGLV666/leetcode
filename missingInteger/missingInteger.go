package missingInteger

func missingInteger(nums []int) int {
	res := 0
	sum := nums[0]
	hashTable := make(map[int]bool)
	hashTable[nums[0]] = true
	for i := 1; i < len(nums); i++ {
		if nums[i-1]+1 == nums[i] {
			sum += nums[i]
		} else {
			break
		}
	}
	for _, v := range nums {
		hashTable[v] = true
	}
	for i := sum; ; i++ {
		if !hashTable[i] {
			res = i
			break
		}
	}
	return res
}
