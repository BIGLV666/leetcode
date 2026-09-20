select ROUND(sum(tiv_2016),2) as tiv_2016
from Insurance i1
where EXISTS(
    select 1 from Insurance i2
             where i2.tiv_2015=i1.tiv_2015
                                 and i1.pid!=i2.pid
)
and not EXISTS(
    select 1 from Insurance i3
             where i1.lat=i3.lat and i1.lon=i3.lon and i1.pid!=i3.pid
);
