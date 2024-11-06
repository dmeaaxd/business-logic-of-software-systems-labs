package ru.danmax.app.delegates.favorite;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.entity.Favorite;
import ru.danmax.app.service.FavoriteService;

import java.util.List;

@Named("showAllFavorites")
@RequiredArgsConstructor
public class ShowAllFavoritesDelegate implements JavaDelegate {
    private final FavoriteService favoriteService;
    private final String TAB = " ";

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long clientId = (Long) delegateExecution.getVariable("client_id");

        List<Favorite> favorites = favoriteService.getAll(clientId);
        delegateExecution.setVariable("favorites_count", favorites.size());
        delegateExecution.setVariable("favorites", convertFavoritesToJson(favorites));
    }

    private String convertFavoritesToJson(List<Favorite> favorites){
        StringBuilder resultJson = new StringBuilder("[\n");
        int index = 0;
        for (Favorite favorite : favorites) {
            resultJson.append(addLeftTabToAllLine(convertFavoriteToJson(favorite)));
            index += 1;
            if (index != favorites.size()){
                resultJson.append(",\n");
            }
            else{
                resultJson.append("\n");
            }
        }
        resultJson.append("]");

        return resultJson.toString();
    }

    private String convertFavoriteToJson(Favorite favorite){
        return String.format("""
                {
                %s "favorite_id": %d,
                %s "shop_id": %d,
                %s "shop_name": %s
                }""",
                TAB, favorite.getId(),
                TAB, favorite.getShop().getId(),
                TAB, favorite.getShop().getName());
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
