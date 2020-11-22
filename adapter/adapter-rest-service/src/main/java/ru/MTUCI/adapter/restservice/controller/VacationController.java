package ru.MTUCI.adapter.restservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.MTUCI.app.vacation.VacationService;
import ru.MTUCI.domain.dto.VacationDto;

@RestController
@RequestMapping(VacationController.BASE_MAPPING)
public class VacationController {

    protected final static String BASE_MAPPING = "/vacation/v1";
    private final String ACCEPT_MAPPING = "/accept";

    @Autowired
    private VacationService vacationService;

    @PostMapping(ACCEPT_MAPPING)
    public ResponseEntity<?> acceptVacation(@RequestBody VacationDto dto) {
        vacationService.acceptVacation(dto);
        return null;
    }

}
