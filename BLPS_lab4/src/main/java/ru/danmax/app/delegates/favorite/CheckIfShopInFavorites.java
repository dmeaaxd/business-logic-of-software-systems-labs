package ru.danmax.app.delegates.favorite;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.service.FavoriteService;

@Named("checkIfShopInFavorites")
@RequiredArgsConstructor
public class CheckIfShopInFavorites implements JavaDelegate {
    private final FavoriteService favoriteService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long clientId = (Long) delegateExecution.getVariable("client_id");
        Long shopId = (Long) delegateExecution.getVariable("shop_id");

        delegateExecution.setVariable("is_shop_in_favorites", favoriteService.isShopInFavorites(clientId, shopId));
    }
}
