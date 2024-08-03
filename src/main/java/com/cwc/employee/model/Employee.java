package com.cwc.employee.model;

import com.cwc.employee.model.enums.Department;
import com.cwc.employee.model.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int empId;
    private String employeeName;
    @Enumerated(EnumType.STRING)
    private Department department;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private int age;
    private double salary;
    @Transient
    private String category;
}
