package ru.bmtsu.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bmtsu.warehouse.entity.Item;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByModelAndSize(String model, String size);
}
