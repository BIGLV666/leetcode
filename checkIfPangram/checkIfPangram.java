package checkIfPangram;

import java.util.stream.Collectors;

class Solution {
    public boolean checkIfPangram(String word) {
        return word.chars().mapToObj(a -> (char) a).collect(Collectors.toSet()).size() == 26;
    }
}
