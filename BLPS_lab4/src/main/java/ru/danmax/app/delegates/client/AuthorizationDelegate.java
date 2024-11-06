package ru.danmax.app.delegates.client;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.dto.AuthorizationDTO;
import ru.danmax.app.entity.Client;
import ru.danmax.app.entity.Role;
import ru.danmax.app.service.ClientService;

import java.util.Objects;
import java.util.Set;

@Named("authorization")
@RequiredArgsConstructor
public class AuthorizationDelegate implements JavaDelegate {
    private final ClientService clientService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = (String) delegateExecution.getVariable("username");
        String password = (String) delegateExecution.getVariable("password");

        Client client = clientService.auth(AuthorizationDTO.builder()
                .username(username)
                .password(password)
                .build());

        Role clientRole = findRole(client.getRoles());

        delegateExecution.setVariable("client_id", client.getId());
        delegateExecution.setVariable("role", clientRole.getName());
    }

    private Role findRole(Set<Role> roles){
        boolean isSystemAdmin = false;
        boolean isShopAdmin = false;

        for (Role role : roles){
            if (Objects.equals(role.getName(), "SYSTEM_ADMIN")){
                isSystemAdmin = true;
            }
            if (Objects.equals(role.getName(), "SHOP_ADMIN")){
                isShopAdmin = true;
            }
        }

        if (isSystemAdmin) return Role.builder().name("SYSTEM_ADMIN").build();
        if (isShopAdmin) return Role.builder().name("SHOP_ADMIN").build();
        return  Role.builder().name("USER").build();
    }
}
