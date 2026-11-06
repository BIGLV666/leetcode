package SORTracker;

import java.util.ArrayList;
import java.util.List;

class SORTracker {
    private int index=0;
    static class Node implements Comparable<Node>  {
        String name;
        int score;

        Node(String name, int score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public int compareTo(Node o) {
            if (this.score != o.score) {
                return Integer.compare(o.score, this.score); // 分数高的在前
            }
            return this.name.compareTo(o.name);              // 同分名字小的在前
        }
    }
    private final List<Node>score;

    public SORTracker() {
        score=new ArrayList<>();
    }

    public void add(String name, int score) {
        Node node=new Node(name,score);
        int pos= java.util.Collections.binarySearch(this.score,node);
        if (pos < 0) {
            pos = -pos - 1;
        }
        this.score.add(pos,node);
    }

    public String get() {
        return this.score.get(index++).name;
    }
}

/**
 * Your SORTracker object will be instantiated and called as such:
 * SORTracker obj = new SORTracker();
 * obj.add(name,score);
 * String param_2 = obj.get();
 */
