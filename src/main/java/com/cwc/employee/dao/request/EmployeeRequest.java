package com.cwc.employee.dao.request;

import com.cwc.employee.model.enums.Department;
import com.cwc.employee.model.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class EmployeeRequest {
    private int empId;
    private String employeeName;
    private Department department;
    private Gender gender;
    private int age;
    private double salary;
    private String category;
}

