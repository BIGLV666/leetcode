package findDuplicate

import "strings"

/**
 * @Description: 609. 文件系统中的重复文件
 * @link:{https://leetcode.cn/problems/find-duplicate-file-in-system/description/}
 * @param paths []string
 * @return [][]string
 */
func findDuplicate(paths []string) [][]string {
	group := make(map[string][]string)
	for _, path := range paths {
		ps := strings.Split(path, " ")
		for _, p := range ps[1:] {
			content := getContent(p)
			group[content] = append(group[content], ps[0]+"/"+deleteContent(p))
		}
	}
	res := [][]string{}
	for _, v := range group {
		if len(v) > 1 {
			res = append(res, v)
		}
	}
	return res
}
func getContent(path string) string {
	index := strings.Index(path, "(")
	return path[index+1 : len(path)-1]
}
func deleteContent(path string) string {
	return path[:strings.Index(path, "(")]
}
