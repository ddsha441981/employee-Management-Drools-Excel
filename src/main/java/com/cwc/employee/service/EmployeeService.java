package com.cwc.employee.service;

import com.cwc.employee.dao.request.EmployeeRequest;
import com.cwc.employee.dao.response.EmployeeResponse;
import com.cwc.employee.model.Employee;

import java.util.List;

public interface EmployeeService {

    Employee saveEmployee(EmployeeRequest employeeRequest);
    List<EmployeeResponse> fetchAndProcessEmployees();
}
