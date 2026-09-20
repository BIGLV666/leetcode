-- ============================================================================
-- 550. 游戏玩法分析 IV (Game Play Analysis IV)  —— 多解法合集
-- https://leetcode.cn/problems/game-play-analysis-iv/
-- ============================================================================
--
-- 【题意】
-- Activity 表记录玩家的登录(游戏)记录,主键 (player_id, event_date)。
-- 求:首次登录的"第二天也登录了"的玩家数 / 玩家总数,保留 2 位小数。
--
-- 【样例数据】(即 LC 官方示例)
-- +-----------+-----------+------------+--------------+
-- | player_id | device_id | event_date | games_played |
-- +-----------+-----------+------------+--------------+
-- | 1         | 2         | 2016-03-01 | 5            |   ← 玩家 1 首次
-- | 1         | 2         | 2016-03-02 | 6            |   ← 次日又登录 ✓
-- | 2         | 3         | 2017-06-25 | 1            |   ← 只有一天
-- | 3         | 1         | 2016-03-02 | 0            |   ← 首次
-- | 3         | 4         | 2018-07-03 | 5            |   ← 隔了两年,不是次日
-- +-----------+-----------+------------+--------------+
-- 答案:1/3 = 0.33
--
-- 【核心思路】(所有解法都围绕同一个判断)
--   ① 找出每个玩家的"首次登录日" first_date
--   ② 判断该玩家是否存在一行 event_date = first_date + 1 天
--   ③ 统计满足 ② 的玩家数,除以总玩家数,ROUND 2 位
--
-- 本文件给出 5 种写法,结果都是 0.33,按"可读性/性能/兼容性"各有取舍:
--   方法① MIN + 自连接            —— 最直观,无需窗口函数
--   方法② 窗口函数 MIN() OVER      —— 推荐,一次扫描
--   方法③ 窗口函数 ROW_NUMBER+LEAD —— 展示"看下一行"的偏移函数
--   方法④ ROW_NUMBER + 自连接      —— 排名函数的经典用法
--   方法⑤ EXISTS 相关子查询        —— 换一种存在性判断
--
-- 说明:以下均为 MySQL 8 语法(LeetCode 判题环境)。
--      窗口函数需要 MySQL 8.0+ / SQLite 3.25+ / PostgreSQL 8.4+。
-- ============================================================================


-- ============================================================================
-- 方法①:MIN + 自连接(最直观,兼容 MySQL 5.7)
-- ============================================================================
-- 思路:先把"每个玩家的首次登录日"算成一张小表 f,再拿它去 Activity 里找
--       是否存在 f.first_date 的次日那一行。找得到 → 该玩家算数。
SELECT ROUND(
           -- 分子:符合条件的玩家数。COUNT(DISTINCT) 防止一个玩家多行被重复计数
           COUNT(DISTINCT b.player_id) * 1.0
           -- 分母:总玩家数(必须 DISTINCT,因为一个玩家有多条登录记录)
           / (SELECT COUNT(DISTINCT player_id) FROM Activity),
           2                       -- 保留 2 位小数
       ) AS fraction
FROM (
    -- 子查询 f:每个玩家的首次登录日(派生表,必须起别名)
    SELECT player_id, MIN(event_date) AS first_date
    FROM Activity
    GROUP BY player_id
) f
-- 自连接:同一个人,且存在"首次登录日 + 1 天"的记录
JOIN Activity b
  ON b.player_id = f.player_id
 AND b.event_date = DATE_ADD(f.first_date, INTERVAL 1 DAY);   -- 也可写 f.first_date + INTERVAL 1 DAY

-- 【为什么分子用 JOIN 而不是 LEFT JOIN】
--   JOIN(内连接)只保留"匹配上"的行,天然就是"次日也登录了"的玩家;
--   若用 LEFT JOIN,没匹配上的玩家 second_date 为 NULL,需要额外加
--   WHERE b.event_date IS NOT NULL,否则分母分子都会算错。


-- ============================================================================
-- 方法②:窗口函数 MIN() OVER(推荐:一次扫描,不用自连接)
-- ============================================================================
-- 关键区别:MIN(event_date) OVER (PARTITION BY player_id) 会为**每一行**
--           附上"该玩家所有行里的最早日期",但**不会把行折叠掉**。
--           (对比 GROUP BY 会把每个玩家压成一行)
WITH first_day AS (
    SELECT
        player_id,
        event_date,
        -- 窗口函数:按 player_id 分组,取组内 event_date 的最小值,写回每一行
        MIN(event_date) OVER (PARTITION BY player_id) AS first_date
    FROM Activity
)
SELECT ROUND(
           -- 分子:登录日恰好等于"首次登录日 + 1 天"的玩家数
           COUNT(DISTINCT CASE WHEN event_date = DATE_ADD(first_date, INTERVAL 1 DAY)
                               THEN player_id END) * 1.0
           -- 分母:总玩家数。可以直接用 COUNT(DISTINCT player_id),
           --       因为 first_day 保留了全部原始行(每行都有 player_id),不会漏人
           / COUNT(DISTINCT player_id),
           2
       ) AS fraction
FROM first_day;

-- 【CASE WHEN ... THEN player_id END 的技巧】
--   不写 ELSE 时,不满足条件的行返回 NULL;
--   而 COUNT(列) 会**忽略 NULL**,所以 COUNT 出来的正好是"满足条件的玩家数"。
--   等价写法:SUM(CASE WHEN ... THEN 1 ELSE 0 END)


-- ============================================================================
-- 方法③:窗口函数 ROW_NUMBER + LEAD(展示"偏移函数")
-- ============================================================================
-- ROW_NUMBER():组内按 event_date 排序后编号 1,2,3...
-- LEAD(event_date):取"排序后下一行"的 event_date(NULL 表示没有下一行)
-- 于是"首次登录的次日也登录了" ⟺ rn = 1 且 next_date = event_date + 1 天
WITH ranked AS (
    SELECT
        player_id,
        event_date,
        ROW_NUMBER() OVER (PARTITION BY player_id ORDER BY event_date) AS rn,
        LEAD(event_date) OVER (PARTITION BY player_id ORDER BY event_date) AS next_date
    FROM Activity
)
SELECT ROUND(
           COUNT(DISTINCT CASE WHEN rn = 1
                                AND next_date = DATE_ADD(event_date, INTERVAL 1 DAY)
                               THEN player_id END) * 1.0
           / (SELECT COUNT(DISTINCT player_id) FROM Activity),
           2
       ) AS fraction
FROM ranked;

-- 【注意】这里的分母必须从 Activity 里重新数,不能从 ranked 里数 ——
--   虽然 ranked 行数没变,但为了语义清晰(防止未来加 WHERE 改变行数)建议单独取。
--   也可以写成 / COUNT(DISTINCT player_id),因为 ranked 保留了全部行。


-- ============================================================================
-- 方法④:ROW_NUMBER + 自连接(排名函数的经典用法)
-- ============================================================================
-- 把 Activity 编号两次(a 取第 1 行 = 首次登录,b 取第 2 行 = 第二次登录),
-- 然后比较两次登录日期是否相差正好 1 天。
SELECT ROUND(
           COUNT(DISTINCT CASE WHEN b.event_date = DATE_ADD(a.event_date, INTERVAL 1 DAY)
                               THEN a.player_id END) * 1.0
           / (SELECT COUNT(DISTINCT player_id) FROM Activity),
           2
       ) AS fraction
FROM (SELECT player_id, event_date,
             ROW_NUMBER() OVER (PARTITION BY player_id ORDER BY event_date) AS rn
      FROM Activity) a
-- LEFT JOIN:只登录过一次的玩家(没有第 2 行)也要保留在结果里,
--           它们的 b.event_date 为 NULL,CASE 判断自然不成立
LEFT JOIN (SELECT player_id, event_date,
                  ROW_NUMBER() OVER (PARTITION BY player_id ORDER BY event_date) AS rn
           FROM Activity) b
  ON a.player_id = b.player_id
 AND b.rn = 2
WHERE a.rn = 1;          -- 只看每个玩家的第一行


-- ============================================================================
-- 方法⑤:EXISTS 相关子查询(存在性判断)
-- ============================================================================
-- 对每一行判断:如果它是该玩家的首次登录,且存在"次日"记录,则该玩家算数。
SELECT ROUND(
           COUNT(DISTINCT CASE WHEN EXISTS (
                    SELECT 1 FROM Activity x
                    WHERE x.player_id = a.player_id
                      AND x.event_date = DATE_ADD(a.event_date, INTERVAL 1 DAY)
                ) THEN a.player_id END) * 1.0
           / (SELECT COUNT(DISTINCT player_id) FROM Activity),
           2
       ) AS fraction
FROM Activity a
-- 只保留"首次登录"那一行,避免同一玩家被重复判断(虽然 COUNT DISTINCT 也能兜住)
WHERE a.event_date = (
    SELECT MIN(event_date) FROM Activity y WHERE y.player_id = a.player_id
);

-- 【性能提示】相关子查询对外层每一行都要执行一次,大表上明显慢于方法②。
--            能用窗口函数时优先用窗口函数。


-- ============================================================================
-- 【附】一步到位的写法(仅供对照,可读性较差)
-- ============================================================================
-- 直接把"次日也登录"的条件写成 JOIN 条件,配合 GROUP BY 求首次登录日:
SELECT ROUND(
           COUNT(DISTINCT a.player_id) * 1.0
           / (SELECT COUNT(DISTINCT player_id) FROM Activity),
           2
       ) AS fraction
FROM Activity a
JOIN Activity b
  ON a.player_id = b.player_id
 AND b.event_date = DATE_ADD(a.event_date, INTERVAL 1 DAY)
-- 用 (player_id, event_date) 行值元组比较,保证 a 那一行确实是"首次登录"
WHERE (a.player_id, a.event_date) IN (
    SELECT player_id, MIN(event_date) FROM Activity GROUP BY player_id
);
