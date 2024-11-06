package ru.danmax.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.danmax.app.entity.Discount;


public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
