package ru.danmax.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.danmax.app.entity.Client;
import ru.danmax.app.entity.Shop;
import ru.danmax.app.entity.Subscription;

import java.util.List;


public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findByClientAndShop(Client client, Shop shop);

    List<Subscription> findAllByClientId(Long clientId);
    List<Subscription> findAllByShopId(Long shopId);
}
