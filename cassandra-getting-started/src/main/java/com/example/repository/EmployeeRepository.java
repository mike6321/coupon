package com.example.repository;

import com.example.entity.Employee;
import com.example.entity.EmployeePrimaryKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends CassandraRepository<Employee, EmployeePrimaryKey> {

    List<Employee> findByKeyLocationAndKeyDepartment(final String location, final String department);

}
