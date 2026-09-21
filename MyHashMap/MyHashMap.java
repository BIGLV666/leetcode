package MyHashMap;

/**
 * 使用分桶加单向链表实现的整数键值哈希表。
 * 题目保证键和值在指定范围内，因此固定 10000 个桶即可满足要求。
 */
class MyHashMap {
    /** 哈希桶数组；同一桶内通过链表解决冲突。 */
    static class Node {
        int key;
        int value;
        Node next;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private final Node[] bucket;

    public MyHashMap() {
        bucket = new Node[10000];
    }

    /** 插入键值；键已存在时覆盖旧值。 */
    public void put(int key, int value) {
        int index = key % bucket.length;
        Node current = bucket[index];
        while (current != null) {
            if (current.key == key) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        Node node = new Node(key, value);
        node.next = bucket[index];
        bucket[index] = node;
    }

    /** 查询键对应的值；键不存在时返回 -1。 */
    public int get(int key) {
        Node current = bucket[key % bucket.length];
        while (current != null) {
            if (current.key == key) {
                return current.value;
            }
            current = current.next;
        }
        return -1;
    }

    /** 删除键及其对应值；键不存在时不执行任何操作。 */
    public void remove(int key) {
        int index = key % bucket.length;
        Node current = bucket[index];
        Node previous = null;
        while (current != null) {
            if (current.key == key) {
                if (previous == null) {
                    bucket[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                return;
            }
            previous = current;
            current = current.next;
        }
    }
}
