package queryString;

import (
	"strconv"
	"strings"
)

func queryString(s string, n int) bool {
    for i := 1; i <= n; i++ {
        if !strings.Contains(s, strconv.FormatUint(uint64(i), 2)) {
            return false
        }
    }
    return true
}

