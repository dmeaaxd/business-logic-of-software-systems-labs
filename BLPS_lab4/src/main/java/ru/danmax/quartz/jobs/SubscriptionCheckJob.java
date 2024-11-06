package ru.danmax.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import ru.danmax.app.entity.Subscription;
import ru.danmax.app.repository.SubscriptionRepository;
import ru.danmax.jms.message.NotificationJmsMessage;
import ru.danmax.jms.sender.JmsNotificationSender;

import java.time.LocalDate;
import java.time.Period;


public class SubscriptionCheckJob implements Job {
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private JmsNotificationSender jmsNotificationSender;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Задача SubscriptionCheckJob запущена");
        for (Subscription subscription : subscriptionRepository.findAll()){
            if (subscription.getStartDate().plus(Period.ofDays(subscription.getDuration())).isBefore(LocalDate.now())){
                try {
                    jmsNotificationSender.sendNotification(NotificationJmsMessage.builder()
                            .to(subscription.getClient().getEmail())
                            .theme("Окончание подписки")
                            .text("Ваша подписка на магазин " + subscription.getShop().getName() + " закончена. \n" +
                                    "Продлите подписку)")
                            .build());
                } catch (Exception e) {
                    System.out.println("Неудалось отправить уведомление об окончании подписки, требуется вмешательство программиста");
                }
                subscriptionRepository.delete(subscription);
            }
        }
    }
}
