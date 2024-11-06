package ru.danmax.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.danmax.app.dto.interfaces.AllFieldIsEmptyCheck;
import ru.danmax.app.dto.interfaces.FieldIsEmptyCheck;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShopDTO implements FieldIsEmptyCheck, AllFieldIsEmptyCheck {

    private Long shopId;
    private String name;
    private String description;
    private Long categoryId;

    @Override
    public boolean isFieldEmpty() {
        if (shopId == null) return true;
        if (name == null || name.isEmpty()) return true;
        if (description == null || description.isEmpty()) return true;
        return categoryId == null;
    }

    @Override
    public boolean isAllFieldsEmpty() {
        return ((name == null || name.isEmpty()) &&
                (description == null || description.isEmpty()) &&
                (categoryId == null));
    }
}
