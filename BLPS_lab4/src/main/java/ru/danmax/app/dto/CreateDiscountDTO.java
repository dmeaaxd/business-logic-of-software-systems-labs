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
public class CreateDiscountDTO implements FieldIsEmptyCheck {

    private Long shopId;
    private String title;
    private String description;
    private String promoCode;

    @Override
    public boolean isFieldEmpty() {
        if (shopId == null) return true;
        if (title == null || title.isEmpty()) return true;
        if (description == null || description.isEmpty()) return true;
        return promoCode == null || promoCode.isEmpty();
    }
}
