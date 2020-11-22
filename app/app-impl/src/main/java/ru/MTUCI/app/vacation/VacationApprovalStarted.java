package ru.MTUCI.app.vacation;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ru.MTUCI.app.datetime.DateTimeService;
import ru.MTUCI.app.employee.EmployeeService;
import ru.MTUCI.domain.Employee;
import ru.MTUCI.domain.Vacation;

import java.time.LocalDate;

@Component("vacationApprovalStarted")
public class VacationApprovalStarted implements JavaDelegate {

    private IdentityService camundaIdentityService;

    private EmployeeService employeeService;

    private DateTimeService dateTimeService;

    public VacationApprovalStarted(IdentityService camundaIdentityService, EmployeeService employeeService, DateTimeService dateTimeService) {
        this.camundaIdentityService = camundaIdentityService;
        this.employeeService = employeeService;
        this.dateTimeService = dateTimeService;
    }

    @Override
    public void execute(DelegateExecution process) {
        String initiatorLogin = getInitiatorLogin();
        Employee employee = employeeService.findByLogin(initiatorLogin);
        Vacation vacation = createDefaultVacationForEmployee(employee);
        Employee approver = employeeService.findWellKnownEmployeeHeadOfHr();
        String businessKey = getBusinessKey(process);

        process.setProcessBusinessKey(businessKey);
        process.setVariable("initiatorLogin", initiatorLogin);
        process.setVariable("approverLogin", approver.getLogin());
        process.setVariable("processName", getProcessName(employee));
        process.setVariable("processBusinessKey", businessKey);
        process.setVariable("vacation", vacation);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private String getInitiatorLogin() {
        return camundaIdentityService.getCurrentAuthentication().getUserId();
    }

    private Vacation createDefaultVacationForEmployee(Employee employee) {
        Vacation vacation = new Vacation();
        vacation.setEmployee(employee);

        LocalDate currentDate = dateTimeService.getCurrentDate();
        vacation.setStart(currentDate);
        vacation.setEnd(currentDate.plusWeeks(2));

        return vacation;
    }

    private String getBusinessKey(DelegateExecution process) {
        return "ОТПУСК-" + process.getId();
    }

    private String getProcessName(Employee initiator) {
        return "Согласование отпуска: " + initiator.getFullName();
    }
}
