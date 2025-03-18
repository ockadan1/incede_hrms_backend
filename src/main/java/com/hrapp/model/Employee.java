package com.hrapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String employeeId;
    private String fullName;
    private String designation;
    private String department;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joiningDate;
    
    private String officialEmail;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    
    private String contactNo;
    private String personalEmail;
    
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
    
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate updatedAt;
    
    private int leaves; // General leaves
    private int sickLeaves; // Sick leaves


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }

    public void setLeaves(int leaves) {
        this.leaves = leaves;
    }

    public void setSickLeaves(int sickLeaves) {
        this.sickLeaves = sickLeaves;
    }



    public int getLeaves() {
        return leaves;
    }

    public int getSickLeaves() {
        return sickLeaves;
    }

}
