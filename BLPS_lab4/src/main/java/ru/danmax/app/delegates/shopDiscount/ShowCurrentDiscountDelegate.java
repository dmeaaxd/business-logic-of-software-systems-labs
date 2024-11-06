package ru.danmax.app.delegates.shopDiscount;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.entity.Discount;
import ru.danmax.app.service.ShopDiscountService;

@Named("showCurrentDiscount")
@RequiredArgsConstructor
public class ShowCurrentDiscountDelegate implements JavaDelegate {
    private final ShopDiscountService shopDiscountService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long discountId = (Long) delegateExecution.getVariable("discount_id");
        Discount discount = shopDiscountService.getCurrent(discountId);

        delegateExecution.setVariable("discount_id", discount.getId());
        delegateExecution.setVariable("discount_title", discount.getTitle());
        delegateExecution.setVariable("discount_description", discount.getDescription());
        delegateExecution.setVariable("discount_promocode", discount.getPromoCode());
    }
}
