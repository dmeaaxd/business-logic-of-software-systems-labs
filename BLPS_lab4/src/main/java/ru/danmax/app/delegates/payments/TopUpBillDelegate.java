package ru.danmax.app.delegates.payments;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.service.PaymentService;

@Named("topUpBill")
@RequiredArgsConstructor
public class TopUpBillDelegate implements JavaDelegate {
    private final PaymentService paymentService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long clientId = (Long) delegateExecution.getVariable("client_id");
        Long amount = (Long) delegateExecution.getVariable("amount");

        paymentService.topUp(clientId, amount);
    }
}
