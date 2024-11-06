package ru.danmax.app.delegates.shop;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.dto.UpdateShopDTO;
import ru.danmax.app.service.ShopService;

@Named("updateShop")
@RequiredArgsConstructor
public class UpdateShopDelegate implements JavaDelegate {
    private final ShopService shopService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long shopId = (Long) delegateExecution.getVariable("shop_id");
        String name = (String) delegateExecution.getVariable("shop_name");
        String description = (String) delegateExecution.getVariable("shop_description");
        Long categoryId = (Long) delegateExecution.getVariable("shop_category_id");

        shopService.update(UpdateShopDTO.builder()
                .shopId(shopId)
                .name(name)
                .description(description)
                .categoryId(categoryId)
                .build());
    }
}
