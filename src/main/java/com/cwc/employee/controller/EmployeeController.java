package com.cwc.employee.controller;

import com.cwc.employee.model.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cwc.employee.dao.request.EmployeeRequest;
import com.cwc.employee.dao.response.EmployeeResponse;
import com.cwc.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/")
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeRequest employeeRequest) {
        Employee employee = employeeService.saveEmployee(employeeRequest);
        return new ResponseEntity<>(employee,HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<EmployeeResponse>> getEmployees() {
        List<EmployeeResponse> employeeResponses = employeeService.fetchAndProcessEmployees();
        return new ResponseEntity<>(employeeResponses,HttpStatus.OK);
    }
}

