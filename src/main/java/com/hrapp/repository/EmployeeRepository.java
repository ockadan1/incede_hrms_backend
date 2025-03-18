package com.hrapp.repository;

import com.hrapp.model.Employee;



import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByEmployeeId(String employeeId);


}
