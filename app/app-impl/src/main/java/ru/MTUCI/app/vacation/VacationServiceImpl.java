package ru.MTUCI.app.vacation;

import org.springframework.stereotype.Service;
import ru.MTUCI.hibernate.vacation.VacationRepository;
import ru.MTUCI.domain.Vacation;

@Service
public class VacationServiceImpl implements VacationService {
    private VacationRepository vacationRepository;

    public VacationServiceImpl(VacationRepository vacationRepository) {
        this.vacationRepository = vacationRepository;
    }

    @Override
    public void saveVacation(Vacation vacation) {
        vacationRepository.save(vacation);
    }
}
