package AuthenticationManager;
import java.util.HashMap;
import java.util.Map;
class AuthenticationManager {
    private int timeToLive;
    private Map<String, Integer> table;
    public AuthenticationManager(int timeToLive) {
        table = new HashMap<>();
        this.timeToLive = timeToLive;
    }

    public void generate(String tokenId, int currentTime) {
        table.put(tokenId, currentTime + this.timeToLive);
    }

    public void renew(String tokenId, int currentTime) {
        int time = table.getOrDefault(tokenId, Integer.MAX_VALUE);
        if (time == Integer.MAX_VALUE)
            return;
        if (time <= currentTime) {
            table.remove(tokenId);
            return;
        }
        table.put(tokenId, currentTime + this.timeToLive);
    }

    public int countUnexpiredTokens(int currentTime) {
        return (int) table.values().stream().filter(a -> a > currentTime).count();
    }
}

/**
 * Your AuthenticationManager object will be instantiated and called as such:
 * AuthenticationManager obj = new AuthenticationManager(timeToLive);
 * obj.generate(tokenId,currentTime);
 * obj.renew(tokenId,currentTime);
 * int param_3 = obj.countUnexpiredTokens(currentTime);
 */
