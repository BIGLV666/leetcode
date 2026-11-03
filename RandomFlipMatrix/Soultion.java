package RandomFlipMatrix;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

class Solution {

    private final Set<List<Integer>> flipped;
    private int m;
    private int n;
    public Solution(int m, int n) {
        this.m = m;
        this.n = n;
        flipped = new java.util.HashSet<>();
    }

    public int[] flip() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int row, col;
        row=random.nextInt(m);
        col=random.nextInt(n);
        while(flipped.contains(List.of(row,col))){
            row=random.nextInt(m);
            col=random.nextInt(n);
        }
        flipped.add(List.of(row, col));
        return new int[]{row, col};
    }

    public void reset() {
        flipped.clear();
    }
}
