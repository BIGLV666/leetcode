-- 196. 删除重复的电子邮箱
-- https://leetcode.cn/problems/delete-duplicate-emails/
--
-- 删除 Person 中 email 重复的行,只保留每组 email 里 id 最小的那一行。
--
-- 写法说明:这里用「保留每组 email 的最小 id」的可移植写法,MySQL 与 SQLite 均可执行。
-- MySQL 对同一张表先 DELETE 再用子查询会报 ERROR 1093,所以要再包一层派生表。
-- 另一种常见写法是 MySQL 专用的多表 DELETE(无法在 sqlite3 里验证):
--   delete p1 from Person p1, Person p2 where p1.email = p2.email and p1.id > p2.id;
delete from Person
where id not in (
    select id from (
        select min(id) as id from Person group by email
    ) keep
);
