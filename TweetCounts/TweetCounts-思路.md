# TweetCounts 思路文档

## 1. 题目要做什么

实现两个操作：

| 操作 | 含义 |
| --- | --- |
| `recordTweet(tweetName, time)` | 记录 `tweetName` 在 `time` 秒发布了一条推文 |
| `getTweetCountsPerFrequency(freq, tweetName, startTime, endTime)` | 把 `[startTime, endTime]` 按 `freq` 切块，返回每个块内该推文的数量 |

频率对应块大小：

| freq | 块大小 `d` |
| --- | --- |
| `minute` | 60 |
| `hour` | 3600 |
| `day` | 86400 |

切块规则：

- 从 `startTime` 开始切，不是从 0 或整点开始；
- 每块是左闭右闭区间 `[s, s + d - 1]`；
- 最后一块可能不足 `d` 秒，但一定以 `endTime` 结束。

例如 `[10, 10000]` 按分钟切：

```text
[10, 69], [70, 129], ..., [9970, 10000]
```

---

## 2. 核心数据结构

```java
Map<String, List<Integer>> tweetTime;
```

- `key`：推文名；
- `value`：该推文的所有发布时间，**保持升序**。

为什么用有序 `List`：

- 同一个推文的发布时间可能有重复；
- 查询时只需要统计某个区间 `[L, R]` 内有多少个时间；
- 有序数组上做两次二分，就能在 `O(log n)` 内得到区间数量。

---

## 3. 记录推文：插入并保持有序

```java
public void recordTweet(String tweetName, int time) {
    var list = tweetTime.computeIfAbsent(tweetName, k -> new ArrayList<>());
    int pos = java.util.Collections.binarySearch(list, time);
    if (pos < 0) {
        pos = -pos - 1;
    }
    list.add(pos, time);
}
```

说明：

1. `computeIfAbsent`：没有该推文就创建列表，有就直接拿；
2. `Collections.binarySearch`：
   - 找到相同值时返回某个匹配下标；
   - 没找到时返回 `-(插入点) - 1`，所以插入点是 `-pos - 1`；
3. `list.add(pos, time)` 把新时间插到正确位置，列表始终有序。

> 注意：如果只是 `list.add(time)` 直接追加，列表不保证有序，后面的二分就会失效。

---

## 4. 切分时间块

```java
private List<Node> chuck(int startTime, int endTime, int d) {
    List<Node> ans = new ArrayList<>();
    while (startTime <= endTime) {
        if (startTime + d <= endTime) {
            ans.add(new Node(startTime, startTime + d - 1));
        } else {
            ans.add(new Node(startTime, endTime));
        }
        startTime += d;
    }
    return ans;
}
```

每一块：

- 完整块：`[startTime, startTime + d - 1]`；
- 最后不足一块：`[startTime, endTime]`。

块的数量：

```text
k = (endTime - startTime) / d + 1
```

因为题目保证 `endTime - startTime <= 10^4`，所以按分钟切最多约 167 块，完全可接受。

> 易错点：这里必须用参数 `d`，不能写死成 `MINUTE`，否则 `hour` 和 `day` 会被按分钟切。

---

## 5. 统计每个块：两个二分

对每个块 `[L, R]`，在有序列表 `times` 中统计落在闭区间内的时间数量。

因为 `times` 升序，满足条件的时间一定是一段连续下标：

```text
[ lowerBound(L), upperBound(R) )
```

所以：

```text
数量 = upperBound(R) - lowerBound(L)
```

### 5.1 `searchStart`：lower bound

找到**第一个 `>= startTime`** 的下标。

```java
private int searchStart(List<Integer> chuck, int startTime) {
    int l = 0;
    int r = chuck.size() - 1;
    int ans = chuck.size();          // 找不到时返回 n，表示区间为空
    while (l <= r) {
        int mid = l + (r - l) / 2;
        int start = chuck.get(mid);
        if (start >= startTime) {
            ans = mid;               // 记录候选，继续往左找更靠前的
            r = mid - 1;
        } else {
            l = mid + 1;
        }
    }
    return ans;
}
```

### 5.2 `searchEnd`：upper bound

找到**第一个 `> endTime`** 的下标。

```java
private int searchEnd(List<Integer> c, int endTime) {
    int l = 0;
    int r = c.size() - 1;
    int ans = c.size();              // 找不到时返回 n
    while (l <= r) {
        int mid = l + (r - l) / 2;
        int end = c.get(mid);
        if (end > endTime) {
            ans = mid;               // 记录候选，继续往左找更靠前的
            r = mid - 1;
        } else {
            l = mid + 1;
        }
    }
    return ans;
}
```

### 5.3 为什么是 `searchEnd - searchStart`

例如 `times = [0, 10, 60]`，查询块 `[0, 59]`：

```text
searchStart(times, 0)  = 0   // 第一个 >= 0 的是下标 0
searchEnd(times, 59)   = 2   // 第一个 > 59 的是下标 2（值为 60）
数量 = 2 - 0 = 2             // 下标 [0, 2) 即 0、10
```

如果写成 `searchStart - searchEnd`，结果会是负数，这是之前的笔误。

---

## 6. 完整查询流程

```java
public List<Integer> getTweetCountsPerFrequency(
        String freq, String tweetName, int startTime, int endTime) {
    return switch (freq) {
        case "minute" -> f(chuck(startTime, endTime, MINUTE), tweetName);
        case "hour"   -> f(chuck(startTime, endTime, HOUR), tweetName);
        case "day"    -> f(chuck(startTime, endTime, DAY), tweetName);
        default       -> null;
    };
}
```

```java
private List<Integer> f(List<Node> chuck, String name) {
    var times = tweetTime.getOrDefault(name, java.util.Collections.emptyList());
    List<Integer> ans = new ArrayList<>();
    for (Node n : chuck) {
        int st = searchStart(times, n.start);
        int end = searchEnd(times, n.end);
        ans.add(end - st);
    }
    return ans;
}
```

如果 `tweetName` 从未出现过，`getOrDefault` 返回空列表：

- `searchStart`、`searchEnd` 都返回 0；
- 每个块的数量都是 0；
- 不会出现空指针。

---

## 7. 示例走查

记录：

```text
tweet3: 0, 10, 60
```

查询 `minute, tweet3, 0, 59`：

```text
块：[0, 59]
searchStart(0)  = 0
searchEnd(59)   = 2
结果：[2]
```

查询 `minute, tweet3, 0, 60`：

```text
块：[0, 59] -> 2 条（0、10）
块：[60, 60] -> 1 条（60）
结果：[2, 1]
```

再记录 `120`，查询 `hour, tweet3, 0, 210`：

```text
块：[0, 210]
4 条（0、10、60、120）
结果：[4]
```

---

## 8. 复杂度

设某个推文的记录数为 `n`，查询切出的块数为 `k`：

| 操作 | 复杂度 |
| --- | --- |
| `recordTweet` | 二分找位置 `O(log n)` + 数组插入搬移 `O(n)` |
| `getTweetCountsPerFrequency` | 切块 `O(k)` + 每块两次二分 `O(k log n)` |
| 空间 | `O(n)` 存所有发布时间，查询时 `O(k)` 存块和结果 |

本题操作数最多 `10^4`，`endTime - startTime <= 10^4`，`k` 最大约 167，因此该方案足够快。

---

## 9. 易错点清单

1. **列表必须有序**：二分前要保证 `recordTweet` 是插入排序，而不是直接追加。
2. **切块用参数 `d`**：不能把 `MINUTE` 写死到 `chuck` 里。
3. **块从 `startTime` 开始**：不是按自然分钟/小时/天对齐。
4. **闭区间**：块是 `[s, e]`，所以 `searchEnd` 用 `> endTime`，不是 `>= endTime`。
5. **计数方向**：是 `searchEnd - searchStart`，不是反过来。
6. **重复时间**：同一秒可以有多条推文，二分天然支持。
7. **不存在的推文名**：要返回全 0 列表，不能 NPE。
8. **最后一块可能不足 `d` 秒**：必须以 `endTime` 结束。
9. **时间范围**：`time <= 10^9`，`int` 足够；`startTime + d` 也不会溢出。
10. **`freq` 非法值**：当前实现返回 `null`，题目保证只会传三种合法值。

---

## 10. 另一种可选方案（了解即可）

可以用 `Map<String, TreeMap<Integer, Integer>>` 存「时间 -> 次数」：

- `recordTweet`：`map.merge(time, 1, Integer::sum)`；
- 查询：用 `subMap(L, true, R, true)` 或 `floorKey/ceilingKey` 统计区间。

优点是插入 `O(log n)`，查询也能用有序结构；缺点是代码更绕，而且本题数据规模下 `ArrayList + 二分` 已经足够，思路也更直观。
