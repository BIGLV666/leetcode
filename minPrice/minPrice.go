package minPrice

import "sort"

func minPrice(prices []int, discounts []int) float64 {
	sort.Sort(sort.IntSlice(prices))
	sort.Sort(sort.IntSlice(discounts))
	res := float64(0)
	i := len(prices) - 1
	j := len(discounts) - 1
	for i >= 0 && j >= 0 {
		res += float64(prices[i]) * (1 - float64(discounts[j])/100)
		i--
		j--
	}
	for i >= 0 {
		res += float64(prices[i])
		i--
	}
	return res

}
