select l1.num as ConsecutiveNums
from Logs l1
join Logs l2
join Logs l3
where l1.id=l2.id-1 and l2.id= l3.id-2 and l1.num=l2.num and l2.num =l3.num
group by l1.num