package findWords

import (
    "reflect"
    "sort"
    "testing"
)

func TestFindWords(t *testing.T) {
    board := [][]byte{{'o','a','a','n'}, {'e','t','a','e'}, {'i','h','k','r'}, {'i','f','l','v'}}
    want := []string{"oath", "eat"}
    if got := findWords(board, []string{"oath", "pea", "eat", "rain"}); !reflect.DeepEqual(got, want) { t.Fatalf("got %v, want %v", got, want) }
}

func TestFindWordsOptimized(t *testing.T) {
    board := [][]byte{{'a','b'}, {'c','d'}}
    got := findWordsOptimized(board, []string{"ab", "abcd", "acdb", "ab"})
    // 题目只要求返回所有命中的单词，顺序不作要求，故排序后比较。
    sort.Strings(got)
    if want := []string{"ab", "acdb"}; !reflect.DeepEqual(got, want) { t.Fatalf("got %v, want %v", got, want) }
}

func TestFindWordsEmpty(t *testing.T) {
    if got := findWordsOptimized(nil, []string{"a"}); len(got) != 0 { t.Fatalf("got %v", got) }
}
