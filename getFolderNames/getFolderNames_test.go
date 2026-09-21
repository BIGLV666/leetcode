package getFolderNames

import (
    "reflect"
    "testing"
)

func TestGetFolderNames(t *testing.T) {
    cases := []struct { input, want []string }{
        {[]string{"gta", "gta(1)", "gta", "avalon"}, []string{"gta", "gta(1)", "gta(2)", "avalon"}},
        {[]string{"onepiece", "onepiece(1)", "onepiece(2)", "onepiece(1)"}, []string{"onepiece", "onepiece(1)", "onepiece(2)", "onepiece(1)(1)"}},
    }
    for _, tc := range cases { if got := getFolderNames(tc.input); !reflect.DeepEqual(got, tc.want) { t.Fatalf("got %v, want %v", got, tc.want) } }
}
