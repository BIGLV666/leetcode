package canVisitAllRooms

/*
*841. 钥匙和房间
@link{https://leetcode.cn/problems/keys-and-rooms/description/}
*/
func canVisitAllRooms(rooms [][]int) bool {
	lookedRoom := make(map[int]bool)
	dq := make([]int, 0, len(rooms))
	lookedRoom[0] = true
	for i := 0; i < len(rooms[0]); i++ {
		dq = append(dq, rooms[0][i])
	}
	for len(dq) != 0 {
		roomIndex := dq[0]
		dq = dq[1:]
		if lookedRoom[roomIndex] {
			continue
		}
		lookedRoom[roomIndex] = true
		for i := range rooms[roomIndex] {
			dq = append(dq, rooms[roomIndex][i])
		}
	}

	return len(rooms) == len(lookedRoom)
}
