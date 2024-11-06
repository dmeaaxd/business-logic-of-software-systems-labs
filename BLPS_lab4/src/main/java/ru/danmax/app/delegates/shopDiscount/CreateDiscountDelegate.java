package ru.danmax.app.delegates.shopDiscount;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.dto.CreateDiscountDTO;
import ru.danmax.app.service.ShopDiscountService;

@Named("createDiscount")
@RequiredArgsConstructor
public class CreateDiscountDelegate implements JavaDelegate {
    private final ShopDiscountService shopDiscountService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long shopId = (Long) delegateExecution.getVariable("shop_id");
        String title = (String) delegateExecution.getVariable("discount_title");
        String description = (String) delegateExecution.getVariable("discount_description");
        String promoCode = (String) delegateExecution.getVariable("discount_promocode");


        shopDiscountService.create(CreateDiscountDTO.builder()
                .shopId(shopId)
                .title(title)
                .description(description)
                .promoCode(promoCode)
                .build());
    }
}
