package com.billing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.billing.dto.AdminLoginRequest;
import com.billing.dto.AdminUpdateRequest;
import com.billing.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginRequest request) {

        if (adminService.login(request)) {
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.status(401).body("Invalid username or password");
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody AdminUpdateRequest request) {

        if (adminService.updateCredentials(request)) {
            return ResponseEntity.ok("Admin credentials updated successfully");
        }
        return ResponseEntity.status(400).body("Old credentials are incorrect");
    }
}
