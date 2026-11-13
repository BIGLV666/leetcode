package thousandSeparator;
class Solution {
    public String thousandSeparator(int n) {
        StringBuilder sb = new StringBuilder();
        String s = String.valueOf(n);
        int count = 0;
        for (int i = s.length() - 1; i > -1; i--) {
            sb.append(s.charAt(i));
            count++;
            if (count % 3 == 0)
                sb.append(".");
        }
        sb.reverse();
        return sb.charAt(0) == '.' ? sb.substring(1) : sb.toString();
    }
}
