package maxProfitAssignment

import (
	"slices"
	"strconv"
	"strings"
)

import "sort"

func (node *Node) String() string {
	res := strings.Builder{}
	res.WriteString("difficulty:")
	res.WriteString(strconv.Itoa(node.difficulty))
	res.WriteString("\n")
	res.WriteString("profit:")
	res.WriteString(strconv.Itoa(node.profit))
	res.WriteString("\n")
	return res.String()
}

type Node struct {
	difficulty int
	profit     int
}

func maxProfitAssignment(difficulty []int, profit []int, worker []int) int {
	var list []*Node
	var res = 0
	for i := range difficulty {
		list = append(list, &Node{
			difficulty: difficulty[i],
			profit:     profit[i],
		})
	}

	sort.Slice(list, func(i, j int) bool {
		return list[i].difficulty < list[j].difficulty
	})
	slices.Sort(worker)
	top := 0
	Max := 0
	for _, num := range worker {
		if top < len(list) && num >= list[top].difficulty {
			for top < len(list) {
				if list[top].difficulty > num {
					break
				}
				Max = max(Max, list[top].profit)
				top++
			}
		}
		res += Max

	}
	return res
}
