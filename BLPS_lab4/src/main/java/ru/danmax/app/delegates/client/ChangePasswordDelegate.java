package ru.danmax.app.delegates.client;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.service.ClientService;

@Named("changePassword")
@RequiredArgsConstructor
public class ChangePasswordDelegate implements JavaDelegate {
    private final ClientService clientService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = (String) delegateExecution.getVariable("username");
        String restorePassword = (String) delegateExecution.getVariable("restorePassword");
        String newPassword = (String) delegateExecution.getVariable("newPassword");

        clientService.changePassword(username, restorePassword, newPassword);
    }
}
