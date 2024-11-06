package ru.danmax.app.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.danmax.app.entity.Client;
import ru.danmax.app.entity.Shop;
import ru.danmax.app.entity.Subscription;
import ru.danmax.app.exceptions.InsufficientFundsException;
import ru.danmax.app.repository.ClientRepository;
import ru.danmax.app.repository.ShopRepository;
import ru.danmax.app.repository.SubscriptionRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final ClientRepository clientRepository;
    private final ShopRepository shopRepository;
    private final PaymentService paymentService;

    public List<Subscription> getSubscriptions(Long clientId) throws Exception {
        if (clientId == null) {
            throw new Exception("Client id cannot be empty");
        }

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new Exception("User not found"));

        return subscriptionRepository.findAllByClientId(client.getId());
    }

    @Transactional(rollbackFor = Exception.class, noRollbackFor = InsufficientFundsException.class)
    public Subscription subscribe(Long clientId, Long shopId, int duration) throws Exception {
        if (clientId == null) {
            throw new Exception("Client id cannot be empty");
        }

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new Exception("User not found"));

        if (shopId == null) {
            throw new Exception("Shop id cannot be empty");
        }

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new Exception("Shop not found"));

        int ONE_DAY_SUBSCRIPTION_PRICE = 10;
        paymentService.writeOff(clientId, (long) duration * ONE_DAY_SUBSCRIPTION_PRICE);

        Subscription existingSubscription = subscriptionRepository.findByClientAndShop(client, shop);
        if (existingSubscription != null) {
            existingSubscription.setDuration(existingSubscription.getDuration() + duration);
        } else {
            existingSubscription = Subscription.builder()
                    .client(client)
                    .shop(shop)
                    .startDate(LocalDate.now())
                    .duration(duration)
                    .build();
        }

        return subscriptionRepository.save(existingSubscription);
    }
}

