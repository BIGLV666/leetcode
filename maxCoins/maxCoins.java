package maxCoins;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

class Solution {
    public int maxCoins(int[] piles) {
        Deque<Integer>deque=new ArrayDeque<>();
        Arrays.stream(piles).sorted().forEach(deque::add);
        int ans=0;
        while(!deque.isEmpty()){
            deque.pollLast();
            assert !deque.isEmpty();
            ans+=deque.pollLast();
            deque.pollFirst();
        }
        return ans;
    }
}
