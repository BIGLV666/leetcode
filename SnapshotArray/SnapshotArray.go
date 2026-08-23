package main

import "sort"

type change struct {
	snapID int
	value  int
}

type SnapshotArray struct {
	history [][]change
	snapID  int
}

func Constructor(length int) SnapshotArray {
	return SnapshotArray{
		history: make([][]change, length),
		snapID:  0,
	}
}

func (this *SnapshotArray) Set(index int, val int) {
	records := this.history[index]

	// 同一个 snapID 内可能多次修改同一个位置，
	// 只需要保留最后一次修改。
	if len(records) > 0 && records[len(records)-1].snapID == this.snapID {
		records[len(records)-1].value = val
		return
	}

	this.history[index] = append(records, change{
		snapID: this.snapID,
		value:  val,
	})
}

func (this *SnapshotArray) Snap() int {
	currentSnapID := this.snapID
	this.snapID++
	return currentSnapID
}

func (this *SnapshotArray) Get(index int, snapID int) int {
	records := this.history[index]

	// 找到第一条 snapID > 目标 snapID 的记录。
	position := sort.Search(len(records), func(i int) bool {
		return records[i].snapID > snapID
	})

	// 没有找到 snapID <= 目标 snapID 的修改记录，
	// 说明该位置仍然是初始值 0。
	if position == 0 {
		return 0
	}

	return records[position-1].value
}
