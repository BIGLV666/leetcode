package maxNumberOfFamilies

import (
	"fmt"
	"sort"
)

/**
 * 1386. 安排电影院座位
 * @link{https://leetcode.cn/problems/cinema-seat-allocation/solutions/172443/an-pai-dian-ying-yuan-zuo-wei-by-leetcode-solution/?envType=daily-question&envId=2026-08-19}
 */
type node struct {
	index int
	rows  []int
}

func maxNumberOfFamilies(n int, reservedSeats [][]int) int {
	sort.Slice(reservedSeats, func(i, j int) bool {
		if reservedSeats[i][0] != reservedSeats[j][0] {
			return reservedSeats[i][0] < reservedSeats[j][0]
		}
		return reservedSeats[i][1] < reservedSeats[j][1]
	})
	temp := make(map[int][]int)
	for _, v := range reservedSeats {
		_, ok := temp[v[0]]
		if ok {
			temp[v[0]] = append(temp[v[0]], v[1])
		} else {
			temp[v[0]] = make([]int, 0, 100)
			temp[v[0]] = append(temp[v[0]], v[1])
		}
	}
	ros := make([]node, 0, len(temp))
	for k, v := range temp {
		ros = append(ros, node{index: k, rows: v})
	}
	sort.Slice(ros, func(i, j int) bool {
		return ros[i].index < ros[j].index
	})
	res := 0
	top := 0

	for i := range ros {
		r := ros[i].index
		c := ros[i].rows
		fmt.Println(c)
		if r != top-1 {
			res += (r - top - 1) * 2
			top = r
		}
		// 检查三个固定区间是否有预订
		reserved := make(map[int]bool)
		for _, seat := range c {
			reserved[seat] = true
		}
		left := !reserved[2] && !reserved[3] && !reserved[4] && !reserved[5]
		mid := !reserved[4] && !reserved[5] && !reserved[6] && !reserved[7]
		right := !reserved[6] && !reserved[7] && !reserved[8] && !reserved[9]

		if left && right {
			res += 2
		} else if left || mid || right {
			res++
		}
	}
	res += (n - reservedSeats[len(reservedSeats)-1][0]) * 2
	return res

}
func maxNumberOfFamilies1(n int, reservedSeats [][]int) int {
	sort.Slice(reservedSeats, func(i, j int) bool {
		return reservedSeats[i][0] < reservedSeats[j][0]
	})
	fmt.Println(reservedSeats)
	top := 1
	final := 0
	res := 0
	for final < len(reservedSeats) {
		v := reservedSeats[final]
		if v[0] != top {
			fmt.Printf("%d层，可有%d个家庭\n", v[0]-top, (v[0]-top)*2)
			res += (v[0] - top) * 2
			top = v[0]
			continue
		}
		temp := make([]int, 0, len(reservedSeats))
		for final < len(reservedSeats) && reservedSeats[final][0] == v[0] {
			temp = append(temp, reservedSeats[final][1])
			final++
		}

		fmt.Println("temp", temp)

		for i := 1; i < len(temp); i++ {
			if len(temp) == 1 {
				if temp[0] == 1 || temp[0] == 10 {
					fmt.Printf("%d层，可有%d个家庭\n", top, 2)
					res += 2
				} else {
					fmt.Printf("%d层，可有%d个家庭\n", top, 1)
					res += 1
				}
				break
			}
			r := temp[i] - temp[i-1] - 1
			if r >= 4 {
				if r/4 == 2 {
					fmt.Printf("%d层，可有%d个家庭\n", top, 2)
					res += 2
					break
				} else {
					fmt.Printf("%d层，可有%d个家庭\n", top, 1)
					res++
				}
			}
			if temp[0] > 5 {
				res++
			}
			if temp[len(temp)-1] < 6 {
				res++
			}
		}
		final++
		top++
	}
	fmt.Println("top", top)
	res += (n - reservedSeats[len(reservedSeats)-1][0]) * 2
	return res
}
