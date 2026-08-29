package removeDuplicates;

/**
 * PrettyDeque 是 ArrayDeque 的一个小子类，重写了 toString()，
 * 以更可读的方式输出 deque 中的元素（按从头到尾顺序，使用逗号分隔）。
 *
 * 该类放在 removeDuplicates 包中以便与现有代码直接互用。
 *
 * @param <E> 元素类型
 */
public class PrettyDeque<E> extends java.util.ArrayDeque<E> {
    public PrettyDeque() { super(); }

    /**
     * 返回类似 "[a,b,c]" 的字符串表示，null 元素将显示为 "null"。 
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (E e : this) {
            if (!first) sb.append(",");
            first = false;
            sb.append(e == null ? "null" : e.toString());
        }
        sb.append("]");
        return sb.toString();
    }
}
