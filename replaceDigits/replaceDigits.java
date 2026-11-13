package replaceDigits;
class Solution {
    public String replaceDigits(String s) {
        StringBuilder ans = new StringBuilder();
        for (char ch : s.toCharArray()) {
            if (Character.isDigit(ch)) {
                ans.append((char) (ans.charAt(ans.length() - 1) + (ch - '0')));
            } else {
                ans.append(ch);
            }
        }
        return ans.toString();
    }
}
