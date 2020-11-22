package ru.MTUCI.app.vacation;

import org.springframework.stereotype.Service;
import ru.MTUCI.domain.Employee;
import ru.MTUCI.domain.Vacation;
import ru.MTUCI.domain.dto.VacationDto;
import ru.MTUCI.hibernate.vacation.VacationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacationServiceImpl implements VacationService {
    private VacationRepository vacationRepository;

    public VacationServiceImpl(VacationRepository vacationRepository) {
        this.vacationRepository = vacationRepository;
    }

    @Override
    public void checkVacationForAnotherExists(Vacation vacation) {
        Employee employee = vacation.getEmployee();
        List<Vacation> employeeVacations = vacationRepository.findAllForEmployeeId(employee.getId());

        employeeVacations = employeeVacations.stream()
                .filter(v -> v.getStart().getYear() == LocalDate.now().getYear()).collect(Collectors.toList());

        if (!employeeVacations.isEmpty()) {
            throw new RuntimeException("Employee already have vacation");
        }
    }

    @Override
    public void saveVacation(Vacation vacation) {
        vacationRepository.save(vacation);
    }

    @Override
    public VacationDto acceptVacation(VacationDto vacationDto) {
        Vacation vacation = vacationRepository.getOne(vacationDto.getId());
        checkVacationForAnotherExists(vacation);
        vacation.setAccepted(true);
        vacationRepository.save(vacation);
        vacationDto.setAccepted(true);
        return vacationDto;
    }
}
