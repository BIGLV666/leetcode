package maxDiff;

import java.util.Locale;

class Solution {
    public int maxDiff(int num) {
        String s = String.valueOf(num);
        int max = num;
        int min = num;
       for(char ch : s.toCharArray()){
           if(ch != '9') {
               max = Integer.parseInt(s.replace(ch, '9'));
               break;
           }
       }
       s=String.valueOf(num);
        if (s.charAt(0) != '1') {
            min = Integer.parseInt(s.replace(s.charAt(0), '1'));
        } else {
            for (int i = 1; i < s.length(); i++) {
                if (s.charAt(i) != '0' && s.charAt(i) != s.charAt(0)) {
                    min = Integer.parseInt(s.replace(s.charAt(i), '0'));
                    break;
                }
            }
        }
        return max - min;

    }
}
