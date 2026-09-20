
select score,DENSE_RANK() over (order by s.score DESC )as `rank`
from Scores s;
