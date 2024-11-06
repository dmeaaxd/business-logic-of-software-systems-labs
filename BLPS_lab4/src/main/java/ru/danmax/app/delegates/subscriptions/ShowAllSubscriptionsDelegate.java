package ru.danmax.app.delegates.subscriptions;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.entity.Subscription;
import ru.danmax.app.service.SubscriptionService;

import java.time.Period;
import java.util.List;

@Named("showAllSubscriptions")
@RequiredArgsConstructor
public class ShowAllSubscriptionsDelegate implements JavaDelegate {
    private final SubscriptionService subscriptionService;
    private final String TAB = " ";

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long clientId = (Long) delegateExecution.getVariable("client_id");
        List<Subscription> subscriptions = subscriptionService.getSubscriptions(clientId);

        delegateExecution.setVariable("subscriptions_count", subscriptions.size());
        delegateExecution.setVariable("subscriptions", convertSubscriptionsToJson(subscriptions));
    }

    private String convertSubscriptionsToJson(List<Subscription> subscriptions){
        StringBuilder resultJson = new StringBuilder("[\n");
        int index = 0;
        for (Subscription subscription : subscriptions) {
            resultJson.append(addLeftTabToAllLine(convertSubscriptionToJson(subscription)));
            index += 1;
            if (index != subscriptions.size()){
                resultJson.append(",\n");
            }
            else{
                resultJson.append("\n");
            }
        }
        resultJson.append("]");

        return resultJson.toString();
    }

    private String convertSubscriptionToJson(Subscription subscription){
        return String.format("""
                {
                %s "subscription_id": %d,
                %s "shop_id": %d,
                %s "shop_name": %s,
                %s "endDate": %s
                }""",
                TAB, subscription.getId(),
                TAB, subscription.getShop().getId(),
                TAB, subscription.getShop().getName(),
                TAB, subscription.getStartDate().plus(Period.ofDays(subscription.getDuration())));
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
