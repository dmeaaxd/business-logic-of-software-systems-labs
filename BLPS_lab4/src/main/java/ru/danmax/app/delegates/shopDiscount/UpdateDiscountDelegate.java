package ru.danmax.app.delegates.shopDiscount;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.dto.UpdateDiscountDTO;
import ru.danmax.app.service.ShopDiscountService;

@Named("updateDiscount")
@RequiredArgsConstructor
public class UpdateDiscountDelegate implements JavaDelegate {
    private final ShopDiscountService shopDiscountService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long discountId = (Long) delegateExecution.getVariable("discount_id");
        String title = (String) delegateExecution.getVariable("discount_title");
        String description = (String) delegateExecution.getVariable("discount_description");
        String promoCode = (String) delegateExecution.getVariable("discount_promocode");


        shopDiscountService.update(UpdateDiscountDTO.builder()
                .discountId(discountId)
                .title(title)
                .description(description)
                .promoCode(promoCode)
                .build());
    }
}
