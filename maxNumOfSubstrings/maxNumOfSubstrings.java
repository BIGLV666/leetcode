package maxNumOfSubstrings;

import java.util.*;

/**
 * 1520. 最多的不重叠子字符串(Hard)
 * https://leetcode.cn/problems/maximum-number-of-non-overlapping-substrings/
 *
 * 找出最多个数的不重叠子串,满足:
 *   ① 任意两个子串不重叠;
 *   ② 子串里出现某个字符 c,就必须包含 c 在 s 中的全部出现;
 * 个数相同时,要求所有子串的总长度最小(该最优解唯一)。
 *
 * 三段式解法:
 *   ① 统计每个字符第一次/最后一次出现的位置,得到初始区间 [first,last];
 *   ② 求每个字符的"闭包区间"——包含它全部出现、且区间内每个字符的全部出现也都在区间内的最小闭区间
 *      (DFS 递归 + while 扫到稳定,见 expand);
 *   ③ 闭包区间之间只会"相含"或"相离"(成层叠族),其中不含其它闭包区间的"最小"区间两两不相交,
 *      它们就是"个数最多 + 总长最短"的答案;按长度从小到大贪心取不重叠即可。
 *
 * 复杂度:① O(n);② 每个字符的区间最多扩张 26 次、每次扫描 O(n),上界 O(26^2 * n),实际远小于此;③ O(26 log 26)。
 * 空间:O(n) 存 char 数组 + O(26) 区间。
 */
class Solution {
    private char[] chars;                                  // s 的字符数组(递归中反复用)
    private Map<Character,Node> map;                       // 字符 -> 区间信息
    private Set<Character> done = new HashSet<>();         // 闭包已算完的字符
    private Set<Character> visiting = new HashSet<>();     // 正在计算闭包的字符(防互相引用死循环)

    public List<String> maxNumOfSubstrings(String s) {
        Map<Character,Node>map=getNodes(s);                 // ① + ② 建表并求闭包
        Queue<Node>queue=getQueue(map);                     // ③ 按区间长度从小到大

        List<String> ans=new ArrayList<>();
        boolean[]old=new boolean[s.length()];               // 已被占用的位置
        while(!queue.isEmpty()){
            Node node=queue.poll();
            boolean []newArray=check(node,old);
            if(newArray==old)ans.add(s.substring(node.start,node.end+1));  // 不冲突 → 选它
        }
        return ans;

    }

    /** 尝试占用 node 的区间:与已占用位置冲突则返回副本(调用方判定 newArray!=old → 放弃该区间) */
    private boolean[] check(Node node,boolean[] old){
        boolean[]newArray=Arrays.copyOf(old,old.length);
        int left=node.start;
        int right=node.end;
        for(int i=left;i<=right;i++){
            if(old[i]){
                return newArray;                            // 有冲突:返回副本(与 old 不同引用)
            }
            else old[i]=true;                               // 占用
        }
        return old;                                         // 成功:返回 old 本体
    }

    private Map<Character,Node>getNodes(String s){

        chars = s.toCharArray();
        map = new HashMap<>();
        for(int i=0;i<chars.length;i++){                    // ① 统计每个字符的 first/last
            if(!map.containsKey(chars[i])){
                map.put(chars[i],new Node(chars[i],1,i,i));
            }else {
                Node node = map.get(chars[i]);
                node.count++;
                node.end=i;                                 // end 不断右移,最后即为最后一次出现
            }
        }

        // ② 用 DFS 求每个字符的闭包区间(替换原来"收集 set + 单遍展开"的写法)
        done.clear();
        visiting.clear();
        for(Character c:map.keySet()){
            expand(c);
        }

        return map;
    }

    /**
     * 求字符 c 的闭包区间并写回它的 node,返回 {start,end}。
     *
     * 闭包 = 包含 c 的全部出现、且"区间内每个字符的全部出现也都在区间内"的最小闭区间。
     * 做法:从 [first,last] 出发,把区间内出现的每个字符 d 的闭包【递归】并进来;
     * 并入后区间会变大、又可能带进新字符,所以外面套 while 反复扫,直到区间不再变化。
     */
    private int[] expand(char c){
        Node node=map.get(c);
        // done:已算完;visiting:正在算(如 "abab" 中 a、b 互相落在对方区间内),
        // 此时先返回当前区间,区间只增不减,外层 while 会继续收敛到正确值
        if(done.contains(c)||visiting.contains(c)){
            return new int[]{node.start,node.end};
        }
        visiting.add(c);
        int l=node.start;      // 初始区间:该字符第一次出现、最后一次出现
        int r=node.end;
        while(true){
            int nl=l;
            int nr=r;
            // 扫描当前区间:里面出现的每个字符 d,都要把它自己的闭包并进来
            for(int i=l;i<=r;i++){
                char d=chars[i];
                if(d==c){          // 跳过自己,否则无限递归
                    continue;
                }
                int[]sub=expand(d);        // ★ 递归:先算出 d 的闭包
                nl=Math.min(nl,sub[0]);
                nr=Math.max(nr,sub[1]);
            }
            if(nl==l&&nr==r){      // 区间不再变化 → 闭包稳定,结束
                break;
            }
            l=nl;                  // 区间变大了:可能带进新字符,回到 while 重新扫
            r=nr;
            node.start=l;          // 中途写回,方便递归中的兄弟/父分支复用
            node.end=r;
        }
        visiting.remove(c);
        done.add(c);
        node.start=l;
        node.end=r;
        return new int[]{l,r};
    }

    /** 优先队列:按区间长度从小到大(短区间 = 更"内层"的闭包,优先选它才能个数最多、总长最短) */
    private PriorityQueue<Node> getQueue(Map<Character,Node> map){
        PriorityQueue<Node> queue=new PriorityQueue<>((a,b)->(a.end-a.start)-(b.end-b.start));
        for(Map.Entry<Character,Node> entry:map.entrySet()){
            queue.add(entry.getValue());
        }
        return queue;
    }

}
class Node{
    char s;
    int count;
    int start;
    int end;
    Set<Character> set;
    public Node(char s, int count, int start, int end) {
        this.s = s;
        this.count = count;
        this.start = start;
        this.end = end;
    }
    public void add(char s){
        if(this.set==null){
            set=new HashSet<>();
        }
        set.add(s);
    }
}
