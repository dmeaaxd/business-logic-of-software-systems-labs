package ru.danmax.app.delegates.subscriptions;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.exceptions.InsufficientFundsException;
import ru.danmax.app.service.SubscriptionService;

@Named("subscribe")
@RequiredArgsConstructor
public class SubscribeDelegate implements JavaDelegate {
    private final SubscriptionService subscriptionService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long clientId = (Long) delegateExecution.getVariable("client_id");
        Long shopId = (Long) delegateExecution.getVariable("shop_id");
        int duration = ((Long) delegateExecution.getVariable("duration")).intValue();

        try {
            subscriptionService.subscribe(clientId, shopId, duration);
            delegateExecution.setVariable("insufficientFundsFlag", false);
        } catch (Exception exception){
            if (exception instanceof InsufficientFundsException){
                delegateExecution.setVariable("insufficientFundsFlag", true);
            }
            else {
                throw exception;
            }
        }
    }
}
