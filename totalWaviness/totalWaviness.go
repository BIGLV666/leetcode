package totalWaviness

func totalWaviness(num1 int, num2 int) int {
	waviness := 0
    for i:=num1;i<=num2;i++{
        waviness += getWaviness(i)     
    }
    return waviness
}
func getDigits(num int) []int {
    digits := make([]int, 0, 4)
    for num > 0 {
        digits = append(digits, num%10)
        num /= 10
    }
    return digits
}
func getWaviness(num int) int {
    digits := getDigits(num)
    waviness := 0
	if len(digits) < 3 {
		return waviness
	}
    for i := 1; i < len(digits)-1; i++ {
        if digits[i] > digits[i-1] && digits[i] > digits[i+1] {
            waviness++
        }
        if digits[i] < digits[i-1] && digits[i] < digits[i+1] {
            waviness++
        }
    }
    return waviness
}