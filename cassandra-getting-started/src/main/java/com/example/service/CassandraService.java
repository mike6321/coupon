package com.example.service;

import com.example.entity.Employee;
import com.example.entity.EmployeePrimaryKey;
import com.example.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CassandraService {

    private final EmployeeRepository employeeRepository;

    public void casTest() {
        Employee employee1 = new Employee(
                new EmployeePrimaryKey("seoul", "business", "key"),
                "010-1234-1234"
        );
        employeeRepository.save(employee1);

        Employee employee2 = new Employee(
                new EmployeePrimaryKey("seoul", "business", "joy"),
                "010-5678-1234"
        );
        employeeRepository.save(employee2);

        var result = employeeRepository.findByKeyLocationAndKeyDepartment("seoul", "business");

        result.stream()
                .map(e -> String.format("location: %s, department: %s, name: %s, phoneNumber: %s",
                        e.key.location, e.key.department, e.key.name, e.phoneNumber))
                .forEach(System.out::println);
    }

}
