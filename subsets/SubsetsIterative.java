package subsets;

import java.util.ArrayList;
import java.util.List;

/**
 * 78. 子集 —— 不用回溯的增量构造版(双层循环)。
 *
 * <p>核心:从空集 {@code [[]]} 出发,每来一个新元素 x,就把「当前已有的每个子集」
 * 复制一份、追加 x,作为新增子集。处理完 i 个元素后恰好拥有 2^i 个子集。</p>
 *
 * <p>外层循环遍历元素,内层循环遍历"当前结果快照"——注意内层要以进入时的
 * {@code size} 为界(不能用 {@code res.size()}),否则新追加的子集会被再次遍历,
 * 造成死循环或重复叠加。</p>
 *
 * <p>复杂度:时间 O(n · 2^n),空间 O(n · 2^n)(输出即该量级,额外 O(1))。</p>
 */
public class SubsetsIterative {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        res.add(new ArrayList<>()); // 空集是幂集的一员,也是增量构造的种子
        for (int x : nums) {                 // 外层:逐个引入元素
            int size = res.size();           // 快照!只扩展"不含 x 的旧子集"
            for (int i = 0; i < size; i++) {
                List<Integer> next = new ArrayList<>(res.get(i)); // 复制旧子集
                next.add(x);                 // 追加 x → 新子集
                res.add(next);
            }
        }
        return res;
    }
}