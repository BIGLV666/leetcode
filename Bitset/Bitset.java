package Bitset;



class Bitset {

    private final int []arr;
    private int flipCount;
    private int ONE;

    public Bitset(int size) {
        arr=new int[size];
        flipCount=0;
        ONE=size;
    }

    public void fix(int idx) {
        // 逻辑位是 0 才需要改
        if ((arr[idx] ^ flipCount) == 0) {
            ONE--;
        }
        // 把逻辑位设成 1：arr = 1 ^ flipCount
        arr[idx] = 1 ^ flipCount;
    }

    public void unfix(int idx) {
        // 逻辑位是 1 才需要改
        if ((arr[idx] ^ flipCount) == 1) {
            ONE++;
        }
        // 把逻辑位设成 0：arr = flipCount
        arr[idx] = flipCount;
    }

    public void flip(){
        flipCount ^= 1;
        ONE = arr.length - ONE;
    }


    public boolean all() {
        return ONE==0;
    }

    public boolean one() {
        return ONE!=arr.length;
    }

    public int count() {
        return arr.length-ONE;
    }

    public String toString() {

        StringBuilder sb=new StringBuilder();
        for(int num:arr)sb.append((num+flipCount)%2);
        return sb.toString();
    }
}

/**
 * Your Bitset object will be instantiated and called as such:
 * Bitset obj = new Bitset(size);
 * obj.fix(idx);
 * obj.unfix(idx);
 * obj.flip();
 * boolean param_4 = obj.all();
 * boolean param_5 = obj.one();
 * int param_6 = obj.count();
 * String param_7 = obj.toString();
 */
