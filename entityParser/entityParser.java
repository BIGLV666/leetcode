package entityParser;

/** 将题目规定的 HTML 实体替换为对应字符。 */
class Solution {
    /** 按实体定义解码文本，未识别的实体保持原样。 */
    public String entityParser(String text) {
        return text.replaceAll("&quot;", "\"")
                .replaceAll("&apos;","'")
                .replaceAll("&gt;",">")
                .replaceAll("&lt;","<")
                .replaceAll("&frasl;","/")
                .replaceAll("&amp;","&");

    }
}
