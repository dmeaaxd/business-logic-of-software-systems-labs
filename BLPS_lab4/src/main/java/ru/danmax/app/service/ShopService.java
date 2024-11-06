package ru.danmax.app.service;

import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.danmax.app.dto.CreateShopDTO;
import ru.danmax.app.dto.UpdateShopDTO;
import ru.danmax.app.entity.Category;
import ru.danmax.app.entity.Shop;
import ru.danmax.app.repository.*;

import java.util.List;

@Service
@AllArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final ClientRepository clientRepository;
    private final CategoryRepository categoryRepository;
    private final DiscountRepository discountRepository;
    private final FavoriteRepository favoriteRepository;
    private final SubscriptionRepository subscriptionRepository;

    public List<Shop> getAll() {
        return shopRepository.findAll();
    }


    public Shop getCurrent(Long shopId) throws Exception {
        if (shopId == null) {
            throw new Exception("Shop id cannot be empty");
        }

        return shopRepository.findById(shopId)
                .orElseThrow(() -> new Exception("Shop not found"));
    }


    public void create(CreateShopDTO createShopDTO) throws Exception {
        if (createShopDTO.isFieldEmpty()) {
            throw new Exception("Fields cannot be empty");
        }

        Category category = categoryRepository.findById(createShopDTO.getCategoryId())
                .orElseThrow(() -> new Exception("Category not found"));

        if (shopRepository.existsByName(createShopDTO.getName())) {
            throw new Exception("Shop name is already taken");
        }

        Shop shop = Shop.builder()
                .name(createShopDTO.getName())
                .description(createShopDTO.getDescription())
                .category(category)
                .build();

        shopRepository.save(shop);
    }


    public void update(UpdateShopDTO updateShopDTO) throws Exception {
        if (updateShopDTO.isAllFieldsEmpty()) {
            throw new Exception("All fields cannot be empty");
        }

        Shop shop = getCurrent(updateShopDTO.getShopId());

        if (!(updateShopDTO.getName() == null || updateShopDTO.getName().isEmpty())) {
            if (shopRepository.existsByName(updateShopDTO.getName())) {
                throw new Exception("Shop name is already taken");
            }

            shop.setName(updateShopDTO.getName());
        }

        if (!(updateShopDTO.getDescription() == null || updateShopDTO.getDescription().isEmpty())) {
            shop.setDescription(updateShopDTO.getDescription());
        }

        if (updateShopDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(updateShopDTO.getCategoryId())
                    .orElseThrow(() -> new Exception("Category not found"));

            shop.setCategory(category);
        }

        shopRepository.save(shop);
    }

    @Transactional(rollbackFor = ObjectNotFoundException.class)
    public void delete(Long shopId) throws Exception {
        if (shopId == null) {
            throw new Exception("Shop id cannot be empty");
        }

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new Exception("Shop not found"));

        try {
            clientRepository.deleteAll(shop.getAdmins());
            discountRepository.deleteAll(shop.getDiscounts());
            favoriteRepository.deleteAll(favoriteRepository.findAllByShopId(shop.getId()));
            subscriptionRepository.deleteAll(subscriptionRepository.findAllByShopId(shop.getId()));
        } catch (Exception e) {
            throw new Exception("System error: Cannot delete related posts");
        }

        shopRepository.deleteById(shopId);
    }
}
