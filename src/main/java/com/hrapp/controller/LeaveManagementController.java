// package com.hrapp.controller;

// import com.hrapp.model.*;
// import com.hrapp.model.LeaveType; // Added import for LeaveType enum
// import com.hrapp.service.LeaveManagementService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.time.LocalDate;
// import java.util.Map;

// @RestController
// @RequestMapping("/api/leave")
// @CrossOrigin(origins = "http://192.168.1.22:3000")
// public class LeaveManagementController {

//     @Autowired
//     private LeaveManagementService leaveManagementService;

//     @GetMapping("/balance/{employeeId}")
//     public ResponseEntity<?> getLeaveBalance(@PathVariable Long employeeId) {
//         try {
//             LeaveBalance balance = leaveManagementService.getLeaveBalance(employeeId);
//             return ResponseEntity.ok(balance);
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//         }
//     }

//     @PostMapping("/mark")
//     public ResponseEntity<?> markLeave(@RequestBody Map<String, Object> request) {
//         try {
//             Long employeeId = Long.parseLong(request.get("employeeId").toString());
//             LeaveType leaveType = LeaveType.valueOf(request.get("leaveType").toString());
//             LocalDate leaveDate = LocalDate.parse(request.get("leaveDate").toString());
//             Boolean isHalfDay = Boolean.parseBoolean(request.get("isHalfDay").toString());
//             String reason = request.get("reason").toString();

//             LeaveTransaction transaction = leaveManagementService.markLeave(
//                 employeeId, leaveType, leaveDate, isHalfDay, reason);
            
//             return ResponseEntity.ok(Map.of(
//                 "message", "Leave marked successfully",
//                 "transaction", transaction
//             ));
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//         }
//     }

    
// }


    // @PostMapping("/mark")
    // public ResponseEntity<?> markLeave(@RequestBody Map<String, Object> request) {
    //     try {
    //         Long employeeId = Long.parseLong(request.get("employeeId").toString());
    //         LeaveType leaveType = LeaveType.valueOf(request.get("leaveType").toString().toUpperCase());
    //         Boolean isHalfDay = Boolean.parseBoolean(request.get("isHalfDay").toString());
    //         String reason = request.get("reason").toString();

    //         // Check for single-day or multi-day leave
    //         LocalDate leaveDate = null;
    //         LocalDate startDate = null;
    //         LocalDate endDate = null;

    //         if (request.containsKey("leaveDate") && request.get("leaveDate") != null) {
    //             leaveDate = LocalDate.parse(request.get("leaveDate").toString());
    //         } else if (request.containsKey("startDate") && request.containsKey("endDate") 
    //                 && request.get("startDate") != null && request.get("endDate") != null) {
    //             startDate = LocalDate.parse(request.get("startDate").toString());
    //             endDate = LocalDate.parse(request.get("endDate").toString());
    //         }

    //         // Check if LOP is applied
    //         int lopCount = request.containsKey("isLop") && Boolean.parseBoolean(request.get("isLop").toString()) 
    //                 ? Integer.parseInt(request.get("noOfLeaves").toString()) : 0;

    //         LeaveTransaction transaction = leaveManagementService.markLeave(
    //             employeeId, leaveType, leaveDate, startDate, endDate, isHalfDay, reason, lopCount
    //         );

    //         return ResponseEntity.ok(Map.of(
    //             "message", "Leave marked successfully",
    //             "transaction", transaction
    //         ));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    //     }
    // }




package com.hrapp.controller;

import com.hrapp.model.LeaveTransaction;
import com.hrapp.model.LeaveType;
import com.hrapp.service.EmployeeService;
import com.hrapp.service.LeaveManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/leave")
// @CrossOrigin(origins = "http://192.168.1.22:3000")
@CrossOrigin(origins = "*")
public class LeaveManagementController {

    @Autowired
    private LeaveManagementService leaveManagementService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/balance/{employeeId}")
    public ResponseEntity<?> getLeaveBalance(@PathVariable Long employeeId) {
        try {
            return ResponseEntity.ok(leaveManagementService.getLeaveBalance(employeeId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/mark")
    public ResponseEntity<?> markLeave(@RequestBody Map<String, Object> request) {
        try {
            String employeeId = (String) request.get("employeeId");
            
            // ✅ Ensure leaveType is valid
            String leaveTypeStr = request.get("leaveType").toString().toUpperCase();
            int no_of_leaves = (int) request.get("noOfLeaves");
            LeaveType leaveType;
            try {
                leaveType = LeaveType.valueOf(leaveTypeStr);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid leave type: " + leaveTypeStr));
            }
    
            Boolean isHalfDay = Boolean.parseBoolean(request.get("isHalfDay").toString());
            String reason = request.get("reason").toString();
    
            LocalDate leaveDate = request.get("leaveDate") != null ? LocalDate.parse(request.get("leaveDate").toString()) : null;
            LocalDate startDate = request.get("startDate") != null ? LocalDate.parse(request.get("startDate").toString()) : null;
            LocalDate endDate = request.get("endDate") != null ? LocalDate.parse(request.get("endDate").toString()) : null;
    
            boolean isLop = leaveType == LeaveType.LOP; // ✅ Now it will work correctly
            
            // ✅ Handle missing `noOfLeaves` value safely
            int lopCount = isLop && request.get("noOfLeaves") != null ? 
                Integer.parseInt(request.get("noOfLeaves").toString()) : 0;
    
            LeaveTransaction transaction = leaveManagementService.markLeave(
                employeeId, leaveType, leaveDate, startDate, endDate,no_of_leaves, isHalfDay, reason, lopCount
            );

            
    
            return ResponseEntity.ok(Map.of(
                "message", "Leave marked successfully",
                "transaction", transaction
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    

}
