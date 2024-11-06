package ru.danmax.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.danmax.app.entity.Shop;


@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    boolean existsByName(String name);
}
