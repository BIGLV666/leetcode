package MyHashMap;

public class MyHashMapTest {
    public static void main(String[] args) {
        MyHashMap map = new MyHashMap();
        assert map.get(1) == -1;
        map.put(1, 1);
        assert map.get(1) == 1;
        map.put(1, 10);
        assert map.get(1) == 10;
        map.put(10001, 2); // 与 key=1 冲突，验证链表处理
        assert map.get(10001) == 2;
        map.remove(1);
        assert map.get(1) == -1;
        assert map.get(10001) == 2;
        map.remove(999);
    }
}
