-- 1148. 文章浏览 I
-- https://leetcode.cn/problems/article-views-i/
--
-- 找出所有浏览过自己文章的作者,返回其 id,按 id 升序。
--
-- 要点:
--   1. author_id = viewer_id 即「自己看了自己的文章」;
--   2. 同一作者可能有多条这样的浏览记录,用 GROUP BY 去重;
--   3. 题目要求按 id 升序,而 GROUP BY 并不保证输出顺序,必须显式 ORDER BY。
select author_id as id
from Views
where author_id = viewer_id
group by author_id
order by id asc
