with f as (
    select name,bonus
    from Employee e left join Bonus b
    on e.empId=b.empId
)
select name,bonus
from f
where f.bonus<1000 or f.bonus IS NULL;



select e.name,b.bonus
from Employee e
left join Bonus b
on e.empId=b.empId where b.bonus<1000 or b.bonus is null ;