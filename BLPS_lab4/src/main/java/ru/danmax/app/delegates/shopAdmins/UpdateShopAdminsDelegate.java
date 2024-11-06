package ru.danmax.app.delegates.shopAdmins;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.service.ShopAdminsService;

import java.util.ArrayList;
import java.util.List;

@Named("updateShopAdmins")
@RequiredArgsConstructor
public class UpdateShopAdminsDelegate implements JavaDelegate {
    private final ShopAdminsService shopAdminsService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long shopId = (Long) delegateExecution.getVariable("shop_id");
        String admins = (String) delegateExecution.getVariable("updated_shop_admins");

        List<Long> adminIdList = parseAdminIdsFromString(admins);

        shopAdminsService.update(shopId, adminIdList);
    }

    private List<Long> parseAdminIdsFromString(String rawAdmins) throws Exception {
        List<Long> adminIdList = new ArrayList<>();

        if (rawAdmins == null || rawAdmins.isEmpty()) {
            throw new Exception("List of admins cannot be empty");
        }

        String[] adminsArray = rawAdmins.split(", ");
        for (String rawAdminId : adminsArray){
            if (rawAdminId == null || rawAdminId.isEmpty()){
                throw new Exception("Incorrect format of admins");
            }

            try {
                Long adminId = Long.parseLong(rawAdminId);
                adminIdList.add(adminId);
            } catch (NumberFormatException numberFormatException){
                throw new Exception("Incorrect format of admins");
            }
        }

        return adminIdList;
    }
}
