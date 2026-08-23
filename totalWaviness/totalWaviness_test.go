package totalWaviness	
import (
	"testing"
	"leetcode"
)
func TestTotalWaviness(t *testing.T) {
	leetcode.RunTests(
		t,
		totalWaviness,
		[]leetcode.TestCase{
			{
				Args: []any{1,100},
				Expected: 0,
			},
			{
				Args: []any{120,130},
				Expected: 3,
			},
			{
				Args: []any{198,202},
				Expected: 3,
			},
			{
				Args: []any{4848,4848},
				Expected: 2,
			},
		},
	)
}
