package ru.danmax.app.delegates.shop;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.entity.Discount;
import ru.danmax.app.entity.Shop;
import ru.danmax.app.service.ShopService;

import java.util.List;

@Named("showCurrentShop")
@RequiredArgsConstructor
public class ShowCurrentShopDelegate implements JavaDelegate {
    private final ShopService shopService;
    private final String TAB = " ";

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long shopId = (Long) delegateExecution.getVariable("shop_id");
        Shop shop = shopService.getCurrent(shopId);

        delegateExecution.setVariable("shop_id", shop.getId());
        delegateExecution.setVariable("shop_name", shop.getName());
        delegateExecution.setVariable("shop_description", shop.getDescription());
        delegateExecution.setVariable("shop_category", shop.getCategory().getName());

        delegateExecution.setVariable("shop_discounts_count", shop.getDiscounts().size());
        delegateExecution.setVariable("shop_discounts", convertDiscountsToJson(shop.getDiscounts()));
    }

    private String convertDiscountsToJson(List<Discount> discounts){
        StringBuilder resultJson = new StringBuilder("[\n");
        int index = 0;
        for (Discount discount : discounts) {
            resultJson.append(addLeftTabToAllLine(convertDiscountToJson(discount)));
            index += 1;
            if (index != discounts.size()){
                resultJson.append(",\n");
            }
            else{
                resultJson.append("\n");
            }
        }
        resultJson.append("]");

        return resultJson.toString();
    }

    private String convertDiscountToJson(Discount discount){
        return String.format("""
                {
                %s "discount_id": %d,
                %s "discount_title": %s
                }""",
                TAB, discount.getId(),
                TAB, discount.getTitle());
    }

    private String addLeftTabToAllLine(String string){
        String[] lines = string.split("/n");
        String result = "";

        int index = 0;
        for (String line : lines){
            index += 1;
            if (index != lines.length){
                result = TAB + line + "/n";
            }
            else {
                result = TAB + line;
            }
        }
        return result;
    }
}
