package com.cwc.employee.service.impl;

import com.cwc.employee.dao.request.EmployeeRequest;
import com.cwc.employee.dao.response.EmployeeResponse;
import com.cwc.employee.model.Employee;
import com.cwc.employee.repository.EmployeeRepository;
import com.cwc.employee.service.EmployeeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;
    private KieContainer kieContainer;

    @PostConstruct
    public void initializeKieContainer() {
        try {
            KieServices kieServices = KieServices.Factory.get();
            KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
            Resource dt = ResourceFactory.newClassPathResource("SalaryRules.xls");
            kieFileSystem.write(dt);

            KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
            if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
                throw new RuntimeException("KieBuilder errors: " + kieBuilder.getResults().toString());
            }

            KieRepository kieRepository = kieServices.getRepository();
            kieContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());

            kieContainer.getKieBase().getKiePackages().forEach(kiePackage -> {
                kiePackage.getRules().forEach(rule -> {
                    System.out.println("Loaded Rule: " + rule.getName());
                });
            });
        } catch (Exception e) {
            log.error("Error initializing KieContainer", e);
            throw new RuntimeException("Error initializing KieContainer", e);
        }
    }
    @Override
    public Employee saveEmployee(EmployeeRequest employeeRequest) {
        Employee employee = Employee.builder()
                .employeeName(employeeRequest.getEmployeeName())
                .department(employeeRequest.getDepartment())
                .gender(employeeRequest.getGender())
                .age(employeeRequest.getAge())
                .salary(employeeRequest.getSalary())
                .build();
        return employeeRepository.save(employee);
    }
    @Override
    public List<EmployeeResponse> fetchAndProcessEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        KieSession kieSession = kieContainer.newKieSession();
        try {
            for (Employee employee : employees) {
                ActionData actionData = new ActionData();
                kieSession.insert(employee);
                kieSession.insert(actionData);
                kieSession.fireAllRules();
                employee.setCategory(actionData.getCategory());
            }
        } finally {
            kieSession.dispose();
        }

        return employees.stream()
                .sorted((e1, e2) -> Double.compare(e2.getSalary(), e1.getSalary()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    private EmployeeResponse mapToResponse(Employee employee) {
        EmployeeResponse response = EmployeeResponse.builder()
                .empId(employee.getEmpId())
                .employeeName(employee.getEmployeeName())
                .department(employee.getDepartment())
                .gender(employee.getGender())
                .age(employee.getAge())
                .salary(employee.getSalary())
                .category(employee.getCategory())
                .build();
        return response;
    }

    public static class ActionData {
        private String category;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

}
