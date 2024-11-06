package ru.danmax.app.delegates.client;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.dto.RegistrationDTO;
import ru.danmax.app.service.ClientService;

@Named("registration")
@RequiredArgsConstructor
public class RegistrationDelegate  implements JavaDelegate {
    private final ClientService clientService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = (String) delegateExecution.getVariable("username");
        String password = (String) delegateExecution.getVariable("password");
        String email = (String) delegateExecution.getVariable("email");

        clientService.register(RegistrationDTO.builder()
                .username(username)
                .password(password)
                .email(email)
                .build());
    }
}
