package countAndSay;

class Solution {
    public String countAndSay(int n) {
        if(n==1){return "1";}
        if(n==2){return "11";}
        String s=countAndSay(n-1);
        StringBuilder sb=new StringBuilder();
        int count=1;
        for(int i=0;i<s.length();i++){
            if(i+1<s.length()&&s.charAt(i)==s.charAt(i+1)){
                count++;
            }else {
                sb.append(count).append(s.charAt(i));
                count=1;
            }
        }
        return sb.toString();
    }

}
