package URLCodec;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基于内存映射的短链接生成系统。
 *
 * <p>短链接由固定域名、题目 slug 和唯一 id 组成，例如：
 * {@code https://leetcode.com/problems/design-tinyurl?id=1}。
 * </p>
 */
public class UrlCodec implements AbstractCodec {

    // 从 LeetCode 链接中提取题目 slug，例如 design-tinyurl。
    private static final Pattern LONG_URL_PATTERN =
           Pattern.compile("https?://[^/]+/problems/([^/?#]+)(?:[/?#].*)?$");

    // 短链接的 id 可以出现在查询参数的任意位置。
    private static final Pattern SHORT_URL_PATTERN =
           Pattern.compile("(?:^|[?&])id=([0-9A-Za-z]+)(?:&|$)");

    // 62 个不重复字符用于将数字 ID 转换成短字符串。
    private static final String CHARS =
           "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // 两张表分别支持“长链接查短码”和“短码查长链接”。
    private final Map<String, String> longUrl = new HashMap<>();
    private final Map<String, String> shortUrl = new HashMap<>();
    private long nextId = 1;

    /** 将自增数字转换为 Base62 字符串。 */
    private String formId(long id) {
       StringBuilder result = new StringBuilder();
       do {
           result.append(CHARS.charAt((int) (id % CHARS.length())));
           id /= CHARS.length();
       } while (id > 0);
       return result.reverse().toString();
    }

    /** 为新链接分配一个不会重复的短码。 */
    private String createShortCode() {
       String code;
       do {
           code = formId(nextId++);
       } while (shortUrl.containsKey(code));
       return code;
    }

    /** 从原链接中取出题目 slug；非 LeetCode 链接使用通用路径。 */
    private String extractSlug(String url) {
       Matcher matcher = LONG_URL_PATTERN.matcher(url);
       return matcher.find() ? matcher.group(1) : "url";
    }

    /** 生成带有题目路径和唯一 id 的短链接。 */
    @Override
    public synchronized String encode(String longUrl) {
       if (longUrl == null || longUrl.isEmpty()) {
           throw new IllegalArgumentException("longUrl cannot be null or empty");
       }

       // 已经编码过的链接直接复用旧短码，避免重复创建记录。
       String oldCode = this.longUrl.get(longUrl);
       if (oldCode != null) {
           return "https://leetcode.com/problems/" + extractSlug(longUrl) + "?id=" + oldCode;
       }

       String code = createShortCode();
       this.longUrl.put(longUrl, code);
       this.shortUrl.put(code, longUrl);
       return "https://leetcode.com/problems/" + extractSlug(longUrl) + "?id=" + code;
    }

    /** 根据短链接中的 id 找回原链接；未知短链接原样返回。 */
    @Override
    public synchronized String decode(String shortUrl) {
       if (shortUrl == null || shortUrl.isEmpty()) {
           throw new IllegalArgumentException("shortUrl cannot be null or empty");
       }
       Matcher matcher = SHORT_URL_PATTERN.matcher(shortUrl);
       if (matcher.find()) {
           String originalUrl = this.shortUrl.get(matcher.group(1));
           return originalUrl == null ? shortUrl : originalUrl;
       }
       return this.shortUrl.getOrDefault(shortUrl, shortUrl);
    }
}
