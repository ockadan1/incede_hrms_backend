package com.hrapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "leave_balances", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"employee_id", "year"})
})
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "normal_leave_balance", nullable = false)
    private Double normalLeaveBalance;

    @Column(name = "sick_leave_balance", nullable = false)
    private Double sickLeaveBalance;

    @Column(name = "lop_count", nullable = false)
    private Double lopcount;
}
