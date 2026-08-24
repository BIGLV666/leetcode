package totalWaviness	
import (
	"testing"
	"leetcode/common"
)
func TestTotalWaviness(t *testing.T) {
	common.RunTests(
		t,
		totalWaviness,
		[]common.TestCase{
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
