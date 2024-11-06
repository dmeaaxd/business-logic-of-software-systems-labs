package ru.danmax.app.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.danmax.app.dto.AuthorizationDTO;
import ru.danmax.app.dto.RegistrationDTO;
import ru.danmax.app.entity.Role;
import ru.danmax.app.entity.Client;
import ru.danmax.app.repository.RoleRepository;
import ru.danmax.app.repository.ClientRepository;
import ru.danmax.app.utils.BCryptPasswordEncoder;
import ru.danmax.jms.message.NotificationJmsMessage;
import ru.danmax.jms.sender.JmsNotificationSender;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class ClientService {
    private ClientRepository clientRepository;
    private RoleRepository roleRepository;

    private JmsNotificationSender jmsNotificationSender;

    public Client auth(AuthorizationDTO authorizationDTO) throws Exception {
        if (authorizationDTO.isFieldEmpty()){
            throw new Exception("Fields cannot be empty");
        }

        Client client = clientRepository.findByUsername(authorizationDTO.getUsername())
                .orElseThrow(() -> new Exception("User not found"));


        BCryptPasswordEncoder passwordEncoder = BCryptPasswordEncoder.getInstance();
        if (!passwordEncoder.matches(authorizationDTO.getPassword(), client.getPassword())){
            throw new Exception("Incorrect user data");
        }

        return client;
    }

    public void register(RegistrationDTO registrationDTO) throws Exception{
        if (registrationDTO.isFieldEmpty()){
            throw new Exception("Fields cannot be empty");
        }

        if (clientRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new Exception("Username is already taken");
        }

        if (clientRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new Exception("Email is already taken");
        }

        BCryptPasswordEncoder passwordEncoder = BCryptPasswordEncoder.getInstance();

        Client client = Client.builder()
                .username(registrationDTO.getUsername())
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .build();

        Role role = roleRepository.findByName("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        client.setRoles(roles);

        clientRepository.save(client);
    }

    public boolean isClientSystemAdmin(Long clientId) throws Exception {
        if (clientId == null){
            throw new Exception("ClientId cannot be empty");
        }

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new Exception("User not found"));

        Set<Role> roles = client.getRoles();
        for (Role role : roles){
            if (Objects.equals(role.getName(), "SYSTEM_ADMIN")){
                return true;
            }
        }
        return false;
    }

    public void generateRestorePassword(String username) throws Exception {
        if (username == null || username.isEmpty()){
            throw new Exception("Username cannot be empty");
        }

        Client client = clientRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("User not found"));


        // Генерация временного пароля (кода восстановления)
        final int LEN = 10;
        String alphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
        StringBuilder s = new StringBuilder(LEN);

        for (int i = 0; i < LEN; i++) {
            int ch = (int)(alphaNumericStr.length() * Math.random());
            s.append(alphaNumericStr.charAt(ch));
        }
        String restorePassword = s.toString();

        // Сохранение временного пароля (кода восстановления)
        BCryptPasswordEncoder passwordEncoder = BCryptPasswordEncoder.getInstance();
        client.setRestorePassword(passwordEncoder.encode(restorePassword));
        clientRepository.save(client);

        // Отправка кода восстановления на почту
        try{
            jmsNotificationSender.sendNotification(NotificationJmsMessage.builder()
                    .to(client.getEmail())
                    .theme("Восстановление пароля")
                    .text("Код восстановления: " + restorePassword).build());
        } catch (Exception e) {
            System.out.println("Неудалось отправить код восстановления, требуется вмешательство программиста");
        }
    }


    public void changePassword(String username, String restorePassword, String newPassword) throws Exception {
        if (restorePassword == null || restorePassword.isEmpty()){
            throw new Exception("Restore password cannot be empty");
        }

        if (newPassword == null || newPassword.isEmpty()){
            throw new Exception("New password cannot be empty");
        }

        if (username == null || username.isEmpty()){
            throw new Exception("Username cannot be empty");
        }

        Client client = clientRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("User not found"));

        BCryptPasswordEncoder passwordEncoder = BCryptPasswordEncoder.getInstance();
        if (!passwordEncoder.matches(restorePassword, client.getRestorePassword())){
            throw new Exception("Incorrect restore password");
        }

        client.setRestorePassword(null);
        client.setPassword(passwordEncoder.encode(newPassword));
        clientRepository.save(client);
    }
}
