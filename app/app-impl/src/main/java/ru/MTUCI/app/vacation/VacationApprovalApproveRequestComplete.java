package ru.MTUCI.app.vacation;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.springframework.stereotype.Component;
import ru.MTUCI.domain.Vacation;
import ru.MTUCI.email.EmailNotificationController;

@Component("vacationApprovalApproveRequestComplete")
public class VacationApprovalApproveRequestComplete extends VacationApprovalCommonTaskComplete {
    private EmailNotificationController notificationController;
    private VacationService vacationService;

    public VacationApprovalApproveRequestComplete(EmailNotificationController notificationController, VacationService vacationService) {
        this.notificationController = notificationController;
        this.vacationService = vacationService;
    }

    @Override
    public void notify(DelegateTask task) {
        super.notify(task);

        if ("approve".equals(getTaskVariable(task, "action"))) {
            Vacation approvedVacation = getTaskVariable(task, "vacation");
            vacationService.saveVacation(approvedVacation);
            notificationController.notifyOnVacationApproval(approvedVacation);
        }
    }
}
