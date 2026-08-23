package createTargetArray

import "container/list"

func getElement(arr list.List, index int) *list.Element {
	i := 0
	for cur := arr.Front(); cur != nil; cur = cur.Next() {
		if i == index {
			return cur
		}
		i++ // 修复：递增索引
	}
	return nil // 修复：当索引超出范围时返回nil，InsertBefore会在末尾插入
}

func createTargetArray(nums []int, index []int) []int {
	target := new(list.List)
	for i := range index {
		pos := getElement(*target, index[i])
		if pos == nil {
			// 在列表末尾插入
			target.PushBack(nums[i])
		} else {
			target.InsertBefore(nums[i], pos)
		}
	}
	res := make([]int, 0, target.Len())
	for cur := target.Front(); cur != nil; cur = cur.Next() {
		res = append(res, cur.Value.(int))
	}
	return res
}
