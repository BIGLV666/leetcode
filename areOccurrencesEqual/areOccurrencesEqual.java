package areOccurrencesEqual;
import java.lang.Character;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
class Solution {
    public boolean areOccurrencesEqual(String s) {
        Map<Character, Integer> map = new HashMap<>();
        s.chars().mapToObj(a -> (char) a).forEach(a -> map.put(a, map.getOrDefault(a, 0) + 1));
        return map.values().stream().collect(Collectors.toSet()).size() == 1;
    }
}
