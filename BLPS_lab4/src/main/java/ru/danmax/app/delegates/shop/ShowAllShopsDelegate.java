package ru.danmax.app.delegates.shop;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.entity.Shop;
import ru.danmax.app.service.ShopService;

import java.util.List;

@Named("showAllShops")
@RequiredArgsConstructor
public class ShowAllShopsDelegate implements JavaDelegate {
    private final ShopService shopService;
    private final String TAB = " ";

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<Shop> shops = shopService.getAll();

        delegateExecution.setVariable("shops_count", shops.size());
        delegateExecution.setVariable("shops", convertShopsToJson(shops));
    }

    private String convertShopsToJson(List<Shop> shops){
        StringBuilder resultJson = new StringBuilder("[\n");
        int index = 0;
        for (Shop shop : shops) {
            resultJson.append(addLeftTabToAllLine(convertShopToJson(shop)));
            index += 1;
            if (index != shops.size()){
                resultJson.append(",\n");
            }
            else{
                resultJson.append("\n");
            }
        }
        resultJson.append("]");

        return resultJson.toString();
    }

    private String convertShopToJson(Shop shop){
        return String.format("""
                {
                %s "shop_id": %d,
                %s "shop_name": %s,
                %s "shop_category": %s
                }""",
                TAB, shop.getId(),
                TAB, shop.getName(),
                TAB, shop.getCategory().getName());
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
