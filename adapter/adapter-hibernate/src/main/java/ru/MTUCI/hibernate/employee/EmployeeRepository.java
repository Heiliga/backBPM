package ru.MTUCI.hibernate.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.MTUCI.domain.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByLoginIgnoreCase(String login);

    List<Employee> findAll();

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);
}
