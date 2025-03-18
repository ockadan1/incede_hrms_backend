package com.hrapp.repository;

import com.hrapp.model.LeaveBalance;
import com.hrapp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    Optional<LeaveBalance> findByEmployeeAndYear(Employee employee, int year);
}
