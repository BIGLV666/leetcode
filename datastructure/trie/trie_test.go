package trie

import (
	"reflect"
	"strings"
	"testing"
)

func TestTrieBasicOperations(t *testing.T) {
	var trie Trie // 验证零值可以直接使用。
	for _, word := range []string{"apple", "app", "应用", "应用题", "apple"} {
		trie.Insert(word)
	}

	if trie.Count() != 4 {
		t.Fatalf("Count() = %d, want 4", trie.Count())
	}
	for _, word := range []string{"apple", "app", "应用", "应用题"} {
		if !trie.Search(word) {
			t.Fatalf("Search(%q) = false", word)
		}
	}
	for _, word := range []string{"ap", "应用中", "banana"} {
		if trie.Search(word) {
			t.Fatalf("Search(%q) = true", word)
		}
	}
	if !trie.StartsWith("app") || !trie.StartsWith("应用") || trie.StartsWith("banana") {
		t.Fatal("StartsWith 结果错误")
	}
	if trie.PrefixCount("app") != 2 || trie.PrefixCount("应用") != 2 {
		t.Fatal("PrefixCount 结果错误")
	}
	if got, want := trie.Words(), []string{"app", "apple", "应用", "应用题"}; !reflect.DeepEqual(got, want) {
		t.Fatalf("Words() = %v, want %v", got, want)
	}
}

func TestTrieString(t *testing.T) {
	var empty Trie
	if got, want := empty.String(), "[]"; got != want {
		t.Fatalf("empty String() = %q, want %q", got, want)
	}
	if got, want := empty.DebugString(), "Trie{count: 0}\n<未初始化>"; got != want {
		t.Fatalf("empty DebugString() = %q, want %q", got, want)
	}

	trie := New()
	for _, word := range []string{"app", "apple", "应用"} {
		trie.Insert(word)
	}
	if got, want := trie.String(), `["app", "apple", "应用"]`; got != want {
		t.Fatalf("String() = %q, want %q", got, want)
	}
	debug := trie.DebugString()
	t.Log(debug)
	for _, fragment := range []string{"Trie{count: 3", "根 (3)", "'a'", "'应'", "*"} {
		if !strings.Contains(debug, fragment) {
			t.Fatalf("DebugString() = %q, missing %q", debug, fragment)
		}
	}
}

func TestTrieEmptyStringAndBoundaries(t *testing.T) {
	trie := New()
	if trie.StartsWith("") || trie.PrefixCount("") != 0 || trie.Search("") {
		t.Fatal("空 Trie 的空前缀或空单词结果错误")
	}

	trie.Insert("")
	trie.Insert("")
	trie.Insert("a")
	if !trie.Search("") || !trie.StartsWith("") || trie.PrefixCount("") != 2 || trie.Count() != 2 {
		t.Fatal("空字符串处理错误")
	}
	if !trie.Delete("") || trie.Delete("") || trie.Count() != 1 {
		t.Fatal("空字符串删除错误")
	}
	if !trie.Delete("a") || trie.StartsWith("") || trie.PrefixCount("") != 0 {
		t.Fatal("删除最后一个单词后状态错误")
	}
}

func TestTrieDeletePrefixWithoutDeletingChildren(t *testing.T) {
	trie := New()
	for _, word := range []string{"car", "card", "care", "cart"} {
		trie.Insert(word)
	}
	if trie.Delete("ca") {
		t.Fatal("删除不存在的前缀应该失败")
	}
	if !trie.Delete("car") || trie.Search("car") || trie.PrefixCount("car") != 3 {
		t.Fatal("删除同时作为前缀的完整单词时状态错误")
	}
	for _, word := range []string{"card", "care", "cart"} {
		if !trie.Search(word) {
			t.Fatalf("删除前缀单词后 %q 被错误删除", word)
		}
	}
}

func TestTrieUnicodeAndExtremeStrings(t *testing.T) {
	trie := New()
	long := "界" + "中" + "文" + "🙂" + "é"
	words := []string{"", "0", "!", "🙂", "é", "中文", "中文🙂", long}
	for _, word := range words {
		trie.Insert(word)
	}
	for _, word := range words {
		if !trie.Search(word) {
			t.Fatalf("无法找到 Unicode 单词 %q", word)
		}
	}
	if trie.PrefixCount("中文") != 2 || !trie.StartsWith("🙂") || trie.PrefixCount("不存在") != 0 {
		t.Fatal("Unicode 前缀统计错误")
	}
	if !trie.Delete(long) || trie.Search(long) || trie.Count() != len(words)-1 {
		t.Fatal("长 Unicode 字符串删除错误")
	}
}

func TestTrieRepeatedDeleteAndReinsert(t *testing.T) {
	trie := New()
	for cycle := 0; cycle < 100; cycle++ {
		for _, word := range []string{"a", "ab", "abc", "abcd"} {
			trie.Insert(word)
		}
		if trie.Count() != 4 || trie.PrefixCount("a") != 4 {
			t.Fatalf("cycle %d: 插入结果错误", cycle)
		}
		for _, word := range []string{"abcd", "abc", "ab", "a"} {
			if !trie.Delete(word) {
				t.Fatalf("cycle %d: 删除 %q 失败", cycle, word)
			}
		}
		if trie.Count() != 0 || trie.StartsWith("a") || len(trie.Words()) != 0 {
			t.Fatalf("cycle %d: 清空后状态错误", cycle)
		}
	}
}

func TestTrieRandomAgainstMap(t *testing.T) {
	trie := New()
	want := make(map[string]bool)
	words := []string{"a", "ab", "abc", "中", "中文", "🙂", "", "x!", "é"}
	for step := 0; step < 2000; step++ {
		word := words[step%len(words)]
		switch step % 3 {
		case 0:
			trie.Insert(word)
			want[word] = true
		case 1:
			got := trie.Delete(word)
			if got != want[word] {
				t.Fatalf("step %d: Delete(%q) = %v, want %v", step, word, got, want[word])
			}
			delete(want, word)
		default:
			if got := trie.Search(word); got != want[word] {
				t.Fatalf("step %d: Search(%q) = %v, want %v", step, word, got, want[word])
			}
		}
		if trie.Count() != len(want) || len(trie.Words()) != len(want) {
			t.Fatalf("step %d: count 与参考集合不一致", step)
		}
	}
}
