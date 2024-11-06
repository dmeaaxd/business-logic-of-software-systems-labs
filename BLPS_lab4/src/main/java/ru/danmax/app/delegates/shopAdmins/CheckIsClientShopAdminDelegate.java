package ru.danmax.app.delegates.shopAdmins;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.service.ShopAdminsService;

@Named("checkIsClientShopAdmin")
@RequiredArgsConstructor
public class CheckIsClientShopAdminDelegate implements JavaDelegate {
    private final ShopAdminsService shopAdminsService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long clientId = (Long) delegateExecution.getVariable("client_id");
        Long shopId = (Long) delegateExecution.getVariable("shop_id");

        delegateExecution.setVariable("is_shop_admin", shopAdminsService.isClientShopAdmin(clientId, shopId));
    }
}
