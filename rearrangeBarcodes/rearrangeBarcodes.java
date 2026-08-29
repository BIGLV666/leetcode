package rearrangeBarcodes;

import java.util.ArrayList;
import java.util.List;

/**
 * 1054. 距离相等的条形码。
 *
 * <p>算法先统计每个值的出现次数，再将不同值按频率降序排列并展开成一维序列，最后依次填充
 * 结果数组的偶数下标和奇数下标。相同值在展开序列中连续，而偶数位置优先提供了最大的间隔；
 * 对于题目保证有解的输入，任一值的频率不超过 {@code (n + 1) / 2}，因此不会在相邻位置
 * 放入相同值。</p>
 *
 * <p>时间复杂度：{@code O(n + V + u log u)}；空间复杂度：{@code O(n + V + u)}。
 * 其中 {@code V = 10000}，{@code u} 为不同条形码值的数量。</p>
 *
 * @see <a href="https://leetcode.cn/problems/distant-barcodes/description/">题目链接</a>
 */
class Solution {
    private static final int MAX_VALUE = 10000;

    public int[] rearrangeBarcodes(int[] barcodes) {
        int n = barcodes.length;
        int[] counts = new int[MAX_VALUE + 1];
        for (int barcode : barcodes) {
            counts[barcode]++;
        }

        List<Integer> values = new ArrayList<>();
        for (int value = 1; value <= MAX_VALUE; value++) {
            if (counts[value] > 0) {
                values.add(value);
            }
        }
        values.sort((left, right) -> Integer.compare(counts[right], counts[left]));

        // 按频率降序展开，使高频值优先占据间隔更大的位置。
        int[] flattened = new int[n];
        int flattenedIndex = 0;
        for (int value : values) {
            for (int occurrence = 0; occurrence < counts[value]; occurrence++) {
                flattened[flattenedIndex++] = value;
            }
        }

        int[] result = new int[n];
        int sourceIndex = 0;
        // 先填偶数下标，再填奇数下标，以分隔出现次数最多的值。
        for (int start = 0; start < 2; start++) {
            for (int index = start; index < n; index += 2) {
                result[index] = flattened[sourceIndex++];
            }
        }
        return result;
    }
}
