package minOperations1598;

class Solution {
    public int minOperations(String[] logs) {
        int count=0;
        for(String s:logs){
            switch (s){
                case "../" -> count=Math.max(--count,0);
                case "./" -> {}
                default -> count++;
            }
        }
        return count;
    }
}
