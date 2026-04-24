package com.billing.service;

import com.billing.dto.AdminLoginRequest;
import com.billing.dto.AdminUpdateRequest;
import com.billing.entity.Admin;

public interface AdminService {

    boolean login(AdminLoginRequest request);

    boolean updateCredentials(AdminUpdateRequest request);

    Admin getProfile();
}
