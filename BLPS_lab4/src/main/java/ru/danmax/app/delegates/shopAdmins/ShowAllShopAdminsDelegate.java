package ru.danmax.app.delegates.shopAdmins;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.entity.Client;
import ru.danmax.app.service.ShopAdminsService;

import java.util.List;

@Named("showAllShopAdmins")
@RequiredArgsConstructor
public class ShowAllShopAdminsDelegate implements JavaDelegate {
    private final ShopAdminsService shopAdminsService;
    private final String TAB = " ";

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long shopId = (Long) delegateExecution.getVariable("shop_id");
        List<Client> admins = shopAdminsService.getShopAdmins(shopId);

        delegateExecution.setVariable("shop_admins_count", admins.size());
        delegateExecution.setVariable("shop_admins", convertClientsToJson(admins));
    }

    private String convertClientsToJson(List<Client> clients){
        StringBuilder resultJson = new StringBuilder("[\n");
        int index = 0;
        for (Client client : clients) {
            resultJson.append(addLeftTabToAllLine(convertClientToJson(client)));
            index += 1;
            if (index != clients.size()){
                resultJson.append(",\n");
            }
            else{
                resultJson.append("\n");
            }
        }
        resultJson.append("]");

        return resultJson.toString();
    }

    private String convertClientToJson(Client client){
        return String.format("""
                {
                %s "client_id": %d,
                %s "client_username": %s
                }""",
                TAB, client.getId(),
                TAB, client.getUsername());
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
