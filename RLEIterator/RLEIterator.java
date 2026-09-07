package RLEIterator;

/**
 * <a href="https://leetcode.cn/problems/rle-iterator/">900. RLE 迭代器</a>
 *
 * <p>游程编码数组 encoding 的偶数位 encoding[i] 是「次数」,奇数位 encoding[i+1] 是「值」。
 * 实现 RLEIterator:next(n) 按顺序耗尽序列的后 n 个元素并返回<strong>最后耗尽</strong>的那个;
 * 不够耗时返回 -1。</p>
 *
 * <p>解法:双指针原地消耗。cur 始终指向某对的「次数」位(偶数下标),next(n) 沿着
 * 次数位扣减 n:次数归零则跳到下一对,某次 next 不够扣时整体耗尽前移;直到 n 用完
 * (返回当前对的值)或编码走完(返回 -1)。直接在 encoding 上扣减,省去额外结构。</p>
 *
 * <p>复杂度:每对只被完整跳过一次,总耗散 O(encoding.length + 所有 next 调用数),
 * 单次 next 均摊 O(1);空间 O(1)。</p>
 */
class RLEIterator {

    private final int[]encoding;
    private int cur ; // 当前消耗到的「次数」位下标(恒为偶数)
    public RLEIterator(int[] encoding) {
        this.cur = 0;
        this.encoding = encoding;
    }

    public int next(int n) {
        int res=-1;
        // 每轮循环开头复查 cur 边界:跳过空 run 后必须回到这里,防止越界
        while(cur<encoding.length&&n>0){
            if(encoding[cur]<=0){
                cur+=2; // 空对(次数为 0)直接跳过;是否越界交由 while 条件复检
                continue;
            }
            if(encoding[cur]<n){
                // 当前对的剩余量不足以耗尽 n:整个对用完,继续消耗下一对
                n-=encoding[cur];
                encoding[cur]=0;
                cur+=2;
            }else {
                // 剩余量 >= n:原地扣减,n 归零,最后耗尽的元素就是本对的值
                encoding[cur]-=n;
                n=0;
                res=encoding[cur+1];
            }
        }
        return res; // n 没用完说明序列已耗尽,返回 -1
    }
}


/**
 * Your RLEIterator object will be instantiated and called as such:
 * RLEIterator obj = new RLEIterator(encoding);
 * int param_1 = obj.next(n);
 */