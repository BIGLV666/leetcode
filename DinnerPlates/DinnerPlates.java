package DinnerPlates;

import java.util.*;

/** 维护多个固定容量栈，并支持最左可用栈与最右非空栈操作。 */
class DinnerPlates {

    private final int capacity;
    private final List<Deque<Integer>> queue;
    private final TreeSet<Integer>indexSet;
    private final TreeSet<Integer>havingSet;
    public DinnerPlates(int capacity) {
        this.capacity = capacity;
        indexSet = new TreeSet<>();
        queue = new ArrayList<>();
        havingSet = new TreeSet<>();
    }

    /** 将元素压入下标最小且未满的栈；没有可用栈时新建栈。 */
    public void push(int val) {
        if(indexSet.isEmpty()){
            queue.add(new ArrayDeque<>(capacity));
            queue.getLast().push(val);
            if(queue.getLast().size()<capacity)indexSet.add(queue.size()-1);
            havingSet.add(queue.size()-1);
            return;
        }
        Integer index=indexSet.first();
        havingSet.add(index);
        queue.get(index).push(val);
        if(queue.get(index).size()>=capacity){
            indexSet.remove(index);
        }
    }

    /** 从下标最大的非空栈弹出元素；所有栈为空时返回 -1。 */
    public int pop() {
        if(havingSet.isEmpty()){
            return -1;
        }
        int index=havingSet.last();
        int val=queue.get(index).pop();
        indexSet.add(index);
        if(queue.get(index).isEmpty()){
            havingSet.remove(index);
        }
        return val;
    }

    /** 从指定栈弹出栈顶；下标越界或该栈为空时返回 -1。 */
    public int popAtStack(int index) {
        if(index<0 || index>=queue.size()){
            return -1;
        }
        Deque<Integer> stack = queue.get(index);
        if(stack.isEmpty()){
            return -1;
        }

        indexSet.add(index);
        int val= stack.pop();
        if(stack.isEmpty()){
            havingSet.remove(index);
            return val;
        }
        havingSet.add(index);
        return val;
    }
}

/**
 * Your DinnerPlates object will be instantiated and called as such:
 * DinnerPlates obj = new DinnerPlates(capacity);
 * obj.push(val);
 * int param_2 = obj.pop();
 * int param_3 = obj.popAtStack(index);
 */
