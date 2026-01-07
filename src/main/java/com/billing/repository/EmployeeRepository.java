package com.billing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.billing.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	boolean existsByEmail(String email);
	Optional<Employee> findByEmpIdAndEmailAndPassword(
		    String empId,
		    String email,
		    String password
		);
}
