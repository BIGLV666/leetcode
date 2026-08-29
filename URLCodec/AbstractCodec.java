package URLCodec;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 简单哈希
 */
public interface AbstractCodec {

    // Encodes a URL to a shortened URL.
     String encode(String longUrl);

    // Decodes a shortened URL to its original URL.
    String decode(String shortUrl) ;
}

// Your Codec object will be instantiated and called as such:
// Codec codec = new Codec();
// codec.decode(codec.encode(url));