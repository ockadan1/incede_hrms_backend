package com.hrapp.service;

import com.hrapp.model.Employee;
import com.hrapp.model.LeaveBalance;
import com.hrapp.model.LeaveTransaction;
import com.hrapp.model.LeaveType;
import com.hrapp.repository.EmployeeRepository;
import com.hrapp.repository.LeaveBalanceRepository;
import com.hrapp.repository.LeaveTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class LeaveManagementService {
    
    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;
    
    @Autowired
    private LeaveTransactionRepository leaveTransactionRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public LeaveBalance initializeLeaveBalance(Employee employee) {
        int currentYear = LocalDate.now().getYear();
        Optional<LeaveBalance> existingBalance = leaveBalanceRepository.findByEmployeeAndYear(employee, currentYear);
        
        if (existingBalance.isPresent()) {
            // Update existing balance if joining date has changed
            LeaveBalance balance = existingBalance.get();
            double normalLeaves = employee.getLeaves();
            balance.setNormalLeaveBalance(normalLeaves);
            return leaveBalanceRepository.save(balance);
        }

        LeaveBalance leaveBalance = new LeaveBalance();
        leaveBalance.setEmployee(employee);
        leaveBalance.setYear(currentYear);
        leaveBalance.setLopcount(0.0);
        
        // Calculate prorated normal leaves
        double normalLeaves = calculateProratedNormalLeaves(employee.getJoiningDate());
        leaveBalance.setNormalLeaveBalance(normalLeaves);
        
        // Set fixed sick leaves
        leaveBalance.setSickLeaveBalance(5.0);
        
        return leaveBalanceRepository.save(leaveBalance);
    }

    private double calculateProratedNormalLeaves(LocalDate joiningDate) {
        if (joiningDate == null) return 12.0;
    
        LocalDate currentDate = LocalDate.now();
        
        // Calculate number of months passed including the joining month
        int monthsPassed = (currentDate.getYear() - joiningDate.getYear()) * 12 +
                            currentDate.getMonthValue() - joiningDate.getMonthValue() + 1;
    
        // Ensure the minimum value is 0 (no negative leaves)
        return Math.max(monthsPassed, 0);
    }
    
    // @Transactional
    // public LeaveTransaction markLeave(Long employeeId, LeaveType leaveType, 
    //                                 LocalDate leaveDate, Boolean isHalfDay, String reason) {
    //     Employee employee = employeeRepository.findById(employeeId)
    //         .orElseThrow(() -> new RuntimeException("Employee not found"));

    //     LeaveBalance leaveBalance = leaveBalanceRepository
    //         .findByEmployeeAndYear(employee, leaveDate.getYear())
    //         .orElseGet(() -> initializeLeaveBalance(employee));

    //     // Check leave balance
    //     double deduction = isHalfDay ? 0.5 : 1.0;
    //     double currentBalance = leaveType.equals(LeaveType.NORMAL) 
    //         ? leaveBalance.getNormalLeaveBalance() 
    //         : leaveBalance.getSickLeaveBalance();

    //     if (currentBalance < deduction) {
    //         throw new RuntimeException("Insufficient leave balance");
    //     }

    //     // Update leave balance
    //     if (leaveType.equals(LeaveType.NORMAL)) {
    //         leaveBalance.setNormalLeaveBalance(currentBalance - deduction);
    //     } else {
    //         leaveBalance.setSickLeaveBalance(currentBalance - deduction);
    //     }
    //     leaveBalanceRepository.save(leaveBalance);

    //     // Create leave transaction
    //     LeaveTransaction leaveTransaction = new LeaveTransaction();
    //     leaveTransaction.setEmployee(employee);
    //     leaveTransaction.setLeaveType(leaveType);
    //     leaveTransaction.setLeaveDate(leaveDate);
    //     leaveTransaction.setIsHalfDay(isHalfDay);
    //     leaveTransaction.setReason(reason);
    //     leaveTransaction.setAppliedDate(LocalDate.now());

    //     return leaveTransactionRepository.save(leaveTransaction);
    // }

    @Transactional
    public LeaveTransaction markLeave(String employeeId, LeaveType leaveType, LocalDate leaveDate,
    LocalDate startDate, LocalDate endDate, int no_of_leaves, boolean isHalfDay, 
    String reason, int lopCount) {
    Employee employee =  employeeRepository.findByEmployeeId(employeeId);
        // .orElseThrow(() -> new RuntimeException("Employee not found"));
    

    LeaveTransaction leaveTransaction = new LeaveTransaction();
    leaveTransaction.setEmployee(employee);
    leaveTransaction.setLeaveType(leaveType);
    leaveTransaction.setReason(reason);
    leaveTransaction.setAppliedDate(LocalDate.now());
    leaveTransaction.setIsHalfDay(isHalfDay);
    leaveTransaction.setLopCount(lopCount);

    if (leaveDate != null) {
    leaveTransaction.setLeaveDate(leaveDate);
    } else {
    leaveTransaction.setStartDate(startDate);
    leaveTransaction.setEndDate(endDate);
    }
    LeaveTransaction savedTransaction = leaveTransactionRepository.save(leaveTransaction);

    if (savedTransaction != null) {
        updateLeaveBalance(employee, leaveType, no_of_leaves, isHalfDay, lopCount);
    }


    return savedTransaction;
    }

    public LeaveBalance getLeaveBalance(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        return leaveBalanceRepository
            .findByEmployeeAndYear(employee, LocalDate.now().getYear())
            .orElseGet(() -> initializeLeaveBalance(employee));
    }



    private void updateLeaveBalance(Employee employee, LeaveType leaveType, int noOfLeaves, boolean isHalfDay, int lopCount) {
        LeaveBalance leaveBalance = leaveBalanceRepository
            .findByEmployeeAndYear(employee, LocalDate.now().getYear())
            .orElseThrow(() -> new RuntimeException("Leave balance not found"));

  
        if(leaveType == LeaveType.SICK){ 
            System.out.println("Sick printing");
            if(isHalfDay){
                leaveBalance.setSickLeaveBalance(leaveBalance.getSickLeaveBalance()-0.5);
                System.out.println(leaveBalance.getSickLeaveBalance() +"leave balance");
            }else{
                System.out.println(leaveBalance.getSickLeaveBalance() +"leave balance");
                leaveBalance.setSickLeaveBalance(leaveBalance.getSickLeaveBalance()-noOfLeaves);
            }
        }


        
        if (leaveType == LeaveType.ANNUAL) {
            System.out.println("Annual printing");
            if(isHalfDay){
                leaveBalance.setNormalLeaveBalance(leaveBalance.getSickLeaveBalance()-0.5);
                System.out.println(leaveBalance.getSickLeaveBalance() +"leave balance");
            }else{
                System.out.println(leaveBalance.getSickLeaveBalance() +"leave balance");
                leaveBalance.setNormalLeaveBalance(leaveBalance.getSickLeaveBalance()-noOfLeaves);
            }

        }

        if (leaveType == LeaveType.LOP) {
            if(isHalfDay){
                leaveBalance.setLopcount(leaveBalance.getLopcount()+0.5);
            }else{
                leaveBalance.setLopcount(leaveBalance.getLopcount()+noOfLeaves);
            }

        } 

        leaveBalanceRepository.save(leaveBalance);
        employeeRepository.save(employee);
    }
    
}
