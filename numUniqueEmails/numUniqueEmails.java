package numUniqueEmails;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * LeetCode 929. 独特的电子邮件地址(Easy)
 *
 * <p>用正则规范化:先删掉本地名(@ 之前)里的所有 '.',再把 '+' 及其后、'@' 之前的
 * 后缀整段替换为 '@';最后用 Set 去重计数。</p>
 *
 * <p>时间复杂度 O(总字符数),空间 O(去重后的地址数)。</p>
 */
class Solution {
    public int numUniqueEmails(String[] emails) {
        return Arrays.stream(emails).map(email -> email.replaceAll("\\.(?=.*@)", "").replaceAll("\\+.*@", "@")).collect(Collectors.toSet()).size();
    }
}
