package leetcode

import (
	"encoding/json"
	"strconv"
	"strings"
)

func BuildIntArray(s string) [][]int {
	var result [][]int
	if err := json.Unmarshal([]byte(s), &result); err != nil {
		return nil
	}
	return result
}
func IntArrayToString(values [][]int) string {
	var builder strings.Builder

	builder.WriteByte('[')

	for i, row := range values {
		if i > 0 {
			builder.WriteByte(',')
		}

		builder.WriteByte('[')

		for j, value := range row {
			if j > 0 {
				builder.WriteByte(',')
			}

			builder.WriteString(strconv.Itoa(value))
		}

		builder.WriteByte(']')
	}

	builder.WriteByte(']')

	return builder.String()
}
