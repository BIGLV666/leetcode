-- 1378. 使用唯一标识码替换员工ID
-- https://leetcode.cn/problems/replace-employee-id-with-the-unique-identifier/
--
-- 列出每位员工的唯一标识码;没有对应标识码的员工,unique_id 显示为 NULL。
--
-- 要点:
--   1. 以 Employees 为左表做 LEFT JOIN,保证每位员工都出现,匹配不到时补 NULL;
--   2. 输出列顺序按题目要求是 unique_id、name;
--   3. 行顺序不限。
select EmployeeUNI.unique_id, Employees.name
from Employees
left join EmployeeUNI
    on Employees.id = EmployeeUNI.id
