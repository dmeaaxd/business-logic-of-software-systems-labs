package ru.danmax.app.delegates.client;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.service.ClientService;

@Named("checkIsClientSystemAdmin")
@RequiredArgsConstructor
public class CheckIsClientSystemAdminDelegate implements JavaDelegate {
    private final ClientService clientService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long clientId = (Long) delegateExecution.getVariable("clientId");

        boolean result = clientService.isClientSystemAdmin(clientId);
        delegateExecution.setVariable("isSystemAdmin", result);
    }
}
