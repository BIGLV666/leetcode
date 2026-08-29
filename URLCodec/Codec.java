package URLCodec;

/**
 * 标准解题
 */
public class Codec implements AbstractCodec {
    @Override
    public String encode(String longUrl) {
        return longUrl;
    }

    @Override
    public String decode(String shortUrl) {
        return shortUrl;
    }
}
