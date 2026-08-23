package checkStraightLine

func checkStraightLine(coordinates [][]int) bool {
	A := coordinates[1][1] - coordinates[0][1]
	B := coordinates[0][0] - coordinates[1][0]
	c := coordinates[1][0]*coordinates[0][1] - coordinates[0][0]*coordinates[1][1]
	for i := 2; i < len(coordinates); i++ {
		if coordinates[i][0]*A+B*coordinates[i][1]+c != 0 {
			return false
		}
	}
	return true
}
