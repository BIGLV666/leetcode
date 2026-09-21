package WordDictionary

import "testing"

func TestWordDictionary(t *testing.T) {
	dictionary := Constructor()
	for _, word := range []string{"bad", "dad", "mad"} {
		dictionary.AddWord(word)
	}
	cases := []struct { query string; want bool }{
		{"pad", false}, {"bad", true}, {".ad", true}, {"b..", true}, {"....", false},
	}
	for _, tc := range cases {
		if got := dictionary.Search(tc.query); got != tc.want { t.Fatalf("Search(%q) = %v, want %v", tc.query, got, tc.want) }
	}
}

func TestWordDictionaryUnicodeAndEmpty(t *testing.T) {
	dictionary := Constructor()
	dictionary.AddWord("")
	dictionary.AddWord("你好")
	if !dictionary.Search("") || !dictionary.Search("你.") || dictionary.Search("你") { t.Fatal("unexpected empty/unicode search result") }
}
