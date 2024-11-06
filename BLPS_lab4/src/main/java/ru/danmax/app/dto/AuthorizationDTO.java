package ru.danmax.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.danmax.app.dto.interfaces.FieldIsEmptyCheck;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationDTO implements FieldIsEmptyCheck {
    private String username;
    private String password;

    @Override
    public boolean isFieldEmpty() {
        if (username == null || username.isEmpty()) return true;
        return password == null || password.isEmpty();
    }
}
