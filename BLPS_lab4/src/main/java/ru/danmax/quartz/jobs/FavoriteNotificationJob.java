package ru.danmax.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import ru.danmax.app.entity.Client;
import ru.danmax.app.entity.Favorite;
import ru.danmax.app.entity.Shop;
import ru.danmax.app.entity.Subscription;
import ru.danmax.app.repository.ClientRepository;
import ru.danmax.app.repository.FavoriteRepository;
import ru.danmax.app.repository.SubscriptionRepository;
import ru.danmax.jms.message.NotificationJmsMessage;
import ru.danmax.jms.sender.JmsNotificationSender;

import java.util.ArrayList;
import java.util.List;

public class FavoriteNotificationJob implements Job {
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private JmsNotificationSender jmsNotificationSender;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Задача FavoriteNotificationJob запущена");
        List<Client> clientList = clientRepository.findAll();
        for (Client client : clientList) {
            List<Favorite> favorites = favoriteRepository.findAllByClientId(client.getId());
            List<Subscription> subscriptions = subscriptionRepository.findAllByClientId(client.getId());

            List<Shop> favoriteShops = new ArrayList<>();
            for (Favorite favorite : favorites) {
                favoriteShops.add(favorite.getShop());
            }

            List<Shop> subscriptionShops = new ArrayList<>();
            for (Subscription subscription : subscriptions) {
                subscriptionShops.add(subscription.getShop());
            }

            List<Shop> toNotification = new ArrayList<>();
            for (Shop shop : favoriteShops) {
                if (!containsShop(subscriptionShops, shop)) {
                    toNotification.add(shop);
                }
            }

            if (!toNotification.isEmpty()){
                StringBuilder notificationText = new StringBuilder();
                notificationText.append("В вашем избранном есть магазины, на которые вы не подписаны. \nОформите подпску на следующие магазины: \n");
                for (Shop shop : toNotification) {
                    notificationText.append(shop.getName()).append("\n");
                }

                try {
                    jmsNotificationSender.sendNotification(NotificationJmsMessage
                            .builder()
                            .to(client.getEmail())
                            .theme("Оформите подписку) ")
                            .text(notificationText.toString())
                            .build());
                } catch (Exception e) {
                    System.out.println("Неудалось отправить уведомление с предложением о подписке, требуется вмешательство программиста");
                }
            }
        }
    }

    private boolean containsShop(final List<Shop> list, final Shop shop) {
        return list.stream().anyMatch(o -> o.getId().equals(shop.getId()));
    }
}
