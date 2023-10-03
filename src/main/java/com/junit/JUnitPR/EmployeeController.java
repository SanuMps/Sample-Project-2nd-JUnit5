package com.junit.JUnitPR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController  {


    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/")
    public Employee createEmployee(@RequestBody Employee employee) {
        
            return employeeService.createEmployee(employee);
        
    }

    @GetMapping("/")
    public List<Employee> getAllEmployees() {
      
            return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
       
            return employeeService.getEmployeeById(id);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        
            return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
      
            employeeService.deleteEmployee(id);
    }

    @DeleteMapping("/")
    public void deleteAllEmployees() {
       
            employeeService.deleteAllEmployees();
    }
}
