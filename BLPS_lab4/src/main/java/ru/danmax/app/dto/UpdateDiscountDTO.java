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
public class UpdateDiscountDTO implements FieldIsEmptyCheck, AllFieldIsEmptyCheck {

    private Long discountId;
    private String title;
    private String description;
    private String promoCode;

    @Override
    public boolean isFieldEmpty() {
        if (discountId == null) return true;
        if (title == null || title.isEmpty()) return true;
        if (description == null || description.isEmpty()) return true;
        return promoCode == null || promoCode.isEmpty();
    }

    @Override
    public boolean isAllFieldsEmpty() {
        return ((title == null || title.isEmpty()) &&
                (description == null || description.isEmpty()) &&
                (promoCode == null || promoCode.isEmpty()));
    }
}
