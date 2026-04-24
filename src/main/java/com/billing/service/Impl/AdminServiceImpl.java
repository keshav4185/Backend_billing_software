package com.billing.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billing.dto.AdminLoginRequest;
import com.billing.dto.AdminUpdateRequest;
import com.billing.entity.Admin;
import com.billing.repository.AdminRepository;
import com.billing.service.AdminService;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public boolean login(AdminLoginRequest request) {
        return adminRepository.findByUsername(request.getUsername())
                .map(admin -> admin.getPassword().equals(request.getPassword()))
                .orElse(false);
    }

    @Override
    public boolean updateCredentials(AdminUpdateRequest request) {
        return adminRepository.findByUsername(request.getOldUsername())
                .filter(admin -> admin.getPassword().equals(request.getOldPassword()))
                .map(admin -> {
                    admin.setUsername(request.getNewUsername());
                    admin.setPassword(request.getNewPassword());
                    admin.setName(request.getName());
                    admin.setEmail(request.getEmail());
                    admin.setPhone(request.getPhone());
                    admin.setJoinDate(request.getJoinDate());
                    adminRepository.save(admin);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Admin getProfile() {
        List<Admin> admins = adminRepository.findAll();
        if (admins.isEmpty()) {
            return null;
        }
        return admins.get(0); // Return the first admin
    }
}
