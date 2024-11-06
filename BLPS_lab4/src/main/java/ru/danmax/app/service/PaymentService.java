package ru.danmax.app.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.danmax.app.entity.Bill;
import ru.danmax.app.entity.Client;
import ru.danmax.app.exceptions.InsufficientFundsException;
import ru.danmax.app.repository.BillRepository;
import ru.danmax.app.repository.ClientRepository;


@Service
@AllArgsConstructor
public class PaymentService {

    private final BillRepository billRepository;
    private final ClientRepository clientRepository;

    public Bill getBill(Long clientId) throws Exception {
        if (clientId == null) {
            throw new Exception("Client id cannot be empty");
        }

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new Exception("User not found"));

        Bill bill = client.getAccountBill();
        if (bill == null){
            bill = Bill.builder()
                    .accountBill(0L)
                    .client(client)
                    .build();
            client.setAccountBill(bill);
            clientRepository.save(client);
        }

        return bill;
    }


    public void topUp(Long clientId, Long amount) throws Exception {
        if (clientId == null) {
            throw new Exception("Client id cannot be empty");
        }

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new Exception("User not found"));

        Bill bill = client.getAccountBill();
        if (bill == null){
            bill = Bill.builder()
                    .accountBill(0L)
                    .client(client)
                    .build();
            client.setAccountBill(bill);
            clientRepository.save(client);
        }

        bill = client.getAccountBill();
        bill.setAccountBill(bill.getAccountBill() + amount);
        billRepository.save(bill);
    }

    public void writeOff(Long clientId, Long amount) throws Exception {
        if (clientId == null) {
            throw new Exception("Client id cannot be empty");
        }

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new Exception("User not found"));

        Bill bill = client.getAccountBill();
        if (bill == null){
            bill = Bill.builder()
                    .accountBill(0L)
                    .client(client)
                    .build();
            client.setAccountBill(bill);
            clientRepository.save(client);
        }

        bill = client.getAccountBill();

        if (bill.getAccountBill() < amount) {
            throw new InsufficientFundsException("There are not enough funds on the bill");
        }

        bill.setAccountBill(bill.getAccountBill() - amount);
        billRepository.save(bill);
    }
}
