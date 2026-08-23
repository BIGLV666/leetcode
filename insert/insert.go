package insert

func insert(intervals [][]int, newInterval []int) [][]int {
	res := make([][]int, 0, len(intervals)+1)
	i := 0
	n := len(intervals)

	// 1. 添加所有在newInterval左侧且不重叠的区间
	for i < n && intervals[i][1] < newInterval[0] {
		res = append(res, intervals[i])
		i++
	}

	// 2. 合并所有与newInterval重叠的区间
	for i < n && intervals[i][0] <= newInterval[1] {
		newInterval[0] = min(newInterval[0], intervals[i][0])
		newInterval[1] = max(newInterval[1], intervals[i][1])
		i++
	}
	res = append(res, newInterval)

	// 3. 添加所有在newInterval右侧的区间
	for i < n {
		res = append(res, intervals[i])
		i++
	}

	return res
}
