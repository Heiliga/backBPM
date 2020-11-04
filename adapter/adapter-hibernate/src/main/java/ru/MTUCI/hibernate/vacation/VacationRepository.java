package ru.MTUCI.hibernate.vacation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.MTUCI.domain.Vacation;

public interface VacationRepository extends JpaRepository<Vacation, Long> {
}
