package com.junit.JUnitPR;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@PropertySource("classpath:application-test.properties")
public class EmployeeTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("Akki");
        employee.setLastName("Khush");
        employee.setAge(30);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Employee createdEmployee = objectMapper.readValue(result.getResponse().getContentAsString(), Employee.class);
        assertThat(createdEmployee).isNotNull();
        assertThat(createdEmployee.getFirstName()).isEqualTo("Akki");
        assertThat(createdEmployee.getLastName()).isEqualTo("Khush");
        assertThat(createdEmployee.getAge()).isEqualTo(30);
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        // Create some test employees using the service
        Employee employee1 = new Employee();
        employee1.setFirstName("Alice");
        employee1.setLastName("Johnson");
        employee1.setAge(25);
        employeeService.createEmployee(employee1);

        Employee employee2 = new Employee();
        employee2.setFirstName("Bob");
        employee2.setLastName("Smith");
        employee2.setAge(35);
        employeeService.createEmployee(employee2);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Employee> employees = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        assertThat(employees).isNotNull();
        assertThat(employees).hasSizeGreaterThan(1);
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        // Create a test employee using the service
        Employee employee = new Employee();
        employee.setFirstName("Eve");
        employee.setLastName("Johnson");
        employee.setAge(28);
        Employee createdEmployee = employeeService.createEmployee(employee);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", createdEmployee.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Employee retrievedEmployee = objectMapper.readValue(result.getResponse().getContentAsString(), Employee.class);
        assertThat(retrievedEmployee).isNotNull();
        assertThat(retrievedEmployee.getId()).isEqualTo(createdEmployee.getId());
        assertThat(retrievedEmployee.getFirstName()).isEqualTo("Eve");
        assertThat(retrievedEmployee.getLastName()).isEqualTo("Johnson");
        assertThat(retrievedEmployee.getAge()).isEqualTo(28);
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        // Create a test employee using the service
        Employee employee = new Employee();
        employee.setFirstName("Sanu");
        employee.setLastName("Singh");
        employee.setAge(32);
        Employee createdEmployee = employeeService.createEmployee(employee);

        // Update the employee's details
        createdEmployee.setFirstName("Sanu");
        createdEmployee.setLastName("Kumar");
        createdEmployee.setAge(33);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", createdEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdEmployee)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Employee updatedEmployee = objectMapper.readValue(result.getResponse().getContentAsString(), Employee.class);
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getId()).isEqualTo(createdEmployee.getId());
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Sanu");
        assertThat(updatedEmployee.getLastName()).isEqualTo("Kumar");
        assertThat(updatedEmployee.getAge()).isEqualTo(33);
    }

    @Test
    public void testDeleteEmployee() throws Exception {
    // Create a test employee using the service
    Employee employee = new Employee();
    employee.setFirstName("Ankit");
    employee.setLastName("Raj");
    employee.setAge(40);
    Employee createdEmployee = employeeService.createEmployee(employee);

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", createdEmployee.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk());

    // Verify that the response content is empty 
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", createdEmployee.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    String content = result.getResponse().getContentAsString();
    assertThat(content).isEmpty(); // Expecting an empty response body
}

    @Test
    public void testDeleteAllEmployees() throws Exception {
        // Create some test employees using the service
        Employee employee1 = new Employee();
        employee1.setFirstName("Ritwik");
        employee1.setLastName("George");
        employee1.setAge(45);
        employeeService.createEmployee(employee1);

        Employee employee2 = new Employee();
        employee2.setFirstName("Farhan");
        employee2.setLastName("Hasan");
        employee2.setAge(28);
        employeeService.createEmployee(employee2);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that all employees have been deleted
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Employee> employees = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        assertThat(employees).isEmpty();
    }
}
