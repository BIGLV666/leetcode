package MyCircularQueue;

/**
 * LeetCode 622. 设计循环队列(Medium)
 *
 * <p>用定长数组实现:head 指向下一个可写入位置,tail 指向队首,size 记录元素个数。
 * 入队时写 head 再让 head 环形前进;出队让 tail 环形前进。Front 取 arr[tail],
 * Rear 取 arr[(head-1+len)%len]。用 size 而不是 head==tail 区分空/满,绕开
 * 「假满」歧义(少用一个槽位的经典陷阱)。</p>
 *
 * <p>所有操作 O(1),额外空间 O(k)。</p>
 */
class MyCircularQueue {


    private final int[]arr;
    private  int head;
    private  int tail;
    private   int size;

    public MyCircularQueue(int k) {
        arr = new int[k];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }



    public boolean enQueue(int value) {
        if(isFull())return false;
        arr[head]=value;
        head=(head+1+arr.length)%arr.length;
        size++;
        return true;
    }

    public boolean deQueue() {
        if(isEmpty())return false;
        this.tail=(tail+1+arr.length)%arr.length;
        size--;
        return true;
    }

    public int Front() {
        if(isEmpty())return -1;
        return arr[tail];
    }

    public int Rear() {
        if(isEmpty())return -1;
        return arr[(head-1+arr.length)%arr.length];
    }

    public boolean isEmpty() {
        return size==0;
    }

    public boolean isFull() {
        return size==arr.length;
    }
}

/**
 * Your MyCircularQueue object will be instantiated and called as such:
 * MyCircularQueue obj = new MyCircularQueue(k);
 * boolean param_1 = obj.enQueue(value);
 * boolean param_2 = obj.deQueue();
 * int param_3 = obj.Front();
 * int param_4 = obj.Rear();
 * boolean param_5 = obj.isEmpty();
 * boolean param_6 = obj.isFull();
 */
