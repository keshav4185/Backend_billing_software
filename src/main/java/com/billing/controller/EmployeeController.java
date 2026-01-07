package com.billing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.billing.dto.EmployeeRequest;
import com.billing.dto.LoginRequest;
import com.billing.entity.Employee;
import com.billing.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return service.getAll();
    }

    @PostMapping
    public Employee addEmployee(@RequestBody EmployeeRequest request) {
        return service.addEmployee(request);
    }

    @PutMapping("/{id}")
    public Employee update(
            @PathVariable Long id,
            @RequestBody EmployeeRequest request) {
        return service.updateEmployee(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        service.delete(id);
    }

    // ✅ LOGIN API (THIS FIXES 405 ERROR)
    @PostMapping("/login")
    public Employee login(@RequestBody LoginRequest request) {
        return service.login(request);
    }

}
