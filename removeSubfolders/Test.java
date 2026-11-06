package removeSubfolders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/** removeSubfolders 的无框架测试:官方示例 + 前缀非子目录边界 + 随机目录树对拍 + 大规模。 */
public class Test {
    public static void main(String[] args) {
        Solution solution = new Solution();

        equal(solution.removeSubfolders(new String[]{"/a", "/a/b", "/c/d", "/c/d/e", "/c/f"}),
                Set.of("/a", "/c/d", "/c/f"), "官方示例1");
        equal(solution.removeSubfolders(new String[]{"/a", "/a/b/c", "/a/b/d"}),
                Set.of("/a"), "官方示例2");
        equal(solution.removeSubfolders(new String[]{"/a/b/c", "/a/b/ca", "/a/b/d"}),
                Set.of("/a/b/c", "/a/b/ca", "/a/b/d"), "官方示例3");
        equal(solution.removeSubfolders(new String[]{"/a"}), Set.of("/a"), "单目录");
        equal(solution.removeSubfolders(new String[]{"/a/b", "/a/bc", "/a/b/d"}),
                Set.of("/a/b", "/a/bc"), "前缀相同但不是子目录");
        equal(solution.removeSubfolders(new String[]{"/a", "/a/b", "/a/b/c", "/a/b/c/d"}),
                Set.of("/a"), "四层嵌套");

        Random random = new Random(1233);
        for (int round = 0; round < 3000; round++) {
            int count = 1 + random.nextInt(20);
            Set<String> folders = new HashSet<>();
            while (folders.size() < count) {
                int depth = 1 + random.nextInt(4);
                StringBuilder sb = new StringBuilder();
                for (int d = 0; d < depth; d++) {
                    sb.append('/').append((char) ('a' + random.nextInt(3)));
                }
                folders.add(sb.toString());
            }
            String[] input = folders.toArray(new String[0]);
            Set<String> expected = reference(input);

            List<String> returned = solution.removeSubfolders(input);
            Set<String> actual = new HashSet<>(returned);
            if (actual.size() != returned.size()) {
                throw new AssertionError("round=" + round + ": 结果中有重复路径 " + returned);
            }
            if (!actual.equals(expected)) {
                throw new AssertionError("round=" + round + ": input=" + folders
                        + " expected=" + expected + " actual=" + actual);
            }
        }

        // 大规模:40000 个目录都是 "/a" 的子目录
        String[] large = new String[40000];
        large[0] = "/a";
        for (int i = 1; i < large.length; i++) {
            large[i] = "/a/b" + i;
        }
        equal(solution.removeSubfolders(large), Set.of("/a"), "40000 个子目录");

        System.out.println("All removeSubfolders tests passed.");
    }

    /** 参考实现:对每个路径检查它的某个前缀(到某个 '/' 为止)是否也在集合中。 */
    private static Set<String> reference(String[] folder) {
        Set<String> all = new HashSet<>(Arrays.asList(folder));
        Set<String> result = new HashSet<>();
        for (String path : folder) {
            boolean subfolder = false;
            for (int i = 1; i < path.length(); i++) {
                if (path.charAt(i) == '/' && all.contains(path.substring(0, i))) {
                    subfolder = true;
                    break;
                }
            }
            if (!subfolder) {
                result.add(path);
            }
        }
        return result;
    }

    private static void equal(List<String> actual, Set<String> expected, String name) {
        Set<String> actualSet = new HashSet<>(actual);
        if (actualSet.size() != actual.size() || !actualSet.equals(expected)) {
            throw new AssertionError(name + ": expected " + expected + ", got " + actual);
        }
    }
}
