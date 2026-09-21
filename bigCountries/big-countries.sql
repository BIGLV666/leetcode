-- 595. 大的国家：面积至少 300 万，或人口至少 2500 万。
SELECT name, population, area
FROM World
WHERE area >= 3000000 OR population >= 25000000;
