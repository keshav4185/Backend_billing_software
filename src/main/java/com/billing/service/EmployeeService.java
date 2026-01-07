package com.billing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billing.dto.EmployeeRequest;
import com.billing.dto.LoginRequest;
import com.billing.entity.Employee;
import com.billing.repository.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;

    public Employee addEmployee(EmployeeRequest request) {
        if (repo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Employee emp = new Employee();
        emp.setEmpId(request.getEmpId());
        emp.setName(request.getName());
        emp.setPhone(request.getPhone());
        emp.setEmail(request.getEmail());
        emp.setPassword(request.getPassword());
        emp.setStatus("Active");

        return repo.save(emp);
    }

    public List<Employee> getAll() {
        return repo.findAll();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ UPDATE
    public Employee updateEmployee(Long id, EmployeeRequest request) {
        Employee emp = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        emp.setName(request.getName());
        emp.setPhone(request.getPhone());
        emp.setEmail(request.getEmail());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            emp.setPassword(request.getPassword());
        }

        return repo.save(emp);
    }

    // ✅ LOGIN METHOD (IMPORTANT)
    public Employee login(LoginRequest request) {

        return repo.findByEmpIdAndEmailAndPassword(
                request.getEmpId(),
                request.getEmail(),
                request.getPassword()
        ).orElseThrow(() -> new RuntimeException("Invalid Credentials"));
    }

	
}
