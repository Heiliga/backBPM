package ru.MTUCI.hibernate.vacation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.MTUCI.domain.Vacation;

import java.util.List;

public interface VacationRepository extends JpaRepository<Vacation, Long> {

    List<Vacation> findAllForEmployeeId(@Param("employee_id") Long employeeId);

}
