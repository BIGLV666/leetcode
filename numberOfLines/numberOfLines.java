package numberOfLines;

class Solution {
    public int[] numberOfLines(int[] widths, String s) {
        int count=0;
        int lines=1;
        for(char c:s.toCharArray()){
            if(count+widths[c-'a'] > 100){
                lines++;
                count=0;
            }
            count+=widths[c-'a'];
        }
        return new int[]{lines, count};
    }
}