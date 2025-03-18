package com.hrapp.service;

import com.hrapp.repository.EmployeeRepository;
import com.hrapp.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class LeaveService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Scheduled(cron = "0 0 1 * * ?") // Runs at 1 AM on the first day of every month
    public void updateLeaveBalances() {
        List<Employee> employees = employeeRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for (Employee employee : employees) {
            LocalDate joiningDate = employee.getJoiningDate();
            int monthsSinceJoining = (int) Period.between(joiningDate, currentDate).toTotalMonths();

            // Calculate total leaves: 1 initial leave + 1 leave per month
            int totalLeaves = Math.min(monthsSinceJoining + 1, 12); // Cap at 12 leaves
            // employee.setAvailableLeaves(totalLeaves);
            employeeRepository.save(employee);
        }
    }

    // Additional methods for leave applications can be added here
}
