package toGoatLatin;

import java.lang.Character;
import java.util.HashSet;
import java.util.Set;
class Solution {
    public String toGoatLatin(String sentence) {
        Set<Character> set = new HashSet<Character>() {
            {
                add('a');
                add('e');
                add('i');
                add('o');
                add('u');
                add('A');
                add('E');
                add('I');
                add('O');
                add('U');
            }
        };
        String[] str = sentence.split(" ");
        for (int i = 0; i < str.length; i++) {
            String s = str[i];
            StringBuilder sb = new StringBuilder();
            if (set.contains(s.charAt(0))) {
                sb.append(s).append("ma");
                for (int j = 0; j < i + 1; j++) sb.append("a");
            } else {
                sb.append(s.substring(1));
                sb.append(s.charAt(0)).append("ma");
                for (int j = 0; j < i + 1; j++) sb.append("a");
            }
            str[i] = sb.toString();
        }
        return String.join(" ", str);
    }
}
