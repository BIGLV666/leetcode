package main

import (
	"fmt"
	"math"
	"strconv"
	"strings"
	"unicode"
)

func f(s string) string {
	chars := []rune(s)
	temp := strings.Builder{}
	is := false

	for i := 0; i < len(chars); i++ {
		if unicode.IsLetter(chars[i]) && chars[i] != '-' && chars[i] != '+' {
			return temp.String()
		} else if chars[i] == '-' || chars[i] == '+' {
			if is {
				return temp.String()
			}
			if temp.Len() == 0 {
				temp.WriteRune(chars[i])
				is = true
			} else {
				return temp.String()
			}

		} else if chars[i] == ' ' {
			if temp.Len() != 0 {
				return temp.String()
			}
			continue
		} else if unicode.IsDigit(chars[i]) {
			temp.WriteRune(chars[i])
		} else {
			return temp.String()
		}
	}
	return temp.String()
}

func myAtoi(s string) int {
	s = f(s)
	chars := []rune(s)
	ress := strings.Builder{}

	for i := 0; i < len(chars); i++ {
		if unicode.IsDigit(chars[i]) {
			if chars[i] == '0' {
				if ress.Len() == 0 || ress.Len() == 1 && ress.String() == "-" {
					continue
				}
			}
			ress.WriteRune(chars[i])
		} else {
			ress.WriteRune(chars[i])
		}
	}
	r := ress.String()
	if len(r) > 12 {
		if r[0] == '-' {
			return math.MinInt32
		}
		return math.MaxInt32
	}
	res, err := strconv.Atoi(r)
	if err == nil {
		if res > math.MaxInt32 {
			return math.MaxInt32
		} else if res < math.MinInt32 {
			return math.MinInt32
		} else {
			return int(res)
		}
	}
	return 0
}
func main() {
	s := "1337c0d3"
	s2 := " -042"
	s3 := "20000000000000000000"
	s4 := "3.14159"
	s5 := " -000000000000001"
	s6 := "  0000000000012345678"
	fmt.Println(myAtoi(s))
	fmt.Println(myAtoi(s2))
	fmt.Println(myAtoi(s3))
	fmt.Println(myAtoi(s4))
	fmt.Println(myAtoi(s5))
	fmt.Println(myAtoi(s6))
}
