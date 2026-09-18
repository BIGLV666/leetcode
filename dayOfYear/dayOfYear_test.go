package dayOfYear

import (
	"fmt"
	"testing"
	"time"

	"leetcode/common"
)

func TestDayOfYear(t *testing.T) {
	common.RunTests(
		t,
		dayOfYear,
		[]common.TestCase{
			// 官方示例
			{Args: []any{"2019-01-09"}, Expected: 9},
			{Args: []any{"2019-02-10"}, Expected: 41},
			{Args: []any{"2003-03-01"}, Expected: 60},
			{Args: []any{"2004-03-01"}, Expected: 61},
			// 边界: 闰年与平年的 2 月 29 日 / 3 月 1 日
			{Args: []any{"2000-02-29"}, Expected: 60}, // 2000 是闰年(能被 400 整除)
			{Args: []any{"2024-02-29"}, Expected: 60}, // 2024 是闰年
			{Args: []any{"1900-03-01"}, Expected: 60}, // 1900 不是闰年(能被 100 整除但不能被 400)
			{Args: []any{"2100-03-01"}, Expected: 60}, // 2100 同理不是闰年
			// 边界: 一年的首尾
			{Args: []any{"2019-01-01"}, Expected: 1},
			{Args: []any{"2019-12-31"}, Expected: 365},
			{Args: []any{"2020-12-31"}, Expected: 366},
			{Args: []any{"2000-12-31"}, Expected: 366},
		},
	)

	// 随机对拍:与标准库 time.Parse(...).YearDay() 互验(完全独立的实现)
	years := []int{1900, 1999, 2000, 2004, 2023, 2024, 2100, 2400}
	for _, year := range years {
		for month := 1; month <= 12; month++ {
			for _, day := range []int{1, 13, 28} {
				date := fmt.Sprintf("%04d-%02d-%02d", year, month, day)
				parsed, err := time.Parse("2006-01-02", date)
				if err != nil {
					t.Fatalf("用例日期本身非法: %s (%v)", date, err)
				}
				want := parsed.YearDay()
				if got := dayOfYear(date); got != want {
					t.Fatalf("%s: want %d, got %d", date, want, got)
				}
			}
		}
	}
}
