package leetcode

import (
	"fmt"
	"reflect"
	"strings"
	"testing"
)

// RunTests 通用测试方法
// fn: 被测函数（任意签名）
// cases: 测试用例列表，每个元素是 TestCase
//
// 用法示例：
//
//	leetcode.RunTests(t, canVisitAllRooms, []leetcode.TestCase{
//	    {Args: []any{leetcode.BuildIntArray("[[1],[2],[3],[]]")}, Expected: true},
//	    {Args: []any{leetcode.BuildIntArray("[[1,3],[3,0,1],[2],[0]]")}, Expected: false},
//	})
type TestCase struct {
	Args     []any // 按照函数参数顺序传入
	Expected any   // 期望的返回值（单返回值）或 []any（多返回值）
}

func RunTests(t *testing.T, fn any, cases []TestCase) {
	t.Helper()
	fnVal := reflect.ValueOf(fn)
	fnType := fnVal.Type()

	for i, tc := range cases {
		// 构造入参
		in := make([]reflect.Value, len(tc.Args))
		for j, arg := range tc.Args {
			in[j] = reflect.ValueOf(arg)
		}

		// 调用函数
		out := fnVal.Call(in)
		
		// 收集实际返回值
		var got any
		if fnType.NumOut() == 1 {
			got = out[0].Interface()
		} else {
			vals := make([]any, len(out))
			for k, v := range out {
				vals[k] = v.Interface()
			}
			got = vals
		}

		// 比较
		if !reflect.DeepEqual(got, tc.Expected) {
			t.Errorf("Test %d: Args=%v\n  expected: %v\n  got:      %v",
				i+1, formatArgs(tc.Args), tc.Expected, got)
		} else {
			fmt.Printf("Test %d: PASS\n", i+1)
		}
	}
}

func formatArgs(args []any) string {
	var s strings.Builder
	s.WriteString("[")
	for i, a := range args {
		if i > 0 {
			s.WriteString(", ")
		}
		s.WriteString(fmt.Sprintf("%v", a))
	}
	return s.String() + "]"
}
