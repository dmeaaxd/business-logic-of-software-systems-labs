package ru.danmax.app.delegates.payments;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.entity.Bill;
import ru.danmax.app.service.PaymentService;

@Named("showCurrentBill")
@RequiredArgsConstructor
public class ShowCurrentBillDelegate implements JavaDelegate {
    private final PaymentService paymentService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long clientId = (Long) delegateExecution.getVariable("client_id");
        Bill bill = paymentService.getBill(clientId);

        delegateExecution.setVariable("bill", bill.getAccountBill() + " у.е.");
    }
}
