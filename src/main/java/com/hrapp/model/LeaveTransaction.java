// package com.hrapp.model;

// import jakarta.persistence.*;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import lombok.AllArgsConstructor;
// import java.time.LocalDate;

// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @Entity
// @Table(name = "leave_transactions")
// public class LeaveTransaction {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @ManyToOne
//     @JoinColumn(name = "employee_id", nullable = false)
//     private Employee employee;

//     @Enumerated(EnumType.STRING)
//     @Column(nullable = false)
//     private LeaveType leaveType;

//     @Column(nullable = false)
//     private LocalDate leaveDate;

//     @Column(nullable = false)
//     private Boolean isHalfDay;

//     @Column(nullable = false)
//     private String reason;

//     @Column(nullable = false)
//     private LocalDate appliedDate;

    
// }



// package com.hrapp.model;

// import jakarta.persistence.*;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import lombok.AllArgsConstructor;
// import java.time.LocalDate;

// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @Entity
// @Table(name = "leave_transactions")
// public class LeaveTransaction {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @ManyToOne
//     @JoinColumn(name = "employee_id", nullable = false)
//     private Employee employee;

//     @Enumerated(EnumType.STRING)
//     @Column(nullable = false)
//     private LeaveType leaveType;

//     @Column(nullable = false)
//     private Boolean isHalfDay;

//     @Column(nullable = false)
//     private String reason;

//     @Column(nullable = false)
//     private LocalDate appliedDate;

//     // New Fields
//     @Column(nullable = false)
//     private Boolean isLop; // To indicate if it's a Loss of Pay leave

//     @Column(nullable = false)
//     private Integer lopCount; // Number of LOP leaves applied

//     // Modified Fields for Multi-Day Leave Support
//     private LocalDate leaveDate;  // For single-day leave
//     private LocalDate startDate;  // For multi-day leave
//     private LocalDate endDate;    // For multi-day leave
// }


package com.hrapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "leave_transactions")
public class LeaveTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType leaveType;

    @Column(nullable = false)
    private Boolean isHalfDay = false; // Default to false

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private LocalDate appliedDate;

    @Column
    private LocalDate leaveDate;  // For single-day leave

    @Column(nullable = true)
    private LocalDate startDate;
    
    @Column(nullable = true)
    private LocalDate endDate;
    


  // For multi-day leave

    @Column(nullable = false)
    private Integer lopCount = 0; // Default to 0

    @Transient  // This is now derived in the database
    private Boolean isLop; // No need to manually store it, calculated as `lopCount > 0`
}
