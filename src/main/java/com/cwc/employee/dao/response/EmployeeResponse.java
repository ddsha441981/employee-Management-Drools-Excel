package com.cwc.employee.dao.response;

import com.cwc.employee.model.enums.Department;
import com.cwc.employee.model.enums.Gender;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class EmployeeResponse {
    private int empId;
    private String employeeName;
    private Department department;
    private Gender gender;
    private int age;
    private double salary;
    private String category;
}
