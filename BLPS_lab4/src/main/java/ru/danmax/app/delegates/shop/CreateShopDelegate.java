package ru.danmax.app.delegates.shop;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.dto.CreateShopDTO;
import ru.danmax.app.service.ShopService;

@Named("createShop")
@RequiredArgsConstructor
public class CreateShopDelegate implements JavaDelegate {
    private final ShopService shopService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String name = (String) delegateExecution.getVariable("shop_name");
        String description = (String) delegateExecution.getVariable("shop_description");
        Long categoryId = (Long) delegateExecution.getVariable("shop_category_id");

        shopService.create(CreateShopDTO.builder()
                .name(name)
                .description(description)
                .categoryId(categoryId)
                .build());
    }
}
