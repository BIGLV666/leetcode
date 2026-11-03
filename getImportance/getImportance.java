package getImportance;

/*
// Definition for Employee.
class Employee {
    public int id;
    public int importance;
    public List<Integer> subordinates;
};
*/

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Solution {

    private Map<Integer, Employee> employeeMap ;

    public int getImportance(List<Employee> employees, int id) {
        employeeMap=new HashMap<>();
        initMap(employees);
        return getImportanceHelper(id);
    }
    private void initMap(List<Employee> employees){
        for(Employee employee:employees){
            employeeMap.put(employee.id,employee);
        }
    }
    private int getImportanceHelper(int id){
        Employee employee=employeeMap.get(id);
        int importance=employee.importance;
        for(int subId:employee.subordinates){
            importance+=getImportanceHelper(subId);
        }
        return importance;
    }
}
class Employee {
    public int id;
    public int importance;
    public List<Integer> subordinates;
};