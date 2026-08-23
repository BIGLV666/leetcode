package minPrice

import "testing"

func Test1(t *testing.T) {
	prices := []int{10, 30, 21}
	discounts := []int{50, 60}
	result := minPrice(prices, discounts)
	expected := 32.50000
	if result != expected {
		t.Errorf("Expected %f, got %f", expected, result)
	}
}
func Test2(t *testing.T) {
	prices := []int{100, 70}
	discounts := []int{10, 40, 50}
	result := minPrice(prices, discounts)
	expected := 92.00000
	if result != expected {
		t.Errorf("Expected %f, got %f", expected, result)
	}
}
func Test3(t *testing.T) {
	prices := []int{7, 3, 9}
	discounts := []int{100, 100}
	result := minPrice(prices, discounts)
	expected := 3.00000
	if result != expected {
		t.Errorf("Expected %f, got %f", expected, result)
	}
}
func Test4(t *testing.T) {
	prices := []int{7}
	discounts := []int{100, 100}
	result := minPrice(prices, discounts)
	expected := 0.00000
	if result != expected {
		t.Errorf("Expected %f, got %f", expected, result)
	}
}
