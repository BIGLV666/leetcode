package complexNumberMultiply

import (
	"strconv"
	"strings"
)

func complexNumberMultiply(num1 string, num2 string) string {
	var get func(s string) (int, int)
	get = func(s string) (int, int) {
		cs := strings.Split(s, "+")
		n1, _ := strconv.Atoi(cs[0])
		n2, _ := strconv.Atoi(cs[1][:len(cs[1])-1])
		return n1, n2
	}
	a, b := get(num1)
	c, d := get(num2)

	return strconv.Itoa(a*c-b*d) + "+" + strconv.Itoa(a*d+b*c) + "i"
}
