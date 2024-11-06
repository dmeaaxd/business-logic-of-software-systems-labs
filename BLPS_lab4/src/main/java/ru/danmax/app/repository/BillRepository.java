package ru.danmax.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.danmax.app.entity.Bill;


public interface BillRepository extends JpaRepository<Bill, Long> {
}
