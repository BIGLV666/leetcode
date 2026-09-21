package getFolderNames

import "strconv"

// getFolderNames 按输入顺序生成不重复的文件夹名，并复用已尝试过的后缀编号。
func getFolderNames(names []string) []string {
	next := make(map[string]int, len(names))
	used := make(map[string]struct{}, len(names))
	result := make([]string, 0, len(names))
	for _, name := range names {
		candidate := name
		if _, exists := used[candidate]; exists {
			for suffix := next[name]; ; suffix++ {
				candidate = name + "(" + strconv.Itoa(suffix) + ")"
				if _, exists := used[candidate]; !exists { next[name] = suffix + 1; break }
			}
		} else {
			next[name] = 1
		}
		used[candidate] = struct{}{}
		result = append(result, candidate)
	}
	return result
}
