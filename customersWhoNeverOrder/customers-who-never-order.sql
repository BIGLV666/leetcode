select Customers.name as Customers
from Customers
where Customers.id not in(
    select customerid from orders
);