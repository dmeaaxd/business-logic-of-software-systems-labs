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
public class CreateShopDTO implements FieldIsEmptyCheck {

    private String name;
    private String description;
    private Long categoryId;

    @Override
    public boolean isFieldEmpty() {
        if (name == null || name.isEmpty()) return true;
        if (description == null || description.isEmpty()) return true;
        return categoryId == null;
    }
}
