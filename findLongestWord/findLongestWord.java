package findLongestWord;

import java.util.List;

class Solution {
    public String findLongestWord(String s, List<String> dictionary) {
        String ans="";
        for(String word : dictionary) {
            if(check(s,word)){
                if(ans.isEmpty()){
                    ans=word;
                }
                if(ans.length()<word.length()){
                    ans=word;
                } else if (ans.length()==word.length()&&ans.compareTo(word)>0) {
                    ans=word;
                }
            }
        }
        return ans;
    }


    private boolean check(String s1, String s2) {
        if(s2.length()>s1.length())return false;
        if(s1.length()==s2.length())return s1.equals(s2);
        int index=0;
        for(int i=0;i<s1.length();i++){
            if(index>=s2.length())return true;
            if(s1.charAt(i)!=s2.charAt(index))continue;
            index++;
        }
        return index==s2.length();

    }
}
