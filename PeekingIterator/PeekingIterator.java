package PeekingIterator;

import java.util.Iterator;

/**
 * <a href="https://leetcode.cn/problems/peeking-iterator/">284. 窥探迭代器</a>
 *
 * <p>在普通迭代器之上增加 peek():返回下一个元素但<b>不推进</b>迭代器。</p>
 *
 * <p>解法:提前缓存一个元素。</p>
 * <ul>
 *   <li>构造时就把底层迭代器的第一个元素取出来存进 peekedElement;</li>
 *   <li>peek() 直接返回缓存;</li>
 *   <li>next() 返回缓存,并立刻从底层再取一个补上缓存。</li>
 * </ul>
 *
 * <p>易错点:hasNext() 判断的是「缓存是否为空」,而不是底层迭代器是否还有元素——
 * 元素一旦被提前取出,底层其实已经"消耗"掉它了。</p>
 *
 * <p>该实现假定元素不为 null(LeetCode 的迭代器不会给出 null)。</p>
 *
 * <p>复杂度:peek / next / hasNext 均为 O(1),额外空间 O(1)。</p>
 */
class PeekingIterator implements Iterator<Integer> {
    private final Iterator<Integer> iterator;
    private Integer peekedElement;   // 已从底层取出、但还没交给调用方的元素

    public PeekingIterator(Iterator<Integer> iterator) {
        this.iterator = iterator;
        this.peekedElement = iterator.hasNext() ? iterator.next() : null;
    }

    /** 返回下一个元素但不推进迭代器。 */
    public Integer peek() {
        return peekedElement;
    }

    @Override
    public Integer next() {
        Integer result = peekedElement;                                // 交出缓存
        peekedElement = iterator.hasNext() ? iterator.next() : null;   // 立刻补上新的缓存
        return result;
    }

    @Override
    public boolean hasNext() {
        return peekedElement != null;
    }
}
