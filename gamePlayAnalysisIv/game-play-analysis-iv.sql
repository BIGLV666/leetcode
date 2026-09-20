select round(
       -- 乘 1.0 把除法变成浮点除法:MySQL 里 / 本就是浮点除,
       -- 但 SQLite 的整数 / 整数 是整除(1/3 = 0),不乘会算出 0.00
       count(DISTINCT b.player_id) * 1.0 / (SELECT COUNT(DISTINCT player_id) FROM Activity),
       2
       )as  fraction
from (
    select player_id,min(event_date) as first
    from  Activity  group by player_id
)f
join Activity b
on b.player_id=f.player_id and b.event_date =f.first +INTERVAL 1 day;



with f as (
    select player_id,event_date,min(event_date)over (PARTITION BY player_id)as first
    from Activity
)
select round(
       count(DISTINCT case when f.event_date=f.first+INTERVAL 1 day then f.player_id end )*1.0/
       count(DISTINCT f.player_id)
       ,2)as fraction
from f;
