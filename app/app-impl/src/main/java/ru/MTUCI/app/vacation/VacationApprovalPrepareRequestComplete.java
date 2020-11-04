package ru.MTUCI.app.vacation;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.springframework.stereotype.Component;

@Component("vacationApprovalPrepareRequestComplete")
public class VacationApprovalPrepareRequestComplete extends VacationApprovalCommonTaskComplete {

    @Override
    public void notify(DelegateTask task) {
        super.notify(task);

        if ("submit".equals(getTaskVariable(task, "action"))) {
            mapFromTaskToProcess(task, "vacation", "vacation");
        }
    }
}
