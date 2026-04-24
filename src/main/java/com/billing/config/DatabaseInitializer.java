package com.billing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("DEBUG: Running Database Self-Healer...");
        try {
            jdbcTemplate.execute("ALTER TABLE admin ADD COLUMN IF NOT EXISTS name VARCHAR(255)");
            jdbcTemplate.execute("ALTER TABLE admin ADD COLUMN IF NOT EXISTS email VARCHAR(255)");
            jdbcTemplate.execute("ALTER TABLE admin ADD COLUMN IF NOT EXISTS phone VARCHAR(255)");
            jdbcTemplate.execute("ALTER TABLE admin ADD COLUMN IF NOT EXISTS join_date VARCHAR(255)");
            System.out.println("DEBUG: Database columns verified/added successfully!");
        } catch (Exception e) {
            System.err.println("DEBUG: Database healer encountered an issue (columns might already exist): " + e.getMessage());
        }
    }
}
