package com.junit.JUnitPR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        try {
            return employeeRepository.findAll();
        } catch (Exception e) {
            logger.error("Error getting all employees: {}", e.getMessage(), e);
            throw e; 
        }
    }

    public Employee getEmployeeById(Long id) {
        try {
            return employeeRepository.findById(id).orElse(null);
        } catch (Exception e) {
            logger.error("Error getting employee with ID {}: {}", id, e.getMessage(), e);
            throw e; 
        }
    }

    public Employee createEmployee(Employee employee) {
        try {
            logger.info("Creating Employee: {}",employee);
            return employeeRepository.save(employee);
        } catch (Exception e) {
            logger.error("Error creating employee: {}", e.getMessage(), e);
            throw e; 
        }
    }

    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        try {
            Employee existingEmployee = employeeRepository.findById(id).orElse(null);
            if (existingEmployee != null) {
                existingEmployee.setFirstName(updatedEmployee.getFirstName());
                existingEmployee.setLastName(updatedEmployee.getLastName());
                existingEmployee.setAge(updatedEmployee.getAge());
                return employeeRepository.save(existingEmployee);
            }
            return null;
        } catch (Exception e) {
            logger.error("Error updating employee with ID {}: {}", id, e.getMessage(), e);
            throw e; 
        }
    }

    public void deleteEmployee(Long id) {
        try {
            employeeRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting employee with ID {}: {}", id, e.getMessage(), e);
            throw e; 
        }
    }

    public void deleteAllEmployees() {
        try {
            employeeRepository.deleteAll();
        } catch (Exception e) {
            logger.error("Error deleting all employees: {}", e.getMessage(), e);
            throw e;
        }
    }
}
