package TweetCounts;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TweetCounts {
    static class Node{
        int start;
        int end;
        Node(int start,int end){
            this.start=start;
            this.end=end;
        }
    }

    private final int MINUTE=60;
    private final int HOUR=3600;
    private final int DAY=86400;

    private final Map<String,List<Integer>>tweetTime;

    public TweetCounts() {
        tweetTime=new HashMap<>();
    }

    public void recordTweet(String tweetName, int time) {
        var list = tweetTime.computeIfAbsent(tweetName, k -> new ArrayList<>());
        int pos = java.util.Collections.binarySearch(list, time);
        if (pos < 0) {
            pos = -pos - 1;
        }
        list.add(pos, time);
    }

    public List<Integer> getTweetCountsPerFrequency(String freq, String tweetName, int startTime, int endTime) {
        return switch (freq){
            case "minute"-> f(chuck(startTime,endTime,MINUTE),tweetName);
            case "hour"->f(chuck(startTime,endTime,HOUR),tweetName);
            case "day"->f(chuck(startTime,endTime,DAY),tweetName);
            default -> null;
        };
    }

    private List<Node> chuck(int startTime, int endTime, int d) {

        List<Node> ans = new ArrayList<>();
        while (startTime <= endTime) {
            if (startTime + d <= endTime) {
                ans.add(new Node(startTime, startTime + d - 1));
            } else {
                ans.add(new Node(startTime, endTime));
            }
            startTime += d;
        }
        return ans;
    }



    private List<Integer>f(List<Node>chuck,String name){
        var times=tweetTime.getOrDefault(name, java.util.Collections.emptyList());
        List<Integer>ans=new ArrayList<>();
        for(Node n:chuck){
            int st=searchStart(times,n.start);
            int end=searchEnd(times,n.end);
            ans.add(end-st);
        }
        return ans;

    }

    // 第一个 >= startTime 的下标（lower bound）
    private int searchStart(List<Integer>chuck,int startTime){
        int l=0;
        int r=chuck.size()-1;
        int ans=chuck.size();
        while(l<=r){
            int mid=l+(r-l)/2;
            int start=chuck.get(mid);
            if(start>=startTime){
                ans=mid;
                r=mid-1;
            }else {
                l=mid+1;
            }
        }
        return ans;
    }

    // 第一个 > endTime 的下标（upper bound）
    private int searchEnd(List<Integer>c,int endTime){
        int l=0;
        int r=c.size()-1;
        int ans=c.size();
        while(l<=r){
            int mid=l+(r-l)/2;
            int end=c.get(mid);
            if(end>endTime){
                ans=mid;
                r=mid-1;
            }else {
                l=mid+1;
            }
        }
        return ans;
    }
}

/**
 * Your TweetCounts object will be instantiated and called as such:
 * TweetCounts obj = new TweetCounts();
 * obj.recordTweet(tweetName,time);
 * List<Integer> param_2 = obj.getTweetCountsPerFrequency(freq,tweetName,startTime,endTime);
 */
