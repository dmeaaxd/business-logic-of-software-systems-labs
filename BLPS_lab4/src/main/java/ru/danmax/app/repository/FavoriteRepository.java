package ru.danmax.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.danmax.app.entity.Favorite;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByClientIdAndShopId(Long clientId, Long shopId);

    Optional<Favorite> findByClientIdAndShopId(Long clientId, Long shopId);

    List<Favorite> findAllByClientId(Long clientId);

    List<Favorite> findAllByShopId(Long shopId);
}
