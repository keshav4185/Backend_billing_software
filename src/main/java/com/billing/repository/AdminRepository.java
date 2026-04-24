package com.billing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billing.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // Added comment to trigger redeploy and schema update
    Optional<Admin> findByUsername(String username);
}
