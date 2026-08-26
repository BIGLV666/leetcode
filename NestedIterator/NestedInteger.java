
import java.util.List;

public interface NestedInteger {
    // 这个 NestedInteger 是否保存单个整数，而不是嵌套列表
    public boolean isInteger();

    // 如果保存单个整数，返回这个整数
    // 如果是嵌套列表则返回 null
    public Integer getInteger();

    // 如果保存嵌套列表，返回这个列表
    // 如果是单个整数则返回空列表
    public List<NestedInteger> getList();
}
